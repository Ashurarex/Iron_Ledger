package ui;

import service.AuthService;
import utils.WindowUtil;

import javax.swing.*;
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

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(35, 50, 35, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Iron Ledger Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel nameLabel = new JLabel("Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");

        JLabel[] labels = {nameLabel, usernameLabel, emailLabel, passwordLabel, confirmPasswordLabel};

        for (JLabel label : labels) {
            label.setFont(new Font("Arial", Font.PLAIN, 18));
        }

        nameField = new JTextField(22);
        usernameField = new JTextField(22);
        emailField = new JTextField(22);
        passwordField = new JPasswordField(22);
        confirmPasswordField = new JPasswordField(22);

        JTextField[] fields = {nameField, usernameField, emailField, passwordField, confirmPasswordField};

        for (JTextField field : fields) {
            field.setFont(new Font("Arial", Font.PLAIN, 18));
        }

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        stylePrimaryButton(registerButton);
        styleSecondaryButton(backButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cardPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        addRow(cardPanel, gbc, 1, nameLabel, nameField);
        addRow(cardPanel, gbc, 2, usernameLabel, usernameField);
        addRow(cardPanel, gbc, 3, emailLabel, emailField);
        addRow(cardPanel, gbc, 4, passwordLabel, passwordField);
        addRow(cardPanel, gbc, 5, confirmPasswordLabel, confirmPasswordField);

        gbc.gridx = 0;
        gbc.gridy = 6;
        cardPanel.add(registerButton, gbc);

        gbc.gridx = 1;
        cardPanel.add(backButton, gbc);

        mainPanel.add(cardPanel);
        add(mainPanel);

        registerButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
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
            JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, result, "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(new Color(40, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 45));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 45));
    }
}