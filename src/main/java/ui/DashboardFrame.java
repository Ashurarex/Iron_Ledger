package ui;

import model.User;
import service.DashboardService;
import service.GamificationService;
import service.GoalService;
import model.Goal;
import utils.Session;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final DashboardService dashboardService = new DashboardService();
    private final GamificationService gamificationService = new GamificationService();
    private final GoalService goalService = new GoalService();

    private JLabel totalWorkoutsValueLabel;
    private JLabel totalDurationValueLabel;
    private JLabel totalSetsValueLabel;
    private JLabel totalRepsValueLabel;
    private JLabel mostFrequentValueLabel;
    private JLabel latestWorkoutValueLabel;
    
    private JLabel streakLabel;
    private JPanel badgesPanel;
    private JPanel goalsListPanel;

    public DashboardFrame() {
        User user = Session.getCurrentUser();

        WindowUtil.setupFullScreen(this, "Iron Ledger - Dashboard");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_MAIN);

        mainPanel.add(createSidebarPanel(), BorderLayout.WEST);
        mainPanel.add(createMainContentPanel(user), BorderLayout.CENTER);

        add(mainPanel);

        loadDashboardData();
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(new EmptyBorder(0, 0, 30, 0));

        // Brand header
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(40, 32, 32, 32));

        JLabel logoLabel = new JLabel("⚒ IRON LEDGER");
        logoLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        logoLabel.setForeground(UITheme.ACCENT_GOLD);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel taglineLabel = new JLabel("EST. 2024 • PREMIUM FITNESS");
        taglineLabel.setFont(UITheme.FONT_SMALL);
        taglineLabel.setForeground(new Color(160, 155, 145));
        taglineLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        brandPanel.add(logoLabel);
        brandPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        brandPanel.add(taglineLabel);

        sidebar.add(brandPanel);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(60, 60, 60));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 16)));

        // Navigation
        sidebar.add(createNavButton("🏠  Dashboard", true, null));
        sidebar.add(createNavButton("📅  Calendar", false, e -> navigateTo(new CalendarFrame())));
        sidebar.add(createNavButton("📋  Routines", false, e -> navigateTo(new RoutineFrame())));
        sidebar.add(createNavButton("📖  Library", false, e -> navigateTo(new ExerciseLibraryFrame())));
        sidebar.add(createNavButton("💪  Workouts", false, e -> navigateTo(new WorkoutFrame())));
        sidebar.add(createNavButton("🏷️  Categories", false, e -> navigateTo(new CategoryFrame())));
        sidebar.add(createNavButton("⚖️  BMI Calculator", false, e -> navigateTo(new BMIFrame())));
        sidebar.add(createNavButton("📊  Reports", false, e -> navigateTo(new ReportFrame())));
        sidebar.add(createNavButton("📤  Export", false, e -> navigateTo(new ExportFrame())));
        
        JButton backupBtn = createNavButton("💾  Backup", false, e -> handleBackup());
        sidebar.add(backupBtn);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = createNavButton("⬅  Logout", false, e -> {
            Session.clearSession();
            navigateTo(new LoginFrame());
        });
        logoutBtn.setForeground(new Color(239, 68, 68));
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton createNavButton(String text, boolean active, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_ICON_SMALL); // Use font that supports emojis
        UITheme.styleSidebarButton(btn);
        btn.setMaximumSize(new Dimension(280, 52));
        if (active) {
            btn.setBackground(UITheme.SIDEBAR_HOVER);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, UITheme.ACCENT_GOLD),
                    new EmptyBorder(14, 24, 14, 28)
            ));
        }
        if (listener != null) btn.addActionListener(listener);
        return btn;
    }

    private void navigateTo(JFrame frame) {
        frame.setVisible(true);
        dispose();
    }

    private void handleBackup() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String result = new service.BackupService().createBackup(fileChooser.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, result.startsWith("SUCCESS") ? "Backup successful!" : result);
        }
    }

    private JPanel createMainContentPanel(User user) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.BG_MAIN);
        contentPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 40, 0));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome back, " + (user != null ? user.getName() : "Athlete"));
        welcomeLabel.setFont(UITheme.FONT_H1);
        welcomeLabel.setForeground(UITheme.TEXT_PRIMARY);
        
        JLabel subLabel = new JLabel("Precision tracking for your fitness journey.");
        subLabel.setFont(UITheme.FONT_MEDIUM);
        subLabel.setForeground(UITheme.TEXT_SECONDARY);

        textPanel.add(welcomeLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        textPanel.add(subLabel);

        JButton refreshBtn = new JButton("🔄  Sync Now");
        refreshBtn.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD, 13f));
        UITheme.stylePillButton(refreshBtn);
        refreshBtn.addActionListener(e -> loadDashboardData());

        headerPanel.add(textPanel, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        // Stats Grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 24, 24));
        statsGrid.setOpaque(false);

        totalWorkoutsValueLabel = new JLabel("0");
        totalDurationValueLabel = new JLabel("0 min");
        totalSetsValueLabel = new JLabel("0");
        totalRepsValueLabel = new JLabel("0");
        mostFrequentValueLabel = new JLabel("-");
        latestWorkoutValueLabel = new JLabel("-");

        statsGrid.add(createStatCard("Total Workouts", totalWorkoutsValueLabel, "🏋️"));
        statsGrid.add(createStatCard("Total Duration", totalDurationValueLabel, "⏱️"));
        statsGrid.add(createStatCard("Total Sets", totalSetsValueLabel, "🔢"));
        statsGrid.add(createStatCard("Total Reps", totalRepsValueLabel, "🔁"));
        statsGrid.add(createStatCard("Fave Category", mostFrequentValueLabel, "🏷️"));
        statsGrid.add(createStatCard("Latest Workout", latestWorkoutValueLabel, "💪"));

        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel bottomWrap = new JPanel(new BorderLayout(0, 32));
        bottomWrap.setOpaque(false);
        bottomWrap.add(statsGrid, BorderLayout.NORTH);
        bottomWrap.add(createGamificationPanel(), BorderLayout.CENTER);
        
        contentPanel.add(bottomWrap, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon) {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(UITheme.FONT_ICON_LARGE);
        iconLabel.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_BOLD);
        titleLabel.setForeground(UITheme.TEXT_SECONDARY);

        valueLabel.setFont(UITheme.FONT_H2);
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private void loadDashboardData() {
        Object[] summary = dashboardService.getWorkoutSummary();
        totalWorkoutsValueLabel.setText(String.valueOf(summary[0]));
        totalDurationValueLabel.setText(summary[1] + " min");
        totalSetsValueLabel.setText(String.valueOf(summary[2]));
        totalRepsValueLabel.setText(String.valueOf(summary[3]));

        mostFrequentValueLabel.setText(dashboardService.getMostFrequentCategory());
        latestWorkoutValueLabel.setText(dashboardService.getLatestWorkoutText());
        
        goalService.syncGoalsWithWorkouts();
        loadGamificationData();
    }
    
    private JPanel createGamificationPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 24, 0));
        panel.setOpaque(false);
        
        // Streak
        JPanel streakCard = UITheme.createCardPanel();
        streakCard.setLayout(new BorderLayout(0, 16));
        streakCard.add(UITheme.createSectionLabel("🔥 Activity Streak"), BorderLayout.NORTH);
        streakLabel = new JLabel("0 Days");
        streakLabel.setFont(UITheme.FONT_DISPLAY);
        streakLabel.setForeground(UITheme.PRIMARY_GREEN);
        streakLabel.setHorizontalAlignment(SwingConstants.CENTER);
        streakCard.add(streakLabel, BorderLayout.CENTER);
        
        // Badges
        JPanel badgesCard = UITheme.createCardPanel();
        badgesCard.setLayout(new BorderLayout(0, 16));
        badgesCard.add(UITheme.createSectionLabel("🏅 Milestones"), BorderLayout.NORTH);
        badgesPanel = new JPanel();
        badgesPanel.setLayout(new BoxLayout(badgesPanel, BoxLayout.Y_AXIS));
        badgesPanel.setOpaque(false);
        JScrollPane badgeScroll = new JScrollPane(badgesPanel);
        badgeScroll.setBorder(null);
        badgeScroll.setOpaque(false);
        badgeScroll.getViewport().setOpaque(false);
        badgesCard.add(badgeScroll, BorderLayout.CENTER);
        
        // Goals
        JPanel goalsCard = UITheme.createCardPanel();
        goalsCard.setLayout(new BorderLayout(0, 16));
        JPanel goalsHeader = new JPanel(new BorderLayout());
        goalsHeader.setOpaque(false);
        goalsHeader.add(UITheme.createSectionLabel("🎯 My Goals"), BorderLayout.WEST);
        JButton addGoalBtn = new JButton("Add New");
        UITheme.styleAccentButton(addGoalBtn);
        addGoalBtn.setFont(UITheme.FONT_SMALL);
        addGoalBtn.addActionListener(e -> showGoalDialog(null));
        goalsHeader.add(addGoalBtn, BorderLayout.EAST);
        
        goalsListPanel = new JPanel();
        goalsListPanel.setLayout(new BoxLayout(goalsListPanel, BoxLayout.Y_AXIS));
        goalsListPanel.setOpaque(false);
        JScrollPane goalsScroll = new JScrollPane(goalsListPanel);
        goalsScroll.setBorder(null);
        goalsScroll.setOpaque(false);
        goalsScroll.getViewport().setOpaque(false);
        
        goalsCard.add(goalsHeader, BorderLayout.NORTH);
        goalsCard.add(goalsScroll, BorderLayout.CENTER);
        
        panel.add(streakCard);
        panel.add(badgesCard);
        panel.add(goalsCard);
        
        return panel;
    }
    
    private void loadGamificationData() {
        streakLabel.setText(gamificationService.calculateCurrentStreak() + " Days");
        
        badgesPanel.removeAll();
        for (String b : gamificationService.getEarnedBadges()) {
            JLabel bl = new JLabel("⭐ " + b);
            bl.setFont(UITheme.FONT_REGULAR);
            bl.setForeground(UITheme.ACCENT_GOLD);
            bl.setBorder(new EmptyBorder(6, 0, 6, 0));
            badgesPanel.add(bl);
        }
        badgesPanel.revalidate(); badgesPanel.repaint();
        
        goalsListPanel.removeAll();
        for (Goal g : goalService.getUserGoals()) {
            JPanel gPanel = new JPanel(new BorderLayout(10, 6));
            gPanel.setOpaque(false);
            gPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
            
            String text = g.getGoalType().replace("_", " ") + ": " + (int)g.getCurrentValue() + "/" + (int)g.getTargetValue();
            if (g.getGoalType().equals("SPECIFIC_EXERCISE_WEIGHT")) text = g.getTargetName() + ": " + (int)g.getCurrentValue() + "/" + (int)g.getTargetValue() + "kg";
            
            JLabel l = new JLabel(text);
            l.setFont(UITheme.FONT_SMALL);
            l.setForeground(UITheme.TEXT_PRIMARY);
            
            JProgressBar pb = new JProgressBar(0, 100);
            pb.setValue((int) g.getProgressPercentage());
            pb.setForeground(UITheme.PRIMARY_GREEN);
            pb.setPreferredSize(new Dimension(0, 10));
            
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            actions.setOpaque(false);
            
            JButton edit = new JButton("✎");
            edit.setFont(UITheme.FONT_ICON_SMALL);
            edit.setMargin(new Insets(0,0,0,0));
            edit.setCursor(new Cursor(Cursor.HAND_CURSOR));
            edit.addActionListener(e -> showGoalDialog(g));
            
            JButton delete = new JButton("🗑");
            delete.setFont(UITheme.FONT_ICON_SMALL);
            delete.setMargin(new Insets(0,0,0,0));
            delete.setForeground(UITheme.DANGER_RUST);
            delete.setCursor(new Cursor(Cursor.HAND_CURSOR));
            delete.addActionListener(e -> {
                if (JOptionPane.showConfirmDialog(this, "Delete goal?") == JOptionPane.OK_OPTION) {
                    goalService.deleteGoal(g.getGoalId());
                    loadDashboardData();
                }
            });
            
            actions.add(edit);
            actions.add(delete);
            
            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(l, BorderLayout.WEST);
            top.add(actions, BorderLayout.EAST);
            
            gPanel.add(top, BorderLayout.NORTH);
            gPanel.add(pb, BorderLayout.CENTER);
            goalsListPanel.add(gPanel);
        }
        goalsListPanel.revalidate(); goalsListPanel.repaint();
    }
    
    private void showGoalDialog(Goal existing) {
        String[] types = {"WORKOUTS_COMPLETED", "WEIGHT_LIFTED", "SPECIFIC_EXERCISE_WEIGHT"};
        JComboBox<String> tBox = new JComboBox<>(types);
        JTextField valField = new JTextField();
        JTextField nameField = new JTextField();
        
        if (existing != null) {
            tBox.setSelectedItem(existing.getGoalType());
            valField.setText(String.valueOf(existing.getTargetValue()));
            nameField.setText(existing.getTargetName() != null ? existing.getTargetName() : "");
        }
        
        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        p.add(new JLabel("Type:")); p.add(tBox);
        p.add(new JLabel("Exercise (if spec):")); p.add(nameField);
        p.add(new JLabel("Target:")); p.add(valField);
        
        if (JOptionPane.showConfirmDialog(this, p, existing == null ? "Add Goal" : "Edit Goal", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String res;
            if (existing == null) res = goalService.createGoal((String)tBox.getSelectedItem(), valField.getText(), nameField.getText());
            else res = goalService.editGoal(existing.getGoalId(), (String)tBox.getSelectedItem(), valField.getText(), nameField.getText());
            
            if (res.equals("SUCCESS")) loadDashboardData();
            else JOptionPane.showMessageDialog(this, res);
        }
    }
}