package ui;

import service.ExerciseLibraryService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ExerciseLibraryFrame extends JFrame {

    private final ExerciseLibraryService libraryService = new ExerciseLibraryService();

    public ExerciseLibraryFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Exercise Library");

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
        mainPanel.add(UITheme.createPageHeader("Reference Library", backBtn), BorderLayout.NORTH);

        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getViewport().setBackground(UITheme.BG_MAIN);

        Map<String, List<String>> library = libraryService.getExerciseLibrary();

        for (Map.Entry<String, List<String>> entry : library.entrySet()) {
            contentPanel.add(createMuscleGroupCard(entry.getKey(), entry.getValue()));
            contentPanel.add(Box.createRigidArea(new Dimension(0, 32)));
        }

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createMuscleGroupCard(String muscleGroup, List<String> exercises) {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(0, 20));

        JLabel title = new JLabel("💪 " + muscleGroup);
        title.setFont(UITheme.FONT_ICON_MEDIUM); // Supports emoji
        title.setForeground(UITheme.PRIMARY_GREEN);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(new JSeparator(), BorderLayout.CENTER);
        
        card.add(headerPanel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new GridLayout(0, 2, 30, 16));
        listPanel.setOpaque(false);

        int count = 1;
        for (String ex : exercises) {
            JLabel exLabel = new JLabel("• " + ex);
            exLabel.setFont(UITheme.FONT_REGULAR);
            exLabel.setForeground(UITheme.TEXT_PRIMARY);
            listPanel.add(exLabel);
            count++;
        }

        card.add(listPanel, BorderLayout.CENTER);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        
        return card;
    }
}
