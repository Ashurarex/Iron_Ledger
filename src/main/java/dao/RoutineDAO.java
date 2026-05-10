package dao;

import db.DBConnection;
import model.Routine;
import model.RoutineExercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoutineDAO {

    public RoutineDAO() {
        createTablesIfNotExists();
    }

    private void createTablesIfNotExists() {
        String createRoutinesTable = """
                CREATE TABLE IF NOT EXISTS routines (
                    routine_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT NOT NULL,
                    routine_name VARCHAR(255) NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
                """;

        String createRoutineExercisesTable = """
                CREATE TABLE IF NOT EXISTS routine_exercises (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    routine_id INT NOT NULL,
                    category_id INT NOT NULL,
                    exercise_name VARCHAR(255) NOT NULL,
                    default_sets INT NOT NULL DEFAULT 3,
                    default_reps INT NOT NULL DEFAULT 10,
                    FOREIGN KEY (routine_id) REFERENCES routines(routine_id) ON DELETE CASCADE,
                    FOREIGN KEY (category_id) REFERENCES exercise_categories(category_id) ON DELETE CASCADE
                )
                """;

        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
             
            if (connection != null) {
                statement.execute(createRoutinesTable);
                statement.execute(createRoutineExercisesTable);
            }
            
        } catch (SQLException e) {
            System.out.println("Failed to initialize Routine tables: " + e.getMessage());
        }
    }

    public int createRoutine(Routine routine) {
        String sql = "INSERT INTO routines (user_id, routine_name) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, routine.getUserId());
            statement.setString(2, routine.getRoutineName());
            
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Create routine failed: " + e.getMessage());
        }
        return -1;
    }

    public boolean addExerciseToRoutine(RoutineExercise exercise) {
        String sql = """
                INSERT INTO routine_exercises 
                (routine_id, category_id, exercise_name, default_sets, default_reps) 
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, exercise.getRoutineId());
            statement.setInt(2, exercise.getCategoryId());
            statement.setString(3, exercise.getExerciseName());
            statement.setInt(4, exercise.getDefaultSets());
            statement.setInt(5, exercise.getDefaultReps());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Add exercise to routine failed: " + e.getMessage());
            return false;
        }
    }

    public List<Routine> getRoutinesByUser(int userId) {
        List<Routine> routines = new ArrayList<>();
        String sql = "SELECT * FROM routines WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Routine routine = new Routine(
                        resultSet.getInt("routine_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("routine_name")
                );
                
                // Fetch exercises for this routine
                routine.setExercises(getExercisesForRoutine(routine.getRoutineId()));
                
                routines.add(routine);
            }

        } catch (SQLException e) {
            System.out.println("Fetch routines failed: " + e.getMessage());
        }

        return routines;
    }

    public List<RoutineExercise> getExercisesForRoutine(int routineId) {
        List<RoutineExercise> exercises = new ArrayList<>();
        String sql = """
                SELECT re.*, c.category_name 
                FROM routine_exercises re
                LEFT JOIN exercise_categories c ON re.category_id = c.category_id
                WHERE re.routine_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, routineId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RoutineExercise exercise = new RoutineExercise(
                        resultSet.getInt("id"),
                        resultSet.getInt("routine_id"),
                        resultSet.getInt("category_id"),
                        resultSet.getString("exercise_name"),
                        resultSet.getInt("default_sets"),
                        resultSet.getInt("default_reps")
                );
                exercise.setCategoryName(resultSet.getString("category_name"));
                exercises.add(exercise);
            }

        } catch (SQLException e) {
            System.out.println("Fetch routine exercises failed: " + e.getMessage());
        }

        return exercises;
    }

    public boolean deleteRoutine(int routineId, int userId) {
        String sql = "DELETE FROM routines WHERE routine_id = ? AND user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, routineId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Delete routine failed: " + e.getMessage());
            return false;
        }
    }
}
