package ui.theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Iron Ledger Design System - "Natural Heritage" Edition
 * 
 * A premium, organic aesthetic using a palette of Light Beige, Deep Forest Green,
 * Walnut, and Warm Sand. This theme feels professional, calm, and high-end.
 */
public class UITheme {

    // ── Core Palette ──────────────────────────────────────────────
    
    public static final Color BG_MAIN        = new Color(245, 243, 239);  // #F5F3EF  Light Beige
    public static final Color BG_CARD        = new Color(255, 255, 255);  // #FFFFFF  Pure White
    public static final Color SIDEBAR_BG     = new Color(27, 38, 30);     // #1B261E  Deep Forest Green
    public static final Color SIDEBAR_HOVER  = new Color(45, 60, 48);     // #2D3C30  Dark Sage
    public static final Color BORDER_COLOR   = new Color(225, 222, 215);  // #E1DED7  Warm Cloud

    // Glassmorphism (Beige tint)
    public static final Color BG_CARD_GLASS  = new Color(255, 255, 255, 210);
    public static final Color BORDER_GLASS   = new Color(255, 255, 255, 240);

    // ── Text ──────────────────────────────────────────────────────
    
    public static final Color TEXT_PRIMARY    = new Color(45, 45, 45);     // #2D2D2D  Onyx
    public static final Color TEXT_SECONDARY  = new Color(110, 105, 95);   // #6E695F  Taupe
    public static final Color TEXT_MUTED      = new Color(160, 155, 145);  // #A09B91  Dust

    // ── Brand Colors ─────────────────────────────────────────────
    
    public static final Color PRIMARY_GREEN  = new Color(74, 93, 78);     // #4A5D4E  Sage
    public static final Color PRIMARY_HOVER  = new Color(58, 74, 62);     // #3A4A3E  Dark Sage
    public static final Color ACCENT_GOLD    = new Color(197, 160, 89);   // #C5A059  Gold/Sand
    public static final Color SUCCESS_OLIVE  = new Color(107, 142, 35);   // #6B8E23  Olive
    public static final Color DANGER_RUST    = new Color(165, 42, 42);    // #A52A2A  Rust Red
    public static final Color WARNING_SAND   = new Color(182, 158, 123);  // #B69E7B  Sand

    // Compatibility Aliases
    public static final Color PRIMARY_BLUE   = PRIMARY_GREEN;
    public static final Color SUCCESS_GREEN  = SUCCESS_OLIVE;
    public static final Color DANGER_RED     = DANGER_RUST;
    public static final Color WARNING_ORANGE = WARNING_SAND;
    public static final Color ACCENT_AMBER   = ACCENT_GOLD;

    // ── Typography ───────────────────────────────────────────────
    
    private static final String FONT_FAMILY = "Inter"; // Use Inter if available, fallback to Segoe UI

    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_MEDIUM  = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font FONT_BOLD    = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_H2      = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_H1      = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 44);

    // Icon Fonts (Segoe UI Emoji is the most complete for modern symbols/emojis on Windows)
    public static final Font FONT_ICON_SMALL  = new Font("Segoe UI Emoji", Font.PLAIN, 14);
    public static final Font FONT_ICON_MEDIUM = new Font("Segoe UI Emoji", Font.PLAIN, 18);
    public static final Font FONT_ICON_LARGE  = new Font("Segoe UI Emoji", Font.PLAIN, 24);

    // ── Button Styling ───────────────────────────────────────────

    public static void stylePrimaryButton(JButton button) {
        styleButton(button, PRIMARY_GREEN, Color.WHITE);
    }

    public static void styleSuccessButton(JButton button) {
        styleButton(button, SUCCESS_OLIVE, Color.WHITE);
    }

    public static void styleDangerButton(JButton button) {
        styleButton(button, DANGER_RUST, Color.WHITE);
    }
    
    public static void styleAccentButton(JButton button) {
        styleButton(button, ACCENT_GOLD, Color.WHITE);
    }

    public static void styleSecondaryButton(JButton button) {
        styleButton(button, Color.WHITE, TEXT_PRIMARY);
        button.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 22, 10, 22)
        ));
    }

    public static void stylePillButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setForeground(Color.WHITE);
        btn.setBackground(ACCENT_GOLD);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(184, 150, 87));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT_GOLD);
            }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), c.getHeight(), c.getHeight());
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    public static void styleBackButton(JButton btn) {
        styleSecondaryButton(btn);
        btn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        String currentText = btn.getText().toUpperCase();
        if (!currentText.contains("⬅")) {
            if (currentText.contains("BACK")) {
                btn.setText("⬅  " + currentText);
            } else {
                btn.setText("⬅  BACK");
            }
        }
    }

    public static void styleSmallIconButton(JButton button) {
        button.setBackground(ACCENT_GOLD);
        button.setForeground(Color.WHITE);
        button.setFont(FONT_ICON_SMALL.deriveFont(Font.BOLD)); // Support icons
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(ACCENT_GOLD.darker());
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(ACCENT_GOLD);
                }
            }
        });
    }

    public static void styleSidebarButton(JButton button) {
        button.setBackground(SIDEBAR_BG);
        button.setForeground(new Color(215, 210, 200)); 
        button.setFont(FONT_ICON_SMALL.deriveFont(16f)); // Use icon font for sidebar
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(14, 28, 14, 28));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SIDEBAR_HOVER);
                button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIDEBAR_BG);
                button.setForeground(new Color(215, 210, 200));
            }
        });
    }

    private static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 26, 12, 26));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(bg);
            }
        });
    }

    // ── Card Panel ───────────────────────────────────────────────

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Soft Shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(4, 4, w - 5, h - 5, 24, 24);

                // Card body
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, w - 4, h - 4, 24, 24);

                // Subtle Border
                g2.setColor(BORDER_COLOR);
                g2.drawRoundRect(0, 0, w - 4, h - 4, 24, 24);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        return panel;
    }

    // ── Input Styling ────────────────────────────────────────────

    public static void styleTextArea(JTextArea area) {
        area.setFont(FONT_REGULAR);
        area.setForeground(TEXT_PRIMARY);
        area.setBackground(Color.WHITE);
        area.setCaretColor(PRIMARY_GREEN);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 14, 10, 14));
    }

    public static void styleTextField(JTextField field) {
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(Color.WHITE);
        field.setCaretColor(PRIMARY_GREEN);
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(10, 14, 10, 14)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(PRIMARY_GREEN, 2),
                        new EmptyBorder(9, 13, 9, 13)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                        new LineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(10, 14, 10, 14)
                ));
            }
        });
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_REGULAR);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1),
                new EmptyBorder(4, 8, 4, 8)
        ));
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ── Table Styling ────────────────────────────────────────────

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setForeground(TEXT_PRIMARY);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BG_MAIN);
        table.setSelectionBackground(new Color(74, 93, 78, 30));
        table.setSelectionForeground(PRIMARY_GREEN);
        table.setBorder(null);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                if (!isSel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : BG_MAIN);
                }
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return c;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(BG_MAIN);
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 48));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    // ── Helpers ──────────────────────────────────────────────────

    public static JPanel createPageHeader(String title, JButton backButton) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 32, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_H1);
        titleLabel.setForeground(TEXT_PRIMARY);

        styleSecondaryButton(backButton);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        return headerPanel;
    }

    public static JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_ICON_MEDIUM.deriveFont(Font.BOLD, 22f)); // Support icons in headers
        label.setForeground(TEXT_PRIMARY);
        label.setBorder(new EmptyBorder(0, 0, 16, 0));
        return label;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
}
