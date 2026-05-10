package ui;

import service.ExportService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExportFrame extends JFrame {

    private final ExportService exportService = new ExportService();

    public ExportFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Data Archival");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_MAIN);
        mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Header
        JButton backBtn = new JButton("BACK");
        UITheme.styleBackButton(backBtn);
        backBtn.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });
        mainPanel.add(UITheme.createPageHeader("Data Archival & Export", backBtn), BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(800, 500));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(24, 24, 24, 24);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weighty = 0.2;
        JLabel infoLabel = new JLabel("<html><center>Preserve your progress. Choose a format to export your complete training history and performance metrics.</center></html>", JLabel.CENTER);
        infoLabel.setFont(UITheme.FONT_MEDIUM);
        infoLabel.setForeground(UITheme.TEXT_SECONDARY);
        card.add(infoLabel, gbc);

        // PDF Export
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weighty = 0.8;
        card.add(createExportOption("📂  Portable PDF", "Ideal for formal records and professional printing of your workout logs.", "EXPORT PDF", UITheme.PRIMARY_GREEN, () -> exportPDF()), gbc);

        // Excel Export
        gbc.gridx = 1;
        card.add(createExportOption("📊  Excel Sheet", "Deep analysis and data science. Best for custom spreadsheets and long-term tracking.", "EXPORT XLSX", UITheme.ACCENT_GOLD, () -> exportExcel()), gbc);

        contentPanel.add(card);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createExportOption(String title, String desc, String btnText, Color btnColor, Runnable action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(32, 32, 32, 32)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_ICON_MEDIUM.deriveFont(Font.BOLD, 22f));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><center>" + desc + "</center></html>");
        descLabel.setFont(UITheme.FONT_REGULAR);
        descLabel.setForeground(UITheme.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setPreferredSize(new Dimension(220, 80));
        descLabel.setMaximumSize(new Dimension(220, 80));

        JButton btn = new JButton(btnText);
        UITheme.stylePrimaryButton(btn); // Use base styling
        btn.setFont(UITheme.FONT_ICON_SMALL);
        btn.setBackground(btnColor);     // Apply custom brand color
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 48));
        
        btn.addActionListener(e -> action.run());

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));
        panel.add(descLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 32)));
        panel.add(btn);

        return panel;
    }

    private void exportPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Training History as PDF");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) path += ".pdf";
            exportService.exportToPDF(path);
            JOptionPane.showMessageDialog(this, "Archive created successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Training History as Excel");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xlsx")) path += ".xlsx";
            exportService.exportToExcel(path);
            JOptionPane.showMessageDialog(this, "Spreadsheet created successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}