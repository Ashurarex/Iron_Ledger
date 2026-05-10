package ui;

import service.ReportService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportFrame extends JFrame {

    private final ReportService reportService = new ReportService();
    private JPanel chartContainer;
    private JLabel chartTitleLabel;
    private JLabel summaryLabel;
    private JButton currentActiveBtn;

    public ReportFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Reports");

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
        mainPanel.add(UITheme.createPageHeader("Analytics & Performance", backBtn), BorderLayout.NORTH);

        // Sidebar Actions for Reports
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(320, 0));
        leftPanel.setBorder(new EmptyBorder(0, 0, 0, 40));

        leftPanel.add(UITheme.createSectionLabel("📊  Performance Views"));
        
        JButton weeklyVolumeBtn = createReportButton("📈  Weekly Volume Progress");
        JButton monthlyVolumeBtn = createReportButton("🗓️  Monthly Volume Growth");
        JButton weightBtn = createReportButton("⚖️  Weight Progress (Daily)");
        JButton durationBtn = createReportButton("⏱️  Workout Duration Mix");
        JButton categoryBtn = createReportButton("🏷️  Category Distribution");

        leftPanel.add(weeklyVolumeBtn); leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        leftPanel.add(monthlyVolumeBtn); leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        leftPanel.add(weightBtn); leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        leftPanel.add(durationBtn); leftPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        leftPanel.add(categoryBtn);

        weeklyVolumeBtn.addActionListener(e -> { setActiveReport(weeklyVolumeBtn); showWeeklyReport(); });
        monthlyVolumeBtn.addActionListener(e -> { setActiveReport(monthlyVolumeBtn); showMonthlyReport(); });
        weightBtn.addActionListener(e -> { setActiveReport(weightBtn); showWeightReport(); });
        durationBtn.addActionListener(e -> { setActiveReport(durationBtn); showDurationReport(); });
        categoryBtn.addActionListener(e -> { setActiveReport(categoryBtn); showVolumeReport(); });

        setActiveReport(weeklyVolumeBtn);

        // Chart Area
        JPanel centerPanel = UITheme.createCardPanel();
        centerPanel.setLayout(new BorderLayout(0, 24));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        chartTitleLabel = new JLabel("Select a report", JLabel.LEFT);
        chartTitleLabel.setFont(UITheme.FONT_H2);
        chartTitleLabel.setForeground(UITheme.TEXT_PRIMARY);
        
        summaryLabel = new JLabel("Hover over bars to see details");
        summaryLabel.setFont(UITheme.FONT_SMALL);
        summaryLabel.setForeground(UITheme.TEXT_SECONDARY);
        
        titlePanel.add(chartTitleLabel, BorderLayout.NORTH);
        titlePanel.add(summaryLabel, BorderLayout.SOUTH);
        
        centerPanel.add(titlePanel, BorderLayout.NORTH);

        chartContainer = new JPanel(new BorderLayout());
        chartContainer.setOpaque(false);
        centerPanel.add(chartContainer, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        
        // Default View
        showWeeklyReport();
    }

    private JButton createReportButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(320, 44));
        btn.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD, 13f));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        UITheme.stylePillButton(btn);
        btn.setBackground(UITheme.BG_CARD);
        btn.setForeground(UITheme.TEXT_SECONDARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setActiveReport(JButton btn) {
        if (currentActiveBtn != null) {
            currentActiveBtn.setBackground(UITheme.BG_CARD);
            currentActiveBtn.setForeground(UITheme.TEXT_SECONDARY);
        }
        currentActiveBtn = btn;
        currentActiveBtn.setBackground(UITheme.ACCENT_GOLD);
        currentActiveBtn.setForeground(Color.WHITE);
    }

    private void showWeeklyReport() {
        chartTitleLabel.setText("📈 Weekly Volume Progress");
        summaryLabel.setText("Tracking total lifted weight (sets * reps * weight) per week.");
        List<Object[]> dataList = reportService.getWeeklyVolume();
        displayChart(convertToMap(dataList), "Volume (kg)", UITheme.PRIMARY_GREEN, UITheme.ACCENT_GOLD);
    }

    private void showMonthlyReport() {
        chartTitleLabel.setText("🗓️ Monthly Volume Growth");
        summaryLabel.setText("Review your training volume progression over the last 6 months.");
        List<Object[]> dataList = reportService.getMonthlyVolume();
        displayChart(convertToMap(dataList), "Volume (kg)", UITheme.ACCENT_GOLD, UITheme.WARNING_SAND);
    }

    private void showWeightReport() {
        chartTitleLabel.setText("⚖️ Daily Weight Progress");
        summaryLabel.setText("Total weight lifted across all exercises each day.");
        List<Object[]> dataList = reportService.getWeightProgressByDate();
        displayChart(convertToMap(dataList), "Weight (kg)", UITheme.PRIMARY_GREEN, UITheme.SUCCESS_OLIVE);
    }

    private void showDurationReport() {
        chartTitleLabel.setText("⏱️ Workout Duration Mix");
        summaryLabel.setText("Total time spent in gym per session.");
        List<Object[]> dataList = reportService.getWorkoutDurationByDate();
        displayChart(convertToMap(dataList), "Duration (min)", UITheme.SUCCESS_OLIVE, UITheme.PRIMARY_GREEN);
    }

    private void showVolumeReport() {
        chartTitleLabel.setText("🏷️ Volume Distribution by Category");
        summaryLabel.setText("Comparison of work volume across different muscle groups.");
        List<Object[]> dataList = reportService.getVolumeByCategory();
        displayChart(convertToMap(dataList), "Volume (kg)", UITheme.WARNING_SAND, UITheme.ACCENT_GOLD);
    }

    private Map<String, Double> convertToMap(List<Object[]> dataList) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Object[] row : dataList) {
            String label = String.valueOf(row[0]);
            double value = Double.parseDouble(String.valueOf(row[1]));
            map.put(label, value);
        }
        return map;
    }

    private void displayChart(Map<String, Double> data, String yAxisLabel, Color startColor, Color endColor) {
        chartContainer.removeAll();
        if (data.isEmpty()) {
            JLabel empty = new JLabel("No data available to plot yet. Log more workouts!", JLabel.CENTER);
            empty.setFont(UITheme.FONT_MEDIUM);
            empty.setForeground(UITheme.TEXT_MUTED);
            chartContainer.add(empty);
        } else {
            chartContainer.add(new BarChartPanel(data, yAxisLabel, startColor, endColor));
        }
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private class BarChartPanel extends JPanel {
        private final Map<String, Double> data;
        private final String yAxisLabel;
        private final Color startColor;
        private final Color endColor;
        private String tooltipText = "";
        private Point tooltipPos = null;

        public BarChartPanel(Map<String, Double> data, String yAxisLabel, Color startColor, Color endColor) {
            this.data = data;
            this.yAxisLabel = yAxisLabel;
            this.startColor = startColor;
            this.endColor = endColor;
            setOpaque(false);

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    updateTooltip(e.getPoint());
                }
            });
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited(MouseEvent e) {
                    tooltipText = "";
                    repaint();
                }
            });
        }

        private void updateTooltip(Point p) {
            int padding = 60;
            int labelHeight = 40;
            int width = getWidth() - 2 * padding;
            int height = getHeight() - 2 * padding - labelHeight;
            if (data.isEmpty() || width <= 0) return;
            int barWidth = width / data.size();
            double max = data.values().stream().max(Double::compare).orElse(1.0);

            int x = padding;
            boolean found = false;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() / max) * height);
                Rectangle rect = new Rectangle(x + 10, padding + height - barHeight, barWidth - 20, barHeight);
                if (rect.contains(p)) {
                    tooltipText = String.format("%s: %.1f", entry.getKey(), entry.getValue());
                    tooltipPos = p;
                    found = true;
                    break;
                }
                x += barWidth;
            }
            if (!found) tooltipText = "";
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int padding = 60;
            int labelHeight = 40;
            int width = getWidth() - 2 * padding;
            int height = getHeight() - 2 * padding - labelHeight;
            if (data.isEmpty() || width <= 0) {
                g2.dispose();
                return;
            }
            double max = data.values().stream().max(Double::compare).orElse(1.0);

            // Draw Background Lines (Horizontal)
            g2.setColor(new Color(235, 232, 225));
            g2.setStroke(new BasicStroke(1));
            for (int i = 0; i <= 5; i++) {
                int y = padding + height - (i * height / 5);
                g2.drawLine(padding, y, padding + width, y);
            }

            // Draw Axes
            g2.setColor(UITheme.BORDER_COLOR);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(padding, padding, padding, padding + height); // Y
            g2.drawLine(padding, padding + height, padding + width, padding + height); // X

            // Y-Axis Labels
            g2.setFont(UITheme.FONT_SMALL);
            g2.setColor(UITheme.TEXT_SECONDARY);
            for (int i = 0; i <= 5; i++) {
                int y = padding + height - (i * height / 5);
                String val = String.format("%.0f", (i * max / 5));
                g2.drawString(val, padding - 50, y + 5);
            }

            // Draw Bars
            int barWidth = width / data.size();
            int x = padding;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int barHeight = (int) ((entry.getValue() / max) * height);
                if (barHeight < 2) barHeight = 2; // Min height for visibility
                
                // Rounded bar with gradient
                GradientPaint barGradient = new GradientPaint(
                        x + 10, padding + height - barHeight, startColor,
                        x + 10, padding + height, endColor
                );
                g2.setPaint(barGradient);
                g2.fillRoundRect(x + 10, padding + height - barHeight, barWidth - 20, barHeight, 12, 12);

                // Highlight border if hovered
                if (!tooltipText.isEmpty() && tooltipText.startsWith(entry.getKey())) {
                    g2.setColor(UITheme.TEXT_PRIMARY);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(x + 10, padding + height - barHeight, barWidth - 20, barHeight, 12, 12);
                }

                // X Labels
                g2.setColor(UITheme.TEXT_SECONDARY);
                g2.setFont(UITheme.FONT_SMALL);
                String label = entry.getKey();
                if (label.length() > 10) label = label.substring(0, 8) + "..";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, x + (barWidth - fm.stringWidth(label)) / 2, padding + height + 28);

                x += barWidth;
            }

            // Draw Tooltip
            if (!tooltipText.isEmpty() && tooltipPos != null) {
                g2.setFont(UITheme.FONT_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(tooltipText) + 24;
                int th = 36;
                int tx = Math.min(getWidth() - tw - 10, Math.max(10, tooltipPos.x - tw / 2));
                int ty = tooltipPos.y - th - 15;

                g2.setColor(new Color(45, 45, 45, 240));
                g2.fillRoundRect(tx, ty, tw, th, 12, 12);
                g2.setColor(Color.WHITE);
                g2.drawString(tooltipText, tx + 12, ty + 24);
            }

            g2.dispose();
        }
    }
}