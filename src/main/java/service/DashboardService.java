package service;

import db.DBConnection;
import utils.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardService {

    public Object[] getWorkoutSummary() {
        String sql = """
                SELECT 
                    COUNT(*) AS total_workouts,
                    COALESCE(SUM(duration), 0) AS total_duration,
                    COALESCE(SUM(sets_count), 0) AS total_sets,
                    COALESCE(SUM(reps), 0) AS total_reps
                FROM workouts
                WHERE user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Session.getCurrentUserId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Object[]{
                        resultSet.getInt("total_workouts"),
                        resultSet.getInt("total_duration"),
                        resultSet.getInt("total_sets"),
                        resultSet.getInt("total_reps")
                };
            }

        } catch (SQLException e) {
            System.out.println("Dashboard workout summary failed: " + e.getMessage());
        }

        return new Object[]{0, 0, 0, 0};
    }

    public String getMostFrequentCategory() {
        String sql = """
                SELECT c.category_name, COUNT(*) as count
                FROM workouts w
                JOIN exercise_categories c ON w.category_id = c.category_id
                WHERE w.user_id = ?
                GROUP BY c.category_id
                ORDER BY count DESC
                LIMIT 1
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Session.getCurrentUserId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("category_name");
            }
        } catch (SQLException e) {
            System.out.println("Most frequent category fetch failed: " + e.getMessage());
        }
        return "None";
    }

    public String getLatestWorkoutText() {
        String sql = """
                SELECT w.exercise_name, c.category_name
                FROM workouts w
                LEFT JOIN exercise_categories c ON w.category_id = c.category_id
                WHERE w.user_id = ?
                ORDER BY w.workout_date DESC, w.workout_id DESC
                LIMIT 1
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Session.getCurrentUserId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String exerciseName = resultSet.getString("exercise_name");
                String categoryName = resultSet.getString("category_name");

                return exerciseName + " (" + categoryName + ")";
            }

        } catch (SQLException e) {
            System.out.println("Latest workout fetch failed: " + e.getMessage());
        }

        return "No workout recorded yet";
    }

    public String getLatestBMIText() {
        String sql = """
                SELECT bmi_value, bmi_category
                FROM bmi_records
                WHERE user_id = ?
                ORDER BY calculated_date DESC, bmi_id DESC
                LIMIT 1
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Session.getCurrentUserId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double bmiValue = resultSet.getDouble("bmi_value");
                String bmiCategory = resultSet.getString("bmi_category");

                return String.format("%.1f", bmiValue) + " - " + bmiCategory;
            }

        } catch (SQLException e) {
            System.out.println("Latest BMI fetch failed: " + e.getMessage());
        }

        return "No BMI record yet";
    }
}