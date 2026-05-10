package ui;

import model.User;
import service.AuthService;
import utils.Session;
import utils.WindowUtil;
import ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private final AuthService authService = new AuthService();

    public LoginFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Login");

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG_MAIN);

        // Left brand panel
        JPanel brandPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Heritage Gradient
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
        cardPanel.setPreferredSize(new Dimension(460, 420));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 24, 12, 24);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Member Login", JLabel.CENTER);
        titleLabel.setFont(UITheme.FONT_H1);
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel("Enter your credentials to enter the ledger", JLabel.CENTER);
        subtitleLabel.setFont(UITheme.FONT_REGULAR);
        subtitleLabel.setForeground(UITheme.TEXT_SECONDARY);

        JLabel usernameLabel = UITheme.createFieldLabel("👤 Username");
        usernameLabel.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD));
        JLabel passwordLabel = UITheme.createFieldLabel("🔑 Password");
        passwordLabel.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD));

        usernameField = new JTextField(28);
        passwordField = new JPasswordField(28);
        UITheme.styleTextField(usernameField);
        UITheme.styleTextField(passwordField);

        JButton loginButton = new JButton("🔓  SIGN IN");
        UITheme.stylePrimaryButton(loginButton);
        loginButton.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD, 14f));
        
        JButton registerButton = new JButton("➕  CREATE ACCOUNT");
        UITheme.styleSecondaryButton(registerButton);
        registerButton.setFont(UITheme.FONT_ICON_SMALL.deriveFont(Font.BOLD, 14f));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        cardPanel.add(titleLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 24, 30, 24);
        cardPanel.add(subtitleLabel, gbc);

        gbc.insets = new Insets(4, 24, 4, 24);
        gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.weightx = 0.1; // Reduced weight for labels to prevent column drifting
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        cardPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.NONE;
        cardPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        cardPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(32, 24, 12, 24);
        cardPanel.add(loginButton, gbc);

        gbc.gridy = 5; gbc.insets = new Insets(4, 24, 16, 24);
        cardPanel.add(registerButton, gbc);

        rightPanel.add(cardPanel);

        mainPanel.add(brandPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);

        loginButton.addActionListener(e -> loginUser());
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        User user = authService.login(username, password);

        if (user != null) {
            Session.setCurrentUser(user);
            new DashboardFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Access Denied", JOptionPane.ERROR_MESSAGE);
        }
    }
}