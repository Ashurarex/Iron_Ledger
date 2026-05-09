package dao;

import db.DBConnection;
import model.BMIRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BMIDAO {

    public boolean saveBMIRecord(BMIRecord bmiRecord) {
        String sql = """
                INSERT INTO bmi_records
                (user_id, height, weight, bmi_value, bmi_category, calculated_date)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bmiRecord.getUserId());
            statement.setDouble(2, bmiRecord.getHeight());
            statement.setDouble(3, bmiRecord.getWeight());
            statement.setDouble(4, bmiRecord.getBmiValue());
            statement.setString(5, bmiRecord.getBmiCategory());
            statement.setDate(6, bmiRecord.getCalculatedDate());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Save BMI failed: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> getBMIHistoryByUser(int userId) {
        List<Object[]> records = new ArrayList<>();

        String sql = """
                SELECT bmi_id, height, weight, bmi_value, bmi_category, calculated_date
                FROM bmi_records
                WHERE user_id = ?
                ORDER BY calculated_date DESC, bmi_id DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                records.add(new Object[]{
                        resultSet.getInt("bmi_id"),
                        resultSet.getDouble("height"),
                        resultSet.getDouble("weight"),
                        resultSet.getDouble("bmi_value"),
                        resultSet.getString("bmi_category"),
                        resultSet.getDate("calculated_date")
                });
            }

        } catch (SQLException e) {
            System.out.println("Fetch BMI history failed: " + e.getMessage());
        }

        return records;
    }

    public boolean deleteBMIRecord(int bmiId, int userId) {
        String sql = "DELETE FROM bmi_records WHERE bmi_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, bmiId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete BMI failed: " + e.getMessage());
            return false;
        }
    }
}