package ui;

import service.BMIService;
import utils.WindowUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BMIFrame extends JFrame {

    private JTextField heightField;
    private JTextField weightField;
    private JLabel bmiValueLabel;
    private JLabel bmiCategoryLabel;

    private JTable bmiTable;
    private DefaultTableModel tableModel;

    private int selectedBmiId = -1;

    private final BMIService bmiService = new BMIService();

    public BMIFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - BMI Calculator");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("BMI Calculator", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        loadBMIHistory();
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(400, 0));
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heightLabel = new JLabel("Height in cm:");
        JLabel weightLabel = new JLabel("Weight in kg:");

        heightField = new JTextField();
        weightField = new JTextField();

        heightLabel.setFont(new Font("Arial", Font.BOLD, 16));
        weightLabel.setFont(new Font("Arial", Font.BOLD, 16));
        heightField.setFont(new Font("Arial", Font.PLAIN, 16));
        weightField.setFont(new Font("Arial", Font.PLAIN, 16));

        addFormRow(formPanel, gbc, 0, heightLabel, heightField);
        addFormRow(formPanel, gbc, 1, weightLabel, weightField);

        JButton calculateButton = new JButton("Calculate and Save BMI");
        JButton clearButton = new JButton("Clear");
        JButton deleteButton = new JButton("Delete Selected Record");
        JButton backButton = new JButton("Back");

        styleButton(calculateButton, new Color(40, 120, 220));
        styleButton(clearButton, Color.DARK_GRAY);
        styleButton(deleteButton, new Color(180, 40, 40));
        styleButton(backButton, Color.GRAY);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(calculateButton, gbc);

        gbc.gridy = 3;
        formPanel.add(clearButton, gbc);

        gbc.gridy = 4;
        formPanel.add(deleteButton, gbc);

        JPanel resultPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        resultPanel.setBackground(new Color(245, 245, 245));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel resultTitle = new JLabel("BMI Result", JLabel.CENTER);
        resultTitle.setFont(new Font("Arial", Font.BOLD, 22));

        bmiValueLabel = new JLabel("BMI Value: -", JLabel.CENTER);
        bmiCategoryLabel = new JLabel("Category: -", JLabel.CENTER);

        bmiValueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bmiCategoryLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel hintLabel = new JLabel("Formula: weight / height²", JLabel.CENTER);
        hintLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        resultPanel.add(resultTitle);
        resultPanel.add(bmiValueLabel);
        resultPanel.add(bmiCategoryLabel);
        resultPanel.add(hintLabel);

        gbc.gridy = 5;
        formPanel.add(resultPanel, gbc);

        gbc.gridy = 6;
        formPanel.add(backButton, gbc);

        calculateButton.addActionListener(e -> calculateBMI());
        clearButton.addActionListener(e -> clearFields());
        deleteButton.addActionListener(e -> deleteSelectedBMI());
        backButton.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel historyLabel = new JLabel("BMI History", JLabel.LEFT);
        historyLabel.setFont(new Font("Arial", Font.BOLD, 22));
        historyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        tableModel = new DefaultTableModel(
                new Object[]{"BMI ID", "Height (cm)", "Weight (kg)", "BMI Value", "Category", "Date"},
                0
        );

        bmiTable = new JTable(tableModel);
        bmiTable.setRowHeight(28);
        bmiTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bmiTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(bmiTable);

        tablePanel.add(historyLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        bmiTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bmiTable.getSelectedRow() != -1) {
                int selectedRow = bmiTable.getSelectedRow();
                int modelRow = bmiTable.convertRowIndexToModel(selectedRow);

                selectedBmiId = (int) tableModel.getValueAt(modelRow, 0);

                heightField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
                weightField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
                bmiValueLabel.setText("BMI Value: " + tableModel.getValueAt(modelRow, 3));
                bmiCategoryLabel.setText("Category: " + tableModel.getValueAt(modelRow, 4));
            }
        });

        return tablePanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void calculateBMI() {
        String result = bmiService.calculateAndSaveBMI(
                heightField.getText().trim(),
                weightField.getText().trim()
        );

        if (result.startsWith("SUCCESS")) {
            String[] parts = result.split(":");

            String bmiValue = parts[1];
            String category = parts[2];

            bmiValueLabel.setText("BMI Value: " + bmiValue);
            bmiCategoryLabel.setText("Category: " + category);

            JOptionPane.showMessageDialog(this, "BMI calculated and saved successfully.");

            loadBMIHistory();
        } else {
            JOptionPane.showMessageDialog(this, result, "BMI Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBMI() {
        if (selectedBmiId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a BMI record first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this BMI record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = bmiService.deleteBMIRecord(selectedBmiId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "BMI record deleted successfully.");
                clearFields();
                loadBMIHistory();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete BMI record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadBMIHistory() {
        tableModel.setRowCount(0);

        List<Object[]> records = bmiService.getBMIHistory();

        for (Object[] row : records) {
            tableModel.addRow(row);
        }
    }

    private void clearFields() {
        selectedBmiId = -1;
        heightField.setText("");
        weightField.setText("");
        bmiValueLabel.setText("BMI Value: -");
        bmiCategoryLabel.setText("Category: -");
        bmiTable.clearSelection();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
    }
}