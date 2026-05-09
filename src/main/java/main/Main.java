package main;

import model.User;
import ui.DashboardFrame;
import utils.Session;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // TEMPORARY AUTH BYPASS
            // Delete this after app development is complete.

            User devUser = new User(
                    1,
                    "Developer User",
                    "dev",
                    "dev123",
                    "dev@example.com"
            );

            Session.setCurrentUser(devUser);

            DashboardFrame dashboardFrame = new DashboardFrame();
            dashboardFrame.setVisible(true);
        });
    }
}