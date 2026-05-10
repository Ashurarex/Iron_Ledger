package ui;

import service.BMIService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BMIFrame extends JFrame {

    private final BMIService bmiService = new BMIService();

    private JTextField heightField;
    private JTextField weightField;
    private JLabel resultLabel;
    private JLabel categoryLabel;
    private JPanel visualIndicatorPanel;

    private JTable historyTable;
    private DefaultTableModel tableModel;

    public BMIFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - BMI Calculator");

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
        mainPanel.add(UITheme.createPageHeader("Body Mass Index Tool", backBtn), BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Calculator (Left)
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 32);
        contentPanel.add(createCalculatorPanel(), gbc);

        // History (Right)
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(createHistoryPanel(), gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        loadHistory();
    }

    private JPanel createCalculatorPanel() {
        JPanel calcCard = UITheme.createCardPanel();
        calcCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 16, 12, 16);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        calcCard.add(UITheme.createSectionLabel("⚖️  BMI Assessment"), gbc);

        gbc.gridy = 1;
        calcCard.add(UITheme.createFieldLabel("Height (cm)"), gbc);
        gbc.gridy = 2;
        heightField = new JTextField();
        UITheme.styleTextField(heightField);
        calcCard.add(heightField, gbc);

        gbc.gridy = 3;
        calcCard.add(UITheme.createFieldLabel("Weight (kg)"), gbc);
        gbc.gridy = 4;
        weightField = new JTextField();
        UITheme.styleTextField(weightField);
        calcCard.add(weightField, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(32, 16, 32, 16);
        JButton calcBtn = new JButton("➕ CALCULATE & LOG");
        UITheme.stylePrimaryButton(calcBtn);
        calcBtn.setFont(UITheme.FONT_ICON_SMALL);
        calcCard.add(calcBtn, gbc);

        // Result Area
        JPanel resultArea = new JPanel(new GridBagLayout());
        resultArea.setOpaque(false);
        resultArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        GridBagConstraints rbc = new GridBagConstraints();
        rbc.insets = new Insets(20, 0, 8, 0);
        rbc.gridx = 0; rbc.gridy = 0;

        resultLabel = new JLabel("BMI: --");
        resultLabel.setFont(UITheme.FONT_DISPLAY);
        resultLabel.setForeground(UITheme.TEXT_PRIMARY);
        resultArea.add(resultLabel, rbc);

        rbc.gridy = 1;
        rbc.insets = new Insets(0, 0, 20, 0);
        categoryLabel = new JLabel("Category: --");
        categoryLabel.setFont(UITheme.FONT_H2);
        categoryLabel.setForeground(UITheme.TEXT_SECONDARY);
        resultArea.add(categoryLabel, rbc);

        // Visual Indicator
        rbc.gridy = 2;
        rbc.fill = GridBagConstraints.HORIZONTAL;
        rbc.weightx = 1.0;
        visualIndicatorPanel = new JPanel(new GridLayout(1, 4, 4, 0));
        visualIndicatorPanel.setOpaque(false);
        visualIndicatorPanel.setPreferredSize(new Dimension(0, 10));
        
        Color[] colors = {UITheme.PRIMARY_GREEN.brighter(), UITheme.PRIMARY_GREEN, UITheme.ACCENT_GOLD, UITheme.DANGER_RUST};
        for (int i = 0; i < 4; i++) {
            JPanel segment = new JPanel();
            segment.setBackground(colors[i]);
            segment.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
            visualIndicatorPanel.add(segment);
        }
        resultArea.add(visualIndicatorPanel, rbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(10, 16, 10, 16);
        calcCard.add(resultArea, gbc);

        calcBtn.addActionListener(e -> calculateBMI());

        return calcCard;
    }

    private JPanel createHistoryPanel() {
        JPanel historyCard = UITheme.createCardPanel();
        historyCard.setLayout(new BorderLayout(0, 20));

        historyCard.add(UITheme.createSectionLabel("📉 Body Composition History"), BorderLayout.NORTH);

        String[] columns = {"ID", "Weight", "Height", "BMI", "Category", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        historyTable = new JTable(tableModel);
        UITheme.styleTable(historyTable);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        historyCard.add(scrollPane, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("🗑️ Remove Selected Entry");
        UITheme.styleSecondaryButton(deleteBtn);
        deleteBtn.setFont(UITheme.FONT_ICON_SMALL);
        deleteBtn.setForeground(UITheme.DANGER_RUST);
        deleteBtn.addActionListener(e -> deleteRecord());
        historyCard.add(deleteBtn, BorderLayout.SOUTH);

        return historyCard;
    }

    private void calculateBMI() {
        String hStr = heightField.getText();
        String wStr = weightField.getText();

        String result = bmiService.calculateAndSaveBMI(hStr, wStr);
        if (result.startsWith("SUCCESS")) {
            String[] parts = result.split(":");
            double bmiValue = Double.parseDouble(parts[1]);
            String cat = parts[2];
            
            resultLabel.setText(String.format("BMI: %.1f", bmiValue));
            categoryLabel.setText(cat);
            updateIndicator(cat);
            loadHistory();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateIndicator(String cat) {
        for (int i = 0; i < 4; i++) {
            JPanel p = (JPanel) visualIndicatorPanel.getComponent(i);
            p.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1));
        }
        int index = -1;
        if (cat.equalsIgnoreCase("Underweight")) index = 0;
        else if (cat.equalsIgnoreCase("Normal weight")) index = 1;
        else if (cat.equalsIgnoreCase("Overweight")) index = 2;
        else if (cat.equalsIgnoreCase("Obese")) index = 3;

        if (index != -1) {
            JPanel p = (JPanel) visualIndicatorPanel.getComponent(index);
            p.setBorder(BorderFactory.createLineBorder(UITheme.TEXT_PRIMARY, 2));
        }
    }

    private void loadHistory() {
        List<Object[]> history = bmiService.getBMIHistory();
        tableModel.setRowCount(0);
        for (Object[] b : history) {
            tableModel.addRow(b);
        }
    }

    private void deleteRecord() {
        int row = historyTable.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (bmiService.deleteBMIRecord(id)) {
                loadHistory();
            }
        }
    }
}