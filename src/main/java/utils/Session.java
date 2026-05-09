package utils;

import model.User;

public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getUserId();
        }
        return -1;
    }

    public static void clearSession() {
        currentUser = null;
    }
}