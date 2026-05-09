package ui;

import service.ReportService;
import utils.WindowUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportFrame extends JFrame {

    private final ReportService reportService = new ReportService();

    private JLabel totalWorkoutsLabel;
    private JLabel totalDurationLabel;
    private JLabel totalWeightLabel;
    private JLabel totalSetsLabel;
    private JLabel totalRepsLabel;

    private JTable reportTable;
    private DefaultTableModel tableModel;

    private ChartPanel chartPanel;

    public ReportFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Progress Reports");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Progress Charts and Reports", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel topPanel = createSummaryPanel();
        mainPanel.add(topPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 245, 245));

        JPanel controlPanel = createControlPanel();
        chartPanel = new ChartPanel();

        centerPanel.add(controlPanel, BorderLayout.NORTH);
        centerPanel.add(chartPanel, BorderLayout.CENTER);
        centerPanel.add(createReportTablePanel(), BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        loadSummary();
        loadCategoryReport();
    }

    private JPanel createSummaryPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.setPreferredSize(new Dimension(360, 0));
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));

        JPanel summaryPanel = new JPanel(new GridLayout(6, 1, 10, 15));
        summaryPanel.setBackground(Color.WHITE);

        JLabel summaryTitle = new JLabel("Workout Summary", JLabel.CENTER);
        summaryTitle.setFont(new Font("Arial", Font.BOLD, 24));

        totalWorkoutsLabel = createSummaryLabel("Total Workouts: 0");
        totalDurationLabel = createSummaryLabel("Total Duration: 0 min");
        totalWeightLabel = createSummaryLabel("Total Weight: 0 kg");
        totalSetsLabel = createSummaryLabel("Total Sets: 0");
        totalRepsLabel = createSummaryLabel("Total Reps: 0");

        summaryPanel.add(summaryTitle);
        summaryPanel.add(totalWorkoutsLabel);
        summaryPanel.add(totalDurationLabel);
        summaryPanel.add(totalWeightLabel);
        summaryPanel.add(totalSetsLabel);
        summaryPanel.add(totalRepsLabel);

        wrapperPanel.add(summaryPanel);

        return wrapperPanel;
    }

    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        return label;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        controlPanel.setBackground(new Color(245, 245, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton categoryReportButton = new JButton("Category Report");
        JButton durationReportButton = new JButton("Duration Progress");
        JButton weightReportButton = new JButton("Weight Progress");
        JButton refreshButton = new JButton("Refresh Summary");
        JButton backButton = new JButton("Back");

        styleButton(categoryReportButton, new Color(40, 120, 220));
        styleButton(durationReportButton, new Color(40, 150, 90));
        styleButton(weightReportButton, new Color(150, 90, 200));
        styleButton(refreshButton, Color.DARK_GRAY);
        styleButton(backButton, Color.GRAY);

        controlPanel.add(categoryReportButton);
        controlPanel.add(durationReportButton);
        controlPanel.add(weightReportButton);
        controlPanel.add(refreshButton);
        controlPanel.add(backButton);

        categoryReportButton.addActionListener(e -> loadCategoryReport());
        durationReportButton.addActionListener(e -> loadDurationReport());
        weightReportButton.addActionListener(e -> loadWeightReport());

        refreshButton.addActionListener(e -> {
            loadSummary();
            JOptionPane.showMessageDialog(this, "Summary refreshed.");
        });

        backButton.addActionListener(e -> {
            new DashboardFrame().setVisible(true);
            dispose();
        });

        return controlPanel;
    }

    private JPanel createReportTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(245, 245, 245));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        tablePanel.setPreferredSize(new Dimension(0, 230));

        tableModel = new DefaultTableModel(new Object[]{"Label", "Value"}, 0);

        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(28);
        reportTable.setFont(new Font("Arial", Font.PLAIN, 14));
        reportTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(reportTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void loadSummary() {
        Object[] summary = reportService.getWorkoutSummary();

        totalWorkoutsLabel.setText("Total Workouts: " + summary[0]);
        totalDurationLabel.setText("Total Duration: " + summary[1] + " min");
        totalWeightLabel.setText("Total Weight: " + summary[2] + " kg");
        totalSetsLabel.setText("Total Sets: " + summary[3]);
        totalRepsLabel.setText("Total Reps: " + summary[4]);
    }

    private void loadCategoryReport() {
        List<Object[]> data = reportService.getWorkoutCountByCategory();

        tableModel.setColumnIdentifiers(new Object[]{"Category", "Workout Count"});
        loadTableData(data);

        chartPanel.setChartData("Category-wise Workout Count", data);
    }

    private void loadDurationReport() {
        List<Object[]> data = reportService.getWorkoutDurationByDate();

        tableModel.setColumnIdentifiers(new Object[]{"Date", "Total Duration"});
        loadTableData(data);

        chartPanel.setChartData("Date-wise Workout Duration", data);
    }

    private void loadWeightReport() {
        List<Object[]> data = reportService.getWeightProgressByDate();

        tableModel.setColumnIdentifiers(new Object[]{"Date", "Total Weight"});
        loadTableData(data);

        chartPanel.setChartData("Date-wise Weight Progress", data);
    }

    private void loadTableData(List<Object[]> data) {
        tableModel.setRowCount(0);

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(170, 38));
    }

    private static class ChartPanel extends JPanel {

        private String chartTitle = "Chart";
        private List<Object[]> chartData;

        public ChartPanel() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        public void setChartData(String chartTitle, List<Object[]> chartData) {
            this.chartTitle = chartTitle;
            this.chartData = chartData;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g2 = (Graphics2D) graphics;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, width, height);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.drawString(chartTitle, 30, 35);

            if (chartData == null || chartData.isEmpty()) {
                g2.setFont(new Font("Arial", Font.PLAIN, 18));
                g2.drawString("No data available for this report.", 30, 90);
                return;
            }

            int leftPadding = 70;
            int bottomPadding = 70;
            int topPadding = 70;
            int rightPadding = 40;

            int chartWidth = width - leftPadding - rightPadding;
            int chartHeight = height - topPadding - bottomPadding;

            g2.setColor(Color.BLACK);
            g2.drawLine(leftPadding, topPadding, leftPadding, topPadding + chartHeight);
            g2.drawLine(leftPadding, topPadding + chartHeight, leftPadding + chartWidth, topPadding + chartHeight);

            double maxValue = getMaxValue(chartData);

            if (maxValue <= 0) {
                maxValue = 1;
            }

            int barCount = chartData.size();
            int availableWidth = chartWidth - 30;
            int barWidth = Math.max(35, availableWidth / Math.max(barCount * 2, 1));
            int gap = Math.max(25, barWidth);

            int x = leftPadding + 25;

            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            for (Object[] row : chartData) {
                String label = String.valueOf(row[0]);
                double value = Double.parseDouble(String.valueOf(row[1]));

                int barHeight = (int) ((value / maxValue) * (chartHeight - 30));
                int y = topPadding + chartHeight - barHeight;

                g2.setColor(new Color(40, 120, 220));
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, barWidth, barHeight);

                g2.drawString(String.valueOf(value), x, y - 5);

                String shortLabel = label.length() > 10 ? label.substring(0, 10) + "..." : label;
                g2.drawString(shortLabel, x - 5, topPadding + chartHeight + 20);

                x += barWidth + gap;
            }
        }

        private double getMaxValue(List<Object[]> data) {
            double max = 0;

            for (Object[] row : data) {
                double value = Double.parseDouble(String.valueOf(row[1]));

                if (value > max) {
                    max = value;
                }
            }

            return max;
        }
    }
}