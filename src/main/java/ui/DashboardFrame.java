package ui;

import model.User;
import service.DashboardService;
import utils.Session;
import utils.WindowUtil;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final DashboardService dashboardService = new DashboardService();

    private JLabel totalWorkoutsValueLabel;
    private JLabel totalDurationValueLabel;
    private JLabel totalWeightValueLabel;
    private JLabel latestWorkoutValueLabel;
    private JLabel latestBMIValueLabel;

    public DashboardFrame() {
        User user = Session.getCurrentUser();

        WindowUtil.setupFullScreen(this, "Iron Ledger - Dashboard");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        mainPanel.add(createHeaderPanel(user), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        add(mainPanel);

        loadDashboardData();
    }

    private JPanel createHeaderPanel(User user) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 45, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel titleLabel = new JLabel("Iron Ledger Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(Color.WHITE);

        String welcomeText = "Welcome";
        if (user != null) {
            welcomeText = "Welcome, " + user.getName();
        }

        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomeLabel.setForeground(new Color(220, 220, 220));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setBackground(new Color(30, 45, 70));
        textPanel.add(titleLabel);
        textPanel.add(welcomeLabel);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(130, 42));
        styleHeaderButton(refreshButton);

        refreshButton.addActionListener(e -> {
            loadDashboardData();
            JOptionPane.showMessageDialog(this, "Dashboard refreshed.");
        });

        headerPanel.add(textPanel, BorderLayout.WEST);
        headerPanel.add(refreshButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 35, 40));

        JPanel summaryPanel = createSummaryPanel();
        JPanel navigationPanel = createNavigationPanel();

        centerPanel.add(summaryPanel, BorderLayout.NORTH);
        centerPanel.add(navigationPanel, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        summaryPanel.setBackground(new Color(245, 245, 245));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        totalWorkoutsValueLabel = new JLabel("0");
        totalDurationValueLabel = new JLabel("0 min");
        totalWeightValueLabel = new JLabel("0 kg");
        latestWorkoutValueLabel = new JLabel("No workout recorded yet");
        latestBMIValueLabel = new JLabel("No BMI record yet");

        summaryPanel.add(createSummaryCard("Total Workouts", totalWorkoutsValueLabel));
        summaryPanel.add(createSummaryCard("Total Duration", totalDurationValueLabel));
        summaryPanel.add(createSummaryCard("Total Weight", totalWeightValueLabel));
        summaryPanel.add(createSummaryCard("Latest Workout", latestWorkoutValueLabel));
        summaryPanel.add(createSummaryCard("Latest BMI", latestBMIValueLabel));
        summaryPanel.add(createInfoCard());

        return summaryPanel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(80, 80, 80));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(new Color(40, 120, 220));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createInfoCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JLabel titleLabel = new JLabel("System Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(80, 80, 80));

        JLabel valueLabel = new JLabel("Ready");
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setForeground(new Color(40, 150, 90));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createNavigationPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(245, 245, 245));

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 25, 25));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton workoutButton = createNavigationButton("Workout Management");
        JButton categoryButton = createNavigationButton("Exercise Categories");
        JButton bmiButton = createNavigationButton("BMI Calculator");
        JButton reportButton = createNavigationButton("Progress Reports");
        JButton exportButton = createNavigationButton("Export Data");
        JButton logoutButton = createLogoutButton("Logout");

        buttonPanel.add(workoutButton);
        buttonPanel.add(categoryButton);
        buttonPanel.add(bmiButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(logoutButton);

        wrapperPanel.add(buttonPanel);

        workoutButton.addActionListener(e -> {
            new WorkoutFrame().setVisible(true);
            dispose();
        });

        categoryButton.addActionListener(e -> {
            new CategoryFrame().setVisible(true);
            dispose();
        });

        bmiButton.addActionListener(e -> {
            new BMIFrame().setVisible(true);
            dispose();
        });

        reportButton.addActionListener(e -> {
            new ReportFrame().setVisible(true);
            dispose();
        });

        exportButton.addActionListener(e -> {
            new ExportFrame().setVisible(true);
            dispose();
        });

        logoutButton.addActionListener(e -> {
            Session.clearSession();
            new LoginFrame().setVisible(true);
            dispose();
        });

        return wrapperPanel;
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(260, 90));
        button.setBackground(new Color(40, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }

    private JButton createLogoutButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(260, 90));
        button.setBackground(new Color(180, 40, 40));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        return button;
    }

    private void styleHeaderButton(JButton button) {
        button.setBackground(new Color(40, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
    }

    private void loadDashboardData() {
        Object[] summary = dashboardService.getWorkoutSummary();

        totalWorkoutsValueLabel.setText(String.valueOf(summary[0]));
        totalDurationValueLabel.setText(summary[1] + " min");
        totalWeightValueLabel.setText(summary[2] + " kg");

        latestWorkoutValueLabel.setText(dashboardService.getLatestWorkoutText());
        latestBMIValueLabel.setText(dashboardService.getLatestBMIText());
    }
}