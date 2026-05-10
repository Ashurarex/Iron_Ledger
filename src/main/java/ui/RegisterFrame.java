package ui;

import service.AuthService;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private final AuthService authService = new AuthService();

    public RegisterFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Register");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_MAIN);

        // Left brand panel (matches LoginFrame)
        JPanel brandPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.SIDEBAR_BG, 0, getHeight(), new Color(27, 38, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        brandPanel.setLayout(new GridBagLayout());
        brandPanel.setPreferredSize(new Dimension(500, 0));

        JPanel brandContent = new JPanel();
        brandContent.setLayout(new BoxLayout(brandContent, BoxLayout.Y_AXIS));
        brandContent.setOpaque(false);

        JLabel brandIcon = new JLabel("⚒");
        brandIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 80));
        brandIcon.setForeground(UITheme.ACCENT_GOLD);
        brandIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel("⚒ IRON LEDGER");
        logoLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("FORGED IN DISCIPLINE. BUILT FOR PROGRESS.");
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tagline.setForeground(new Color(160, 155, 145));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandContent.add(brandIcon);
        brandContent.add(Box.createRigidArea(new Dimension(0, 20)));
        brandContent.add(logoLabel);
        brandContent.add(Box.createRigidArea(new Dimension(0, 10)));
        brandContent.add(tagline);

        brandPanel.add(brandContent);

        // Right form panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UITheme.BG_MAIN);

        JPanel cardPanel = UITheme.createCardPanel();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(500, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 24, 8, 24);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Account", JLabel.CENTER);
        titleLabel.setFont(UITheme.FONT_H1);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Join the ledger to track your power", JLabel.CENTER);
        subtitleLabel.setFont(UITheme.FONT_REGULAR);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        nameField = new JTextField(28);
        usernameField = new JTextField(28);
        emailField = new JTextField(28);
        passwordField = new JPasswordField(28);
        confirmPasswordField = new JPasswordField(28);

        UITheme.styleTextField(nameField);
        UITheme.styleTextField(usernameField);
        UITheme.styleTextField(emailField);
        UITheme.styleTextField(passwordField);
        UITheme.styleTextField(confirmPasswordField);

        JButton registerBtn = new JButton("👤 CREATE ACCOUNT");
        UITheme.stylePrimaryButton(registerBtn);
        registerBtn.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD, 14f));
        
        JButton backBtn = new JButton("BACK TO LOGIN");
        UITheme.styleBackButton(backBtn);
        backBtn.setFont(backBtn.getFont().deriveFont(Font.BOLD, 14f));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        cardPanel.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 24, 30, 24);
        cardPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(4, 24, 4, 24);

        addRow(cardPanel, gbc, 2, "📛 Full Name", nameField);
        addRow(cardPanel, gbc, 3, "👤 Username", usernameField);
        addRow(cardPanel, gbc, 4, "✉️ Email Address", emailField);
        addRow(cardPanel, gbc, 5, "🔑 Password", passwordField);
        addRow(cardPanel, gbc, 6, "🔁 Confirm Pass", confirmPasswordField);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 24, 12, 24);
        cardPanel.add(registerBtn, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(4, 24, 16, 24);
        cardPanel.add(backBtn, gbc);

        rightPanel.add(cardPanel);

        mainPanel.add(brandPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);

        registerBtn.addActionListener(e -> registerUser());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = UITheme.createFieldLabel(labelText);
        label.setFont(UITheme.FONT_ICON_SMALL);
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        String result = authService.register(name, username, password, confirmPassword, email);

        if (result.equals("SUCCESS")) {
            JOptionPane.showMessageDialog(this, "Registration successful.");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}