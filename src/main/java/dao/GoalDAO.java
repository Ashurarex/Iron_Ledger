package dao;

import db.DBConnection;
import model.Goal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GoalDAO {

    public GoalDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS goals (
                    goal_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    goal_type VARCHAR(50) NOT NULL,
                    target_value DOUBLE NOT NULL,
                    current_value DOUBLE NOT NULL DEFAULT 0.0,
                    target_name VARCHAR(255),
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
                """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            
            if (connection != null) {
                statement.execute(createTableSQL);
                
                // Add column to existing table if upgrading from older version
                try {
                    statement.execute("ALTER TABLE goals ADD COLUMN target_name VARCHAR(255)");
                } catch (SQLException ignored) {
                    // Column already exists, safe to ignore
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to initialize Goals table: " + e.getMessage());
        }
    }

    public boolean createGoal(Goal goal) {
        String sql = "INSERT INTO goals (user_id, goal_type, target_value, current_value, target_name) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, goal.getUserId());
            statement.setString(2, goal.getGoalType());
            statement.setDouble(3, goal.getTargetValue());
            statement.setDouble(4, goal.getCurrentValue());
            statement.setString(5, goal.getTargetName());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Create goal failed: " + e.getMessage());
            return false;
        }
    }

    public List<Goal> getGoalsByUser(int userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT * FROM goals WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                goals.add(new Goal(
                        resultSet.getInt("goal_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("goal_type"),
                        resultSet.getDouble("target_value"),
                        resultSet.getDouble("current_value"),
                        resultSet.getString("target_name")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Fetch goals failed: " + e.getMessage());
        }

        return goals;
    }

    public boolean updateGoalCurrentValue(int goalId, double newValue) {
        String sql = "UPDATE goals SET current_value = ? WHERE goal_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, newValue);
            statement.setInt(2, goalId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update goal value failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteGoal(int goalId, int userId) {
        String sql = "DELETE FROM goals WHERE goal_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, goalId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete goal failed: " + e.getMessage());
            return false;
        }
    }
    public boolean updateGoal(int goalId, int userId, String goalType, double targetValue, String targetName) {
        String sql = "UPDATE goals SET goal_type = ?, target_value = ?, target_name = ? WHERE goal_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, goalType);
            statement.setDouble(2, targetValue);
            statement.setString(3, targetName);
            statement.setInt(4, goalId);
            statement.setInt(5, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update goal failed: " + e.getMessage());
            return false;
        }
    }
}
