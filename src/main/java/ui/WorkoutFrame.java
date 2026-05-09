package ui;

import model.Category;
import service.CategoryService;
import service.WorkoutService;
import utils.WindowUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WorkoutFrame extends JFrame {

    private JTextField exerciseField;
    private JTextField setsField;
    private JTextField repsField;
    private JTextField weightField;
    private JTextField durationField;
    private JTextField dateField;

    private JTextField searchExerciseField;
    private JTextField fromDateField;
    private JTextField toDateField;

    private JComboBox<String> categoryBox;
    private JComboBox<String> filterCategoryBox;

    private JTable workoutTable;
    private DefaultTableModel tableModel;

    private int selectedWorkoutId = -1;

    private final WorkoutService workoutService = new WorkoutService();
    private final CategoryService categoryService = new CategoryService();

    private final Map<String, Integer> categoryMap = new LinkedHashMap<>();

    public WorkoutFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Workout Management");

        loadCategoriesFromDatabase();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Workout Management", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.add(createSearchPanel(), BorderLayout.NORTH);
        rightPanel.add(createTablePanel(), BorderLayout.CENTER);

        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);

        loadWorkoutTable();
    }

    private void loadCategoriesFromDatabase() {
        categoryMap.clear();

        List<Category> categories = categoryService.getAllCategories();

        for (Category category : categories) {
            categoryMap.put(category.getCategoryName(), category.getCategoryId());
        }
    }

    private JPanel createFormPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setPreferredSize(new Dimension(430, 0));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 8, 9, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        categoryBox = new JComboBox<>();

        for (String categoryName : categoryMap.keySet()) {
            categoryBox.addItem(categoryName);
        }

        exerciseField = createTextField();
        setsField = createTextField();
        repsField = createTextField();
        weightField = createTextField();
        durationField = createTextField();
        dateField = createTextField();
        dateField.setText(LocalDate.now().toString());

        categoryBox.setPreferredSize(new Dimension(220, 35));
        categoryBox.setMinimumSize(new Dimension(220, 35));
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 15));

        addFormRow(formPanel, gbc, 0, "Category:", categoryBox);
        addFormRow(formPanel, gbc, 1, "Exercise:", exerciseField);
        addFormRow(formPanel, gbc, 2, "Sets:", setsField);
        addFormRow(formPanel, gbc, 3, "Reps:", repsField);
        addFormRow(formPanel, gbc, 4, "Weight:", weightField);
        addFormRow(formPanel, gbc, 5, "Duration:", durationField);
        addFormRow(formPanel, gbc, 6, "Date YYYY-MM-DD:", dateField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back");

        styleButton(addButton, new Color(40, 120, 220));
        styleButton(updateButton, new Color(40, 150, 90));
        styleButton(deleteButton, new Color(180, 40, 40));
        styleButton(clearButton, Color.DARK_GRAY);
        styleButton(backButton, Color.GRAY);

        addButtonRow(formPanel, gbc, 7, addButton, updateButton);
        addButtonRow(formPanel, gbc, 8, deleteButton, clearButton);
        addFullWidthButton(formPanel, gbc, 9, backButton);

        addButton.addActionListener(e -> addWorkout());
        updateButton.addActionListener(e -> updateWorkout());
        deleteButton.addActionListener(e -> deleteWorkout());
        clearButton.addActionListener(e -> clearFields());

        backButton.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        wrapperPanel.add(formPanel);

        return wrapperPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(245, 245, 245));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel searchTitle = new JLabel("Search and Filter Workout Records");
        searchTitle.setFont(new Font("Arial", Font.BOLD, 22));

        searchExerciseField = createSearchTextField();
        fromDateField = createSearchTextField();
        toDateField = createSearchTextField();

        fromDateField.setToolTipText("YYYY-MM-DD");
        toDateField.setToolTipText("YYYY-MM-DD");

        filterCategoryBox = new JComboBox<>();
        filterCategoryBox.addItem("All Categories");

        for (String categoryName : categoryMap.keySet()) {
            filterCategoryBox.addItem(categoryName);
        }

        filterCategoryBox.setFont(new Font("Arial", Font.PLAIN, 15));
        filterCategoryBox.setPreferredSize(new Dimension(180, 35));

        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");

        styleButton(searchButton, new Color(40, 120, 220));
        styleButton(resetButton, Color.DARK_GRAY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 8;
        gbc.weightx = 1;
        searchPanel.add(searchTitle, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0;

        addSearchLabel(searchPanel, gbc, 0, 1, "Exercise:");
        addSearchField(searchPanel, gbc, 1, 1, searchExerciseField);

        addSearchLabel(searchPanel, gbc, 2, 1, "Category:");
        addSearchField(searchPanel, gbc, 3, 1, filterCategoryBox);

        addSearchLabel(searchPanel, gbc, 4, 1, "From:");
        addSearchField(searchPanel, gbc, 5, 1, fromDateField);

        addSearchLabel(searchPanel, gbc, 0, 2, "To:");
        addSearchField(searchPanel, gbc, 1, 2, toDateField);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchButton.setPreferredSize(new Dimension(130, 38));
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 3;
        resetButton.setPreferredSize(new Dimension(130, 38));
        searchPanel.add(resetButton, gbc);

        searchButton.addActionListener(e -> searchWorkouts());
        resetButton.addActionListener(e -> resetSearch());

        return searchPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        tableModel = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Category",
                        "Exercise",
                        "Sets",
                        "Reps",
                        "Weight",
                        "Duration",
                        "Date",
                        "Category ID"
                },
                0
        );

        workoutTable = new JTable(tableModel);
        workoutTable.setRowHeight(28);
        workoutTable.setFont(new Font("Arial", Font.PLAIN, 14));
        workoutTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        workoutTable.removeColumn(workoutTable.getColumnModel().getColumn(8));

        JScrollPane scrollPane = new JScrollPane(workoutTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        workoutTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && workoutTable.getSelectedRow() != -1) {
                fillFieldsFromTable();
            }
        });

        return tablePanel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(220, 35));
        field.setMinimumSize(new Dimension(220, 35));
        return field;
    }

    private JTextField createSearchTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(180, 35));
        field.setMinimumSize(new Dimension(160, 35));
        return field;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 15));

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

    private void addButtonRow(JPanel panel, GridBagConstraints gbc, int row, JButton leftButton, JButton rightButton) {
        leftButton.setPreferredSize(new Dimension(130, 38));
        rightButton.setPreferredSize(new Dimension(130, 38));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(leftButton, gbc);

        gbc.gridx = 1;
        panel.add(rightButton, gbc);
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

    private void addSearchLabel(JPanel panel, GridBagConstraints gbc, int x, int y, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);
    }

    private void addSearchField(JPanel panel, GridBagConstraints gbc, int x, int y, JComponent component) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(component, gbc);
    }

    private void addWorkout() {
        if (categoryBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No category found. Please add a category first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String category = (String) categoryBox.getSelectedItem();
        int categoryId = categoryMap.get(category);

        String result = workoutService.addWorkout(
                categoryId,
                exerciseField.getText().trim(),
                setsField.getText().trim(),
                repsField.getText().trim(),
                weightField.getText().trim(),
                durationField.getText().trim(),
                dateField.getText().trim()
        );

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Workout added successfully.");
            clearFields();
            loadWorkoutTable();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateWorkout() {
        if (selectedWorkoutId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a workout first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (categoryBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "No category found. Please add a category first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String category = (String) categoryBox.getSelectedItem();
        int categoryId = categoryMap.get(category);

        String result = workoutService.updateWorkout(
                selectedWorkoutId,
                categoryId,
                exerciseField.getText().trim(),
                setsField.getText().trim(),
                repsField.getText().trim(),
                weightField.getText().trim(),
                durationField.getText().trim(),
                dateField.getText().trim()
        );

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Workout updated successfully.");
            clearFields();
            loadWorkoutTable();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWorkout() {
        if (selectedWorkoutId <= 0) {
            JOptionPane.showMessageDialog(this, "Select a workout first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this workout?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = workoutService.deleteWorkout(selectedWorkoutId);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Workout deleted successfully.");
                clearFields();
                loadWorkoutTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete workout.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchWorkouts() {
        int categoryId = 0;

        String selectedFilterCategory = (String) filterCategoryBox.getSelectedItem();

        if (selectedFilterCategory != null && !selectedFilterCategory.equals("All Categories")) {
            categoryId = categoryMap.get(selectedFilterCategory);
        }

        List<Object[]> workouts = workoutService.searchWorkouts(
                searchExerciseField.getText().trim(),
                categoryId,
                fromDateField.getText().trim(),
                toDateField.getText().trim()
        );

        tableModel.setRowCount(0);

        for (Object[] row : workouts) {
            tableModel.addRow(row);
        }
    }

    private void resetSearch() {
        searchExerciseField.setText("");
        fromDateField.setText("");
        toDateField.setText("");

        if (filterCategoryBox.getItemCount() > 0) {
            filterCategoryBox.setSelectedIndex(0);
        }

        loadWorkoutTable();
    }

    private void loadWorkoutTable() {
        tableModel.setRowCount(0);

        List<Object[]> workouts = workoutService.getUserWorkouts();

        for (Object[] row : workouts) {
            tableModel.addRow(row);
        }
    }

    private void fillFieldsFromTable() {
        int selectedRow = workoutTable.getSelectedRow();
        int modelRow = workoutTable.convertRowIndexToModel(selectedRow);

        selectedWorkoutId = (int) tableModel.getValueAt(modelRow, 0);

        String categoryName = String.valueOf(tableModel.getValueAt(modelRow, 1));

        categoryBox.setSelectedItem(categoryName);
        exerciseField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
        setsField.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
        repsField.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));
        weightField.setText(String.valueOf(tableModel.getValueAt(modelRow, 5)));
        durationField.setText(String.valueOf(tableModel.getValueAt(modelRow, 6)));
        dateField.setText(String.valueOf(tableModel.getValueAt(modelRow, 7)));
    }

    private void clearFields() {
        selectedWorkoutId = -1;

        if (categoryBox.getItemCount() > 0) {
            categoryBox.setSelectedIndex(0);
        }

        exerciseField.setText("");
        setsField.setText("");
        repsField.setText("");
        weightField.setText("");
        durationField.setText("");
        dateField.setText(LocalDate.now().toString());

        workoutTable.clearSelection();
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
}