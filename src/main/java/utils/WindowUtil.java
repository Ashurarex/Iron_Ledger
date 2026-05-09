package utils;

import javax.swing.*;
import java.awt.*;

public class WindowUtil {

    public static void setupFullScreen(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Allows the window to scale properly
        frame.setResizable(true);

        // Opens maximized according to screen resolution
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Minimum size to prevent broken layout when user resizes
        frame.setMinimumSize(new Dimension(900, 600));

        // Centers if not maximized
        frame.setLocationRelativeTo(null);
    }
}