package ui;

import model.Workout;
import model.Category;
import service.WorkoutService;
import service.CategoryService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class WorkoutFrame extends JFrame {

    private final WorkoutService workoutService = new WorkoutService();
    private final CategoryService categoryService = new CategoryService();

    private JTable workoutTable;
    private DefaultTableModel tableModel;

    private JTextField exerciseField;
    private JTextField weightField;
    private JTextField repsField;
    private JTextField setsField;
    private JTextField durationField;
    private JTextField dateField;
    private JComboBox<Category> categoryComboBox;
    private JTextField searchField;

    private int selectedWorkoutId = -1;

    public WorkoutFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Workouts");

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
        mainPanel.add(UITheme.createPageHeader("Training Logs", backBtn), BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel(new BorderLayout(0, 32));
        contentPanel.setOpaque(false);

        contentPanel.add(createFormPanel(), BorderLayout.NORTH);
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        loadCategories();
        loadWorkouts();
    }

    private JPanel createFormPanel() {
        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formCard.add(UITheme.createSectionLabel("📝  Log Exercise Details"), gbc);

        // Labels and Fields
        gbc.gridwidth = 1; gbc.weightx = 0.5;

        // Row 1
        gbc.gridy = 1; gbc.gridx = 0;
        formCard.add(UITheme.createFieldLabel("Exercise Name"), gbc);
        gbc.gridx = 1;
        exerciseField = new JTextField();
        UITheme.styleTextField(exerciseField);
        formCard.add(exerciseField, gbc);

        gbc.gridx = 2;
        formCard.add(UITheme.createFieldLabel("Category"), gbc);
        gbc.gridx = 3;
        categoryComboBox = new JComboBox<>();
        UITheme.styleComboBox(categoryComboBox);
        formCard.add(categoryComboBox, gbc);

        // Row 2
        gbc.gridy = 2; gbc.gridx = 0;
        formCard.add(UITheme.createFieldLabel("Weight (kg)"), gbc);
        gbc.gridx = 1;
        weightField = new JTextField();
        UITheme.styleTextField(weightField);
        formCard.add(weightField, gbc);

        gbc.gridx = 2;
        formCard.add(UITheme.createFieldLabel("Reps count"), gbc);
        gbc.gridx = 3;
        repsField = new JTextField();
        UITheme.styleTextField(repsField);
        formCard.add(repsField, gbc);

        // Row 3
        gbc.gridy = 3; gbc.gridx = 0;
        formCard.add(UITheme.createFieldLabel("Sets count"), gbc);
        gbc.gridx = 1;
        setsField = new JTextField();
        UITheme.styleTextField(setsField);
        formCard.add(setsField, gbc);

        gbc.gridx = 2;
        formCard.add(UITheme.createFieldLabel("Duration (min)"), gbc);
        gbc.gridx = 3;
        durationField = new JTextField();
        UITheme.styleTextField(durationField);
        formCard.add(durationField, gbc);
        
        // Row 4
        gbc.gridy = 4; gbc.gridx = 0;
        formCard.add(UITheme.createFieldLabel("Date (YYYY-MM-DD)"), gbc);
        gbc.gridx = 1;
        dateField = new JTextField(java.time.LocalDate.now().toString());
        UITheme.styleTextField(dateField);
        formCard.add(dateField, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        btnPanel.setOpaque(false);

        JButton saveBtn = new JButton("➕ Save Workout");
        JButton updateBtn = new JButton("💾 Update");
        JButton deleteBtn = new JButton("🗑️ Delete");
        JButton clearBtn = new JButton("🧹 Clear");

        UITheme.stylePrimaryButton(saveBtn);
        saveBtn.setFont(UITheme.FONT_ICON_SMALL);
        UITheme.styleAccentButton(updateBtn);
        updateBtn.setFont(UITheme.FONT_ICON_SMALL);
        UITheme.styleSecondaryButton(deleteBtn);
        deleteBtn.setFont(UITheme.FONT_ICON_SMALL);
        deleteBtn.setForeground(UITheme.DANGER_RUST);
        UITheme.styleSecondaryButton(clearBtn);
        clearBtn.setFont(UITheme.FONT_ICON_SMALL);

        btnPanel.add(clearBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(saveBtn);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4;
        gbc.insets = new Insets(24, 20, 12, 20);
        formCard.add(btnPanel, gbc);

        saveBtn.addActionListener(e -> saveWorkout());
        updateBtn.addActionListener(e -> updateWorkout());
        deleteBtn.addActionListener(e -> deleteWorkout());
        clearBtn.addActionListener(e -> clearForm());

        return formCard;
    }

    private JPanel createTablePanel() {
        JPanel tableCard = UITheme.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(16, 0));
        searchPanel.setOpaque(false);
        
        searchField = new JTextField();
        UITheme.styleTextField(searchField);
        JButton searchBtn = new JButton("🔍 Search Records");
        UITheme.stylePrimaryButton(searchBtn);
        searchBtn.setFont(UITheme.FONT_ICON_SMALL);
        searchBtn.addActionListener(e -> searchWorkouts());

        searchPanel.add(UITheme.createFieldLabel("Filter by Exercise:"), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        tableCard.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Exercise", "Category", "Weight", "Reps", "Sets", "Duration", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        workoutTable = new JTable(tableModel);
        UITheme.styleTable(workoutTable);
        
        workoutTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedWorkout();
            }
        });

        JScrollPane scrollPane = new JScrollPane(workoutTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private void loadCategories() {
        List<Category> categories = categoryService.getAllCategories();
        categoryComboBox.removeAllItems();
        for (Category cat : categories) {
            categoryComboBox.addItem(cat);
        }
    }

    private void loadWorkouts() {
        List<Object[]> workouts = workoutService.getUserWorkouts();
        updateTable(workouts);
    }

    private void searchWorkouts() {
        String keyword = searchField.getText().trim();
        List<Object[]> workouts = workoutService.searchWorkouts(keyword, -1, "", "");
        updateTable(workouts);
    }

    private void updateTable(List<Object[]> workouts) {
        tableModel.setRowCount(0);
        for (Object[] w : workouts) {
            tableModel.addRow(w);
        }
    }

    private void saveWorkout() {
        String name = exerciseField.getText().trim();
        Category cat = (Category) categoryComboBox.getSelectedItem();
        String weight = weightField.getText().trim();
        String reps = repsField.getText().trim();
        String sets = setsField.getText().trim();
        String duration = durationField.getText().trim();
        String date = dateField.getText().trim();

        String result = workoutService.addWorkout(cat != null ? cat.getCategoryId() : -1, name, sets, reps, weight, duration, date);
        if (result.equals("SUCCESS")) {
            clearForm();
            loadWorkouts();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedWorkout() {
        int row = workoutTable.getSelectedRow();
        if (row != -1) {
            selectedWorkoutId = (int) tableModel.getValueAt(row, 0);
            exerciseField.setText((String) tableModel.getValueAt(row, 1));
            weightField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            repsField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            setsField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
            durationField.setText(String.valueOf(tableModel.getValueAt(row, 6)));
            dateField.setText(String.valueOf(tableModel.getValueAt(row, 7)));
            
            String catName = (String) tableModel.getValueAt(row, 2);
            for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                if (categoryComboBox.getItemAt(i).getCategoryName().equals(catName)) {
                    categoryComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void updateWorkout() {
        if (selectedWorkoutId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a workout first.");
            return;
        }

        String name = exerciseField.getText().trim();
        Category cat = (Category) categoryComboBox.getSelectedItem();
        String weight = weightField.getText().trim();
        String reps = repsField.getText().trim();
        String sets = setsField.getText().trim();
        String duration = durationField.getText().trim();
        String date = dateField.getText().trim();

        String result = workoutService.updateWorkout(selectedWorkoutId, cat != null ? cat.getCategoryId() : -1, name, sets, reps, weight, duration, date);
        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Entry updated.");
            clearForm();
            loadWorkouts();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteWorkout() {
        if (selectedWorkoutId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a workout first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this workout entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            workoutService.deleteWorkout(selectedWorkoutId);
            clearForm();
            loadWorkouts();
        }
    }

    private void clearForm() {
        exerciseField.setText("");
        weightField.setText("");
        repsField.setText("");
        setsField.setText("");
        durationField.setText("");
        categoryComboBox.setSelectedIndex(-1);
        selectedWorkoutId = -1;
        workoutTable.clearSelection();
    }
}