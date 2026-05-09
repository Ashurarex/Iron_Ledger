package service;

import dao.UserDAO;
import model.User;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public String register(String name, String username, String password, String confirmPassword, String email) {
        if (name.isBlank() || username.isBlank() || password.isBlank()
                || confirmPassword.isBlank() || email.isBlank()) {
            return "All fields are required.";
        }

        if (!email.contains("@") || !email.contains(".")) {
            return "Enter a valid email address.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        if (password.length() < 4) {
            return "Password must be at least 4 characters.";
        }

        if (userDAO.usernameExists(username)) {
            return "Username already exists.";
        }

        if (userDAO.emailExists(email)) {
            return "Email already exists.";
        }

        User user = new User(name, username, password, email);

        boolean success = userDAO.registerUser(user);

        if (success) {
            return "SUCCESS";
        } else {
            return "Registration failed.";
        }
    }

    public User login(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            return null;
        }

        return userDAO.loginUser(username, password);
    }
}