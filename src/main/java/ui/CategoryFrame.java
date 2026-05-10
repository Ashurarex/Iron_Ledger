package ui;

import model.Category;
import service.CategoryService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryFrame extends JFrame {

    private final CategoryService categoryService = new CategoryService();

    private JTable categoryTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextArea descriptionArea;
    private int selectedCategoryId = -1;

    public CategoryFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Categories");

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
        mainPanel.add(UITheme.createPageHeader("Training Categories", backBtn), BorderLayout.NORTH);

        // Content - Two columns
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Form Column (Left)
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 0, 0, 32);
        contentPanel.add(createFormPanel(), gbc);

        // Table Column (Right)
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(createTablePanel(), gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        loadCategories();
    }

    private JPanel createFormPanel() {
        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 16, 12, 16);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(UITheme.createSectionLabel("🏷️  Category Details"), gbc);

        gbc.gridy = 1;
        formCard.add(UITheme.createFieldLabel("Category Name"), gbc);
        gbc.gridy = 2;
        nameField = new JTextField();
        UITheme.styleTextField(nameField);
        formCard.add(nameField, gbc);

        gbc.gridy = 3;
        formCard.add(UITheme.createFieldLabel("Notes / Description"), gbc);
        gbc.gridy = 4;
        gbc.weighty = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(6, 20);
        UITheme.styleTextArea(descriptionArea);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        formCard.add(descScroll, gbc);

        // Buttons
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel btnStack = new JPanel(new GridLayout(4, 1, 0, 12));
        btnStack.setOpaque(false);
        btnStack.setBorder(new EmptyBorder(24, 0, 0, 0));

        JButton saveBtn = new JButton("➕ Create Category");
        JButton updateBtn = new JButton("💾 Update Selected");
        JButton deleteBtn = new JButton("🗑️ Remove Category");
        JButton clearBtn = new JButton("🧹 Clear Form");

        UITheme.stylePrimaryButton(saveBtn);
        saveBtn.setFont(UITheme.FONT_ICON_SMALL);
        UITheme.styleAccentButton(updateBtn);
        updateBtn.setFont(UITheme.FONT_ICON_SMALL);
        UITheme.styleSecondaryButton(deleteBtn);
        deleteBtn.setFont(UITheme.FONT_ICON_SMALL);
        deleteBtn.setForeground(UITheme.DANGER_RUST);
        UITheme.styleSecondaryButton(clearBtn);
        clearBtn.setFont(UITheme.FONT_ICON_SMALL);

        btnStack.add(saveBtn);
        btnStack.add(updateBtn);
        btnStack.add(deleteBtn);
        btnStack.add(clearBtn);

        gbc.gridy = 5;
        formCard.add(btnStack, gbc);

        saveBtn.addActionListener(e -> saveCategory());
        updateBtn.addActionListener(e -> updateCategory());
        deleteBtn.addActionListener(e -> deleteCategory());
        clearBtn.addActionListener(e -> clearForm());

        return formCard;
    }

    private JPanel createTablePanel() {
        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 16));
        
        JLabel title = UITheme.createSectionLabel("📋 Existing Muscle Groups / Categories");
        tableCard.add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        categoryTable = new JTable(tableModel);
        UITheme.styleTable(categoryTable);

        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCategory();
            }
        });

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        tableModel.setRowCount(0);
        for (Category cat : categories) {
            tableModel.addRow(new Object[]{cat.getCategoryId(), cat.getCategoryName(), cat.getDescription()});
        }
    }

    private void saveCategory() {
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        String result = categoryService.addCategory(name, desc);
        if (result.equals("SUCCESS")) {
            clearForm();
            loadCategories();
        } else {
            JOptionPane.showMessageDialog(this, "Name is required.", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadSelectedCategory() {
        int row = categoryTable.getSelectedRow();
        if (row != -1) {
            selectedCategoryId = (int) tableModel.getValueAt(row, 0);
            nameField.setText((String) tableModel.getValueAt(row, 1));
            descriptionArea.setText((String) tableModel.getValueAt(row, 2));
        }
    }

    private void updateCategory() {
        if (selectedCategoryId == -1) {
            JOptionPane.showMessageDialog(this, "Select a category first.");
            return;
        }
        String name = nameField.getText().trim();
        String desc = descriptionArea.getText().trim();
        String result = categoryService.updateCategory(selectedCategoryId, name, desc);
        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Category updated.");
            clearForm();
            loadCategories();
        }
    }

    private void deleteCategory() {
        if (selectedCategoryId == -1) {
            JOptionPane.showMessageDialog(this, "Select a category first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this category?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            categoryService.deleteCategory(selectedCategoryId);
            clearForm();
            loadCategories();
        }
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        selectedCategoryId = -1;
        categoryTable.clearSelection();
    }
}