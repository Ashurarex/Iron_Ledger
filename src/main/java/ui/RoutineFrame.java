package ui;

import model.Category;
import model.Routine;
import model.RoutineExercise;
import service.CategoryService;
import service.RoutineService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoutineFrame extends JFrame {

    private final RoutineService routineService = new RoutineService();
    private final CategoryService categoryService = new CategoryService();

    private JList<Routine> routineList;
    private DefaultListModel<Routine> listModel;
    private JTable exerciseTable;
    private DefaultTableModel tableModel;

    private JTextField routineNameField;
    private JComboBox<Category> categoryComboBox;
    private JTextField exerciseNameField;
    private JTextField setsField;
    private JTextField repsField;

    private List<RoutineExercise> currentExercises = new ArrayList<>();

    public RoutineFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Routine Builder");

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
        mainPanel.add(UITheme.createPageHeader("Routine Strategy", backBtn), BorderLayout.NORTH);

        // Content - Split Layout
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Routines List (Left)
        gbc.gridx = 0;
        gbc.weightx = 0.28;
        gbc.insets = new Insets(0, 0, 0, 32);
        contentPanel.add(createRoutineListPanel(), gbc);

        // Builder (Right)
        gbc.gridx = 1;
        gbc.weightx = 0.72;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(createBuilderPanel(), gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        loadCategories();
        loadRoutines();
    }

    private JPanel createRoutineListPanel() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(0, 20));

        card.add(UITheme.createSectionLabel("📖  Saved Blueprints"), BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        routineList = new JList<>(listModel);
        routineList.setFont(UITheme.FONT_MEDIUM);
        routineList.setSelectionBackground(new Color(230, 225, 215));
        routineList.setSelectionForeground(UITheme.TEXT_PRIMARY);
        routineList.setFixedCellHeight(44);
        routineList.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        routineList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setFont(UITheme.FONT_ICON_SMALL.deriveFont(16f)); // Support icons
                if (value instanceof Routine) {
                    l.setText("📜  " + ((Routine) value).getRoutineName());
                }
                l.setBorder(new EmptyBorder(4, 8, 4, 8));
                return l;
            }
        });

        routineList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedRoutine();
            }
        });

        JScrollPane scroll = new JScrollPane(routineList);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));
        card.add(scroll, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("🗑️ Delete Routine");
        UITheme.styleSecondaryButton(deleteBtn);
        deleteBtn.setFont(UITheme.FONT_ICON_SMALL);
        deleteBtn.setForeground(UITheme.DANGER_RUST);
        deleteBtn.addActionListener(e -> deleteRoutine());
        card.add(deleteBtn, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createBuilderPanel() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(0, 24));

        // Top: Routine Name
        JPanel namePanel = new JPanel(new BorderLayout(0, 8));
        namePanel.setOpaque(false);
        namePanel.add(UITheme.createFieldLabel("Blueprint Name (e.g. Push Day A)"), BorderLayout.NORTH);
        routineNameField = new JTextField();
        UITheme.styleTextField(routineNameField);
        namePanel.add(routineNameField, BorderLayout.CENTER);

        // Middle: Add Exercise Inputs
        JPanel addExercisePanel = new JPanel(new GridBagLayout());
        addExercisePanel.setOpaque(false);
        addExercisePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR), " Add Component ", 0, 0, UITheme.FONT_BOLD, UITheme.TEXT_SECONDARY),
                new EmptyBorder(16, 16, 16, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; addExercisePanel.add(UITheme.createFieldLabel("Exercise"), gbc);
        gbc.gridx = 1; addExercisePanel.add(UITheme.createFieldLabel("Category"), gbc);
        gbc.gridx = 2; addExercisePanel.add(UITheme.createFieldLabel("Target Sets"), gbc);
        gbc.gridx = 3; addExercisePanel.add(UITheme.createFieldLabel("Target Reps"), gbc);

        gbc.gridy = 1;
        gbc.gridx = 0; exerciseNameField = new JTextField(); UITheme.styleTextField(exerciseNameField); addExercisePanel.add(exerciseNameField, gbc);
        gbc.gridx = 1; categoryComboBox = new JComboBox<>(); UITheme.styleComboBox(categoryComboBox); addExercisePanel.add(categoryComboBox, gbc);
        gbc.gridx = 2; setsField = new JTextField(); UITheme.styleTextField(setsField); addExercisePanel.add(setsField, gbc);
        gbc.gridx = 3; repsField = new JTextField(); UITheme.styleTextField(repsField); addExercisePanel.add(repsField, gbc);
        
        gbc.gridx = 4;
        JButton addExBtn = new JButton("➕ ADD");
        UITheme.stylePrimaryButton(addExBtn);
        addExBtn.setFont(UITheme.FONT_ICON_SMALL);
        addExBtn.addActionListener(e -> addExerciseToCurrent());
        addExercisePanel.add(addExBtn, gbc);

        // Table
        String[] columns = {"Exercise", "Category", "Sets", "Reps"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        exerciseTable = new JTable(tableModel);
        UITheme.styleTable(exerciseTable);
        JScrollPane tableScroll = new JScrollPane(exerciseTable);
        tableScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER_COLOR));

        // Bottom Actions
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        bottomRow.setOpaque(false);
        
        JButton clearBtn = new JButton("🧹 Reset Builder");
        JButton saveBtn = new JButton("💾 SAVE BLUEPRINT");
        UITheme.styleSecondaryButton(clearBtn);
        clearBtn.setFont(UITheme.FONT_ICON_SMALL);
        UITheme.stylePrimaryButton(saveBtn);
        saveBtn.setFont(UITheme.FONT_ICON_SMALL);
        
        clearBtn.addActionListener(e -> clearForm());
        saveBtn.addActionListener(e -> saveRoutine());
        
        bottomRow.add(clearBtn);
        bottomRow.add(saveBtn);

        // Assemble Right
        JPanel rightCenter = new JPanel(new BorderLayout(0, 24));
        rightCenter.setOpaque(false);
        rightCenter.add(addExercisePanel, BorderLayout.NORTH);
        rightCenter.add(tableScroll, BorderLayout.CENTER);

        card.add(namePanel, BorderLayout.NORTH);
        card.add(rightCenter, BorderLayout.CENTER);
        card.add(bottomRow, BorderLayout.SOUTH);

        return card;
    }

    private void loadCategories() {
        categoryComboBox.removeAllItems();
        for (Category c : categoryService.getAllCategories()) {
            categoryComboBox.addItem(c);
        }
    }

    private void loadRoutines() {
        listModel.clear();
        for (Routine r : routineService.getUserRoutines()) {
            listModel.addElement(r);
        }
    }

    private void addExerciseToCurrent() {
        String name = exerciseNameField.getText().trim();
        Category cat = (Category) categoryComboBox.getSelectedItem();
        String setsText = setsField.getText().trim();
        String repsText = repsField.getText().trim();

        if (!name.isEmpty() && cat != null && !setsText.isEmpty() && !repsText.isEmpty()) {
            try {
                int sets = Integer.parseInt(setsText);
                int reps = Integer.parseInt(repsText);
                RoutineExercise ex = new RoutineExercise(0, 0, cat.getCategoryId(), name, sets, reps);
                ex.setCategoryName(cat.getCategoryName());
                currentExercises.add(ex);
                tableModel.addRow(new Object[]{name, cat.getCategoryName(), sets, reps});
                exerciseNameField.setText("");
                setsField.setText("");
                repsField.setText("");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Numeric input required for sets/reps.");
            }
        }
    }

    private void saveRoutine() {
        String name = routineNameField.getText().trim();
        if (name.isEmpty() || currentExercises.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Routine must have a name and at least one exercise component.");
            return;
        }
        String result = routineService.createRoutine(name, currentExercises);
        if (result.equals("SUCCESS")) {
            clearForm();
            loadRoutines();
        }
    }

    private void loadSelectedRoutine() {
        Routine selected = routineList.getSelectedValue();
        if (selected != null) {
            clearForm();
            routineNameField.setText(selected.getRoutineName());
            currentExercises = selected.getExercises();
            for (RoutineExercise re : currentExercises) {
                tableModel.addRow(new Object[]{re.getExerciseName(), re.getCategoryName(), re.getDefaultSets(), re.getDefaultReps()});
            }
        }
    }

    private void deleteRoutine() {
        Routine selected = routineList.getSelectedValue();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Discard this blueprint?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                routineService.deleteRoutine(selected.getRoutineId());
                loadRoutines();
                clearForm();
            }
        }
    }

    private void clearForm() {
        routineNameField.setText("");
        categoryComboBox.setSelectedIndex(-1);
        exerciseNameField.setText("");
        setsField.setText("");
        repsField.setText("");
        currentExercises = new ArrayList<>();
        tableModel.setRowCount(0);
    }
}
