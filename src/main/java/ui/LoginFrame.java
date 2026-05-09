package ui;

import model.User;
import service.AuthService;
import utils.Session;
import utils.WindowUtil;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private final AuthService authService = new AuthService();

    public LoginFrame() {
        WindowUtil.setupFullScreen(this, "Iron Ledger - Login");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Iron Ledger Login", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        usernameField = new JTextField(22);
        passwordField = new JPasswordField(22);

        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        stylePrimaryButton(loginButton);
        styleSecondaryButton(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        cardPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        cardPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        cardPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        cardPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        cardPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        cardPanel.add(registerButton, gbc);

        mainPanel.add(cardPanel);
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
            JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + user.getName() + "!");
            new DashboardFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(40, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(180, 45));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(180, 45));
    }
}