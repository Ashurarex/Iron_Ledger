package ui;

import service.ExportService;
import utils.WindowUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class ExportFrame extends JFrame {

    private final ExportService exportService = new ExportService();

    private JLabel statusLabel;

    public ExportFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Export Data");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Export Workout Data", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 245, 245));

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(45, 60, 45, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 14, 14, 14);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel infoLabel = new JLabel("Choose an export format for your workout records", JLabel.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JButton pdfButton = new JButton("Export to PDF");
        JButton excelButton = new JButton("Export to Excel");
        JButton backButton = new JButton("Back");

        styleButton(pdfButton, new Color(180, 40, 40));
        styleButton(excelButton, new Color(40, 150, 90));
        styleButton(backButton, Color.GRAY);

        statusLabel = new JLabel("Status: Ready", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(infoLabel, gbc);

        gbc.gridy = 1;
        cardPanel.add(pdfButton, gbc);

        gbc.gridy = 2;
        cardPanel.add(excelButton, gbc);

        gbc.gridy = 3;
        cardPanel.add(backButton, gbc);

        gbc.gridy = 4;
        cardPanel.add(statusLabel, gbc);

        centerWrapper.add(cardPanel);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        add(mainPanel);

        pdfButton.addActionListener(e -> exportPDF());
        excelButton.addActionListener(e -> exportExcel());

        backButton.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });
    }

    private void exportPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Workout Report as PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setSelectedFile(new java.io.File("iron_ledger_workout_report.pdf"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            String exportResult = exportService.exportToPDF(filePath);

            if (exportResult.equals("SUCCESS")) {
                statusLabel.setText("Status: PDF exported successfully.");
                JOptionPane.showMessageDialog(this, "PDF exported successfully.");
            } else {
                statusLabel.setText("Status: PDF export failed.");
                JOptionPane.showMessageDialog(this, exportResult, "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Workout Report as Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xls"));
        fileChooser.setSelectedFile(new java.io.File("iron_ledger_workout_report.xls"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            if (!filePath.toLowerCase().endsWith(".xls")) {
                filePath += ".xls";
            }

            String exportResult = exportService.exportToExcel(filePath);

            if (exportResult.equals("SUCCESS")) {
                statusLabel.setText("Status: Excel exported successfully.");
                JOptionPane.showMessageDialog(this, "Excel exported successfully.");
            } else {
                statusLabel.setText("Status: Excel export failed.");
                JOptionPane.showMessageDialog(this, exportResult, "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(300, 45));
    }
}