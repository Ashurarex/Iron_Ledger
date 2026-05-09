package ui;

import model.Category;
import service.CategoryService;
import utils.WindowUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryFrame extends JFrame {

    private JTextField categoryNameField;
    private JTextArea descriptionArea;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    private int selectedCategoryId = -1;

    private final CategoryService categoryService = new CategoryService();

    public CategoryFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Exercise Category Management");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Exercise Category Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel);

        loadCategoryTable();
    }

    private JPanel createFormPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setPreferredSize(new Dimension(420, 0));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Category Name:");
        JLabel descriptionLabel = new JLabel("Description:");

        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));

        categoryNameField = new JTextField();
        categoryNameField.setFont(new Font("Arial", Font.PLAIN, 16));
        categoryNameField.setPreferredSize(new Dimension(220, 35));
        categoryNameField.setMinimumSize(new Dimension(220, 35));

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setPreferredSize(new Dimension(220, 120));
        descriptionScroll.setMinimumSize(new Dimension(220, 120));

        addFormRow(formPanel, gbc, 0, nameLabel, categoryNameField);
        addFormRow(formPanel, gbc, 1, descriptionLabel, descriptionScroll);

        JButton addButton = new JButton("Add Category");
        JButton updateButton = new JButton("Update Category");
        JButton deleteButton = new JButton("Delete Category");
        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back");

        styleButton(addButton, new Color(40, 120, 220));
        styleButton(updateButton, new Color(40, 150, 90));
        styleButton(deleteButton, new Color(180, 40, 40));
        styleButton(clearButton, Color.DARK_GRAY);
        styleButton(backButton, Color.GRAY);

        addFullWidthButton(formPanel, gbc, 2, addButton);
        addFullWidthButton(formPanel, gbc, 3, updateButton);
        addFullWidthButton(formPanel, gbc, 4, deleteButton);
        addFullWidthButton(formPanel, gbc, 5, clearButton);
        addFullWidthButton(formPanel, gbc, 6, backButton);

        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        clearButton.addActionListener(e -> clearFields());

        backButton.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        wrapperPanel.add(formPanel);

        return wrapperPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tableModel = new DefaultTableModel(
                new Object[]{"Category ID", "Category Name", "Description"},
                0
        );

        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(30);
        categoryTable.setFont(new Font("Arial", Font.PLAIN, 15));
        categoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && categoryTable.getSelectedRow() != -1) {
                fillFieldsFromTable();
            }
        });

        return tablePanel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void addFullWidthButton(JPanel panel, GridBagConstraints gbc, int row, JButton button) {
        button.setPreferredSize(new Dimension(280, 38));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(button, gbc);

        gbc.gridwidth = 1;
    }

    private void addCategory() {
        String result = categoryService.addCategory(
                categoryNameField.getText().trim(),
                descriptionArea.getText().trim()
        );

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Category added successfully.");
            clearFields();
            loadCategoryTable();
        } else {
            JOptionPane.showMessageDialog(this, result, "Category Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        String result = categoryService.updateCategory(
                selectedCategoryId,
                categoryNameField.getText().trim(),
                descriptionArea.getText().trim()
        );

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Category updated successfully.");
            clearFields();
            loadCategoryTable();
        } else {
            JOptionPane.showMessageDialog(this, result, "Category Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        if (selectedCategoryId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a category first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Deleting a category may fail if it is used in workout records.\nAre you sure?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = categoryService.deleteCategory(selectedCategoryId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Category deleted successfully.");
                clearFields();
                loadCategoryTable();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete category. It may be used by workout records.",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void loadCategoryTable() {
        tableModel.setRowCount(0);

        List<Category> categories = categoryService.getAllCategories();

        for (Category category : categories) {
            tableModel.addRow(new Object[]{
                    category.getCategoryId(),
                    category.getCategoryName(),
                    category.getDescription()
            });
        }
    }

    private void fillFieldsFromTable() {
        int selectedRow = categoryTable.getSelectedRow();
        int modelRow = categoryTable.convertRowIndexToModel(selectedRow);

        selectedCategoryId = (int) tableModel.getValueAt(modelRow, 0);
        categoryNameField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
        descriptionArea.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
    }

    private void clearFields() {
        selectedCategoryId = -1;
        categoryNameField.setText("");
        descriptionArea.setText("");
        categoryTable.clearSelection();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
    }
}