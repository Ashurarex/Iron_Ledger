package service;

import dao.WorkoutDAO;
import model.Workout;
import utils.Session;

import java.sql.Date;
import java.util.List;

public class WorkoutService {

    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    public String addWorkout(int categoryId, String exerciseName, String setsText,
                             String repsText, String weightText, String durationText, String dateText) {
        try {
            if (exerciseName.isBlank() || setsText.isBlank() || repsText.isBlank()
                    || weightText.isBlank() || durationText.isBlank() || dateText.isBlank()) {
                return "All fields are required.";
            }

            int sets = Integer.parseInt(setsText);
            int reps = Integer.parseInt(repsText);
            double weight = Double.parseDouble(weightText);
            int duration = Integer.parseInt(durationText);
            Date workoutDate = Date.valueOf(dateText);

            if (sets <= 0 || reps <= 0 || weight < 0 || duration <= 0) {
                return "Enter valid positive values.";
            }

            Workout workout = new Workout(
                    Session.getCurrentUserId(),
                    categoryId,
                    exerciseName,
                    sets,
                    reps,
                    weight,
                    duration,
                    workoutDate
            );

            return workoutDAO.addWorkout(workout) ? "SUCCESS" : "Failed to add workout.";

        } catch (NumberFormatException e) {
            return "Sets, reps, weight, and duration must be valid numbers.";
        } catch (IllegalArgumentException e) {
            return "Date must be in YYYY-MM-DD format.";
        }
    }

    public String updateWorkout(int workoutId, int categoryId, String exerciseName, String setsText,
                                String repsText, String weightText, String durationText, String dateText) {
        try {
            if (workoutId <= 0) {
                return "Select a workout first.";
            }

            if (exerciseName.isBlank() || setsText.isBlank() || repsText.isBlank()
                    || weightText.isBlank() || durationText.isBlank() || dateText.isBlank()) {
                return "All fields are required.";
            }

            int sets = Integer.parseInt(setsText);
            int reps = Integer.parseInt(repsText);
            double weight = Double.parseDouble(weightText);
            int duration = Integer.parseInt(durationText);
            Date workoutDate = Date.valueOf(dateText);

            if (sets <= 0 || reps <= 0 || weight < 0 || duration <= 0) {
                return "Enter valid positive values.";
            }

            Workout workout = new Workout(
                    workoutId,
                    Session.getCurrentUserId(),
                    categoryId,
                    exerciseName,
                    sets,
                    reps,
                    weight,
                    duration,
                    workoutDate
            );

            return workoutDAO.updateWorkout(workout) ? "SUCCESS" : "Failed to update workout.";

        } catch (NumberFormatException e) {
            return "Sets, reps, weight, and duration must be valid numbers.";
        } catch (IllegalArgumentException e) {
            return "Date must be in YYYY-MM-DD format.";
        }
    }

    public boolean deleteWorkout(int workoutId) {
        return workoutDAO.deleteWorkout(workoutId, Session.getCurrentUserId());
    }

    public List<Object[]> getUserWorkouts() {
        return workoutDAO.getAllWorkoutsByUser(Session.getCurrentUserId());
    }

    public List<Object[]> searchWorkouts(String exerciseName, int categoryId, String fromDate, String toDate) {
        return workoutDAO.searchWorkouts(
                Session.getCurrentUserId(),
                exerciseName,
                categoryId,
                fromDate,
                toDate
        );
    }

    public List<java.sql.Date> getWorkoutDatesForMonth(java.time.YearMonth yearMonth) {
        return workoutDAO.getWorkoutDatesForMonth(
                Session.getCurrentUserId(),
                yearMonth.getYear(),
                yearMonth.getMonthValue()
        );
    }
}