package ui;

import service.WorkoutService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarFrame extends JFrame {

    private final WorkoutService workoutService = new WorkoutService();
    private JPanel calendarGrid;
    private JLabel monthLabel;
    private YearMonth currentMonth;

    public CalendarFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Training Calendar");
        currentMonth = YearMonth.now();

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
        
        JPanel headerWrap = new JPanel(new BorderLayout());
        headerWrap.setOpaque(false);
        headerWrap.add(UITheme.createPageHeader("Training History", backBtn), BorderLayout.NORTH);

        // Month Selector
        JPanel monthSelector = new JPanel(new FlowLayout(FlowLayout.CENTER, 48, 0));
        monthSelector.setOpaque(false);
        monthSelector.setBorder(new EmptyBorder(10, 0, 32, 0));

        JButton prevBtn = new JButton("◀");
        JButton nextBtn = new JButton("▶");
        UITheme.styleSecondaryButton(prevBtn);
        prevBtn.setFont(UITheme.FONT_ICON_MEDIUM);
        UITheme.styleSecondaryButton(nextBtn);
        nextBtn.setFont(UITheme.FONT_ICON_MEDIUM);
        prevBtn.setPreferredSize(new Dimension(50, 40));
        nextBtn.setPreferredSize(new Dimension(50, 40));
        
        monthLabel = new JLabel(currentMonth.getMonth().name() + " " + currentMonth.getYear());
        monthLabel.setFont(UITheme.FONT_H2);
        monthLabel.setForeground(UITheme.TEXT_PRIMARY);

        prevBtn.addActionListener(e -> changeMonth(-1));
        nextBtn.addActionListener(e -> changeMonth(1));

        monthSelector.add(prevBtn);
        monthSelector.add(monthLabel);
        monthSelector.add(nextBtn);
        headerWrap.add(monthSelector, BorderLayout.SOUTH);

        mainPanel.add(headerWrap, BorderLayout.NORTH);

        // Calendar Grid Container
        JPanel gridCard = UITheme.createCardPanel();
        gridCard.setLayout(new BorderLayout(0, 20));
        gridCard.setBorder(new EmptyBorder(32, 32, 32, 32));
        
        // Days of week header
        JPanel daysOfWeek = new JPanel(new GridLayout(1, 7));
        daysOfWeek.setOpaque(false);
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (String d : days) {
            JLabel lbl = new JLabel(d, JLabel.CENTER);
            lbl.setFont(UITheme.FONT_BOLD);
            lbl.setForeground(UITheme.TEXT_SECONDARY);
            daysOfWeek.add(lbl);
        }
        gridCard.add(daysOfWeek, BorderLayout.NORTH);

        calendarGrid = new JPanel(new GridLayout(0, 7, 12, 12));
        calendarGrid.setOpaque(false);
        gridCard.add(calendarGrid, BorderLayout.CENTER);

        mainPanel.add(gridCard, BorderLayout.CENTER);
        add(mainPanel);

        renderCalendar();
    }

    private void changeMonth(int delta) {
        currentMonth = currentMonth.plusMonths(delta);
        monthLabel.setText(currentMonth.getMonth().name() + " " + currentMonth.getYear());
        renderCalendar();
    }

    private void renderCalendar() {
        calendarGrid.removeAll();
        java.util.List<java.sql.Date> sqlDates = workoutService.getWorkoutDatesForMonth(currentMonth);
        java.util.Set<java.time.LocalDate> workoutDates = new java.util.HashSet<>();
        for (java.sql.Date d : sqlDates) {
            workoutDates.add(d.toLocalDate());
        }

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7; // Sunday=0
        int daysInMonth = currentMonth.lengthOfMonth();

        // Empty cells before start
        for (int i = 0; i < dayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        // Day cells
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            boolean hasWorkout = workoutDates.contains(date);
            calendarGrid.add(createDayCell(day, hasWorkout, date.equals(LocalDate.now())));
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private JPanel createDayCell(int day, boolean hasWorkout, boolean isToday) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(true);
        cell.setBackground(Color.WHITE);
        cell.setBorder(BorderFactory.createLineBorder(new Color(230, 225, 215), 1));
        
        JPanel inner = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isToday) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(197, 160, 89, 40)); // Gold highlight
                    g2.fillRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 16, 16);
                    g2.setColor(UITheme.ACCENT_GOLD);
                    g2.drawRoundRect(6, 6, getWidth() - 12, getHeight() - 12, 16, 16);
                    g2.dispose();
                }
            }
        };
        inner.setOpaque(false);
        inner.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel numLabel = new JLabel(String.valueOf(day));
        numLabel.setFont(isToday ? UITheme.FONT_BOLD : UITheme.FONT_REGULAR);
        numLabel.setForeground(isToday ? UITheme.TEXT_PRIMARY : UITheme.TEXT_SECONDARY);
        inner.add(numLabel, BorderLayout.NORTH);

        if (hasWorkout) {
            JPanel dotPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(UITheme.PRIMARY_GREEN); // Sage green dot
                    g2.fillOval(getWidth() / 2 - 5, 0, 10, 10);
                    g2.dispose();
                }
            };
            dotPanel.setOpaque(false);
            dotPanel.setPreferredSize(new Dimension(0, 14));
            inner.add(dotPanel, BorderLayout.SOUTH);
        }

        cell.add(inner, BorderLayout.CENTER);

        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cell.setBackground(new Color(245, 243, 239));
                cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBackground(Color.WHITE);
            }
        });

        return cell;
    }
}
