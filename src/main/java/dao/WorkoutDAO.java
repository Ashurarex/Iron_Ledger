package dao;

import db.DBConnection;
import model.Workout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutDAO {

    public boolean addWorkout(Workout workout) {
        String sql = """
                INSERT INTO workouts
                (user_id, category_id, exercise_name, sets_count, reps, weight, duration, workout_date)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, workout.getUserId());
            statement.setInt(2, workout.getCategoryId());
            statement.setString(3, workout.getExerciseName());
            statement.setInt(4, workout.getSetsCount());
            statement.setInt(5, workout.getReps());
            statement.setDouble(6, workout.getWeight());
            statement.setInt(7, workout.getDuration());
            statement.setDate(8, workout.getWorkoutDate());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add workout failed: " + e.getMessage());
            return false;
        }
    }

    public boolean updateWorkout(Workout workout) {
        String sql = """
                UPDATE workouts
                SET category_id = ?, exercise_name = ?, sets_count = ?, reps = ?,
                    weight = ?, duration = ?, workout_date = ?
                WHERE workout_id = ? AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, workout.getCategoryId());
            statement.setString(2, workout.getExerciseName());
            statement.setInt(3, workout.getSetsCount());
            statement.setInt(4, workout.getReps());
            statement.setDouble(5, workout.getWeight());
            statement.setInt(6, workout.getDuration());
            statement.setDate(7, workout.getWorkoutDate());
            statement.setInt(8, workout.getWorkoutId());
            statement.setInt(9, workout.getUserId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update workout failed: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteWorkout(int workoutId, int userId) {
        String sql = "DELETE FROM workouts WHERE workout_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, workoutId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete workout failed: " + e.getMessage());
            return false;
        }
    }

    public List<Object[]> getAllWorkoutsByUser(int userId) {
        List<Object[]> workouts = new ArrayList<>();

        String sql = """
                SELECT w.workout_id, c.category_name, w.exercise_name, w.sets_count,
                       w.reps, w.weight, w.duration, w.workout_date, w.category_id
                FROM workouts w
                LEFT JOIN exercise_categories c ON w.category_id = c.category_id
                WHERE w.user_id = ?
                ORDER BY w.workout_date DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                workouts.add(new Object[]{
                        resultSet.getInt("workout_id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("exercise_name"),
                        resultSet.getInt("sets_count"),
                        resultSet.getInt("reps"),
                        resultSet.getDouble("weight"),
                        resultSet.getInt("duration"),
                        resultSet.getDate("workout_date"),
                        resultSet.getInt("category_id")
                });
            }

        } catch (SQLException e) {
            System.out.println("Fetch workouts failed: " + e.getMessage());
        }

        return workouts;
    }

    public List<Object[]> searchWorkouts(int userId, String exerciseName, int categoryId, String fromDate, String toDate) {
        List<Object[]> workouts = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT w.workout_id, c.category_name, w.exercise_name, w.sets_count,
                       w.reps, w.weight, w.duration, w.workout_date, w.category_id
                FROM workouts w
                LEFT JOIN exercise_categories c ON w.category_id = c.category_id
                WHERE w.user_id = ?
                """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (exerciseName != null && !exerciseName.isBlank()) {
            sql.append(" AND LOWER(w.exercise_name) LIKE LOWER(?) ");
            params.add("%" + exerciseName.trim() + "%");
        }

        if (categoryId > 0) {
            sql.append(" AND w.category_id = ? ");
            params.add(categoryId);
        }

        if (fromDate != null && !fromDate.isBlank()) {
            sql.append(" AND w.workout_date >= ? ");
            params.add(Date.valueOf(fromDate));
        }

        if (toDate != null && !toDate.isBlank()) {
            sql.append(" AND w.workout_date <= ? ");
            params.add(Date.valueOf(toDate));
        }

        sql.append(" ORDER BY w.workout_date DESC ");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object value = params.get(i);

                if (value instanceof Integer) {
                    statement.setInt(i + 1, (Integer) value);
                } else if (value instanceof Date) {
                    statement.setDate(i + 1, (Date) value);
                } else {
                    statement.setString(i + 1, String.valueOf(value));
                }
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                workouts.add(new Object[]{
                        resultSet.getInt("workout_id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("exercise_name"),
                        resultSet.getInt("sets_count"),
                        resultSet.getInt("reps"),
                        resultSet.getDouble("weight"),
                        resultSet.getInt("duration"),
                        resultSet.getDate("workout_date"),
                        resultSet.getInt("category_id")
                });
            }

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Search workouts failed: " + e.getMessage());
        }

        return workouts;
    }

    public List<Object[]> getWorkoutCountByCategory(int userId) {
        List<Object[]> data = new ArrayList<>();

        String sql = """
                SELECT c.category_name, COUNT(w.workout_id) AS workout_count
                FROM workouts w
                LEFT JOIN exercise_categories c ON w.category_id = c.category_id
                WHERE w.user_id = ?
                GROUP BY c.category_name
                ORDER BY workout_count DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new Object[]{
                        resultSet.getString("category_name"),
                        resultSet.getInt("workout_count")
                });
            }

        } catch (SQLException e) {
            System.out.println("Category report failed: " + e.getMessage());
        }

        return data;
    }

    public List<Object[]> getWorkoutDurationByDate(int userId) {
        List<Object[]> data = new ArrayList<>();

        String sql = """
                SELECT workout_date, SUM(duration) AS total_duration
                FROM workouts
                WHERE user_id = ?
                GROUP BY workout_date
                ORDER BY workout_date
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new Object[]{
                        resultSet.getDate("workout_date"),
                        resultSet.getInt("total_duration")
                });
            }

        } catch (SQLException e) {
            System.out.println("Duration report failed: " + e.getMessage());
        }

        return data;
    }

    public List<Object[]> getWeightProgressByDate(int userId) {
        List<Object[]> data = new ArrayList<>();

        String sql = """
                SELECT workout_date, SUM(weight) AS total_weight
                FROM workouts
                WHERE user_id = ?
                GROUP BY workout_date
                ORDER BY workout_date
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new Object[]{
                        resultSet.getDate("workout_date"),
                        resultSet.getDouble("total_weight")
                });
            }

        } catch (SQLException e) {
            System.out.println("Weight progress report failed: " + e.getMessage());
        }

        return data;
    }

    public Object[] getWorkoutSummary(int userId) {
        String sql = """
                SELECT 
                    COUNT(*) AS total_workouts,
                    COALESCE(SUM(duration), 0) AS total_duration,
                    COALESCE(SUM(weight), 0) AS total_weight,
                    COALESCE(SUM(sets_count), 0) AS total_sets,
                    COALESCE(SUM(reps), 0) AS total_reps
                FROM workouts
                WHERE user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Object[]{
                        resultSet.getInt("total_workouts"),
                        resultSet.getInt("total_duration"),
                        resultSet.getDouble("total_weight"),
                        resultSet.getInt("total_sets"),
                        resultSet.getInt("total_reps")
                };
            }

        } catch (SQLException e) {
            System.out.println("Workout summary failed: " + e.getMessage());
        }

        return new Object[]{0, 0, 0.0, 0, 0};
    }

    public List<Object[]> getVolumeByCategory(int userId) {
        List<Object[]> data = new ArrayList<>();

        String sql = """
                SELECT c.category_name, SUM(w.sets_count * w.reps * w.weight) AS total_volume
                FROM workouts w
                LEFT JOIN exercise_categories c ON w.category_id = c.category_id
                WHERE w.user_id = ?
                GROUP BY c.category_name
                ORDER BY total_volume DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new Object[]{
                        resultSet.getString("category_name"),
                        resultSet.getDouble("total_volume")
                    });
            }

        } catch (SQLException e) {
            System.out.println("Volume report failed: " + e.getMessage());
        }

        return data;
    }

    public double getMaxWeightForExercise(int userId, String exerciseName) {
        String sql = """
                SELECT MAX(weight) AS max_weight
                FROM workouts
                WHERE user_id = ? AND LOWER(exercise_name) = LOWER(?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, exerciseName.trim());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("max_weight");
            }

        } catch (SQLException e) {
            System.out.println("Max weight for exercise failed: " + e.getMessage());
        }

        return 0.0;
    }

    public List<Date> getWorkoutDatesForMonth(int userId, int year, int month) {
        List<Date> dates = new ArrayList<>();
        String sql = """
                SELECT DISTINCT workout_date
                FROM workouts
                WHERE user_id = ? AND YEAR(workout_date) = ? AND MONTH(workout_date) = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, year);
            statement.setInt(3, month);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                dates.add(resultSet.getDate("workout_date"));
            }

        } catch (SQLException e) {
            System.out.println("Fetch workout dates for month failed: " + e.getMessage());
        }

        return dates;
    }

    public List<Object[]> getWeeklyVolume(int userId) {
        List<Object[]> data = new ArrayList<>();
        String sql = """
                SELECT CONCAT('Week ', WEEK(workout_date)) as label, SUM(sets_count * reps * weight) as volume
                FROM workouts
                WHERE user_id = ? AND workout_date >= DATE_SUB(CURDATE(), INTERVAL 8 WEEK)
                GROUP BY label
                ORDER BY MIN(workout_date)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Object[]{rs.getString("label"), rs.getDouble("volume")});
            }
        } catch (SQLException e) { System.out.println("Weekly volume failed: " + e.getMessage()); }
        return data;
    }

    public List<Object[]> getMonthlyVolume(int userId) {
        List<Object[]> data = new ArrayList<>();
        String sql = """
                SELECT DATE_FORMAT(workout_date, '%b %Y') as label, SUM(sets_count * reps * weight) as volume
                FROM workouts
                WHERE user_id = ? AND workout_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
                GROUP BY label
                ORDER BY MIN(workout_date)
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new Object[]{rs.getString("label"), rs.getDouble("volume")});
            }
        } catch (SQLException e) { System.out.println("Monthly volume failed: " + e.getMessage()); }
        return data;
    }
}