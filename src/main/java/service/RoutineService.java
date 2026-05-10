package service;

import dao.RoutineDAO;
import model.Routine;
import model.RoutineExercise;
import utils.Session;

import java.util.List;

public class RoutineService {

    private final RoutineDAO routineDAO = new RoutineDAO();

    public String createRoutine(String routineName, List<RoutineExercise> exercises) {
        if (Session.getCurrentUser() == null) {
            return "You must be logged in to create routines.";
        }

        if (routineName == null || routineName.trim().isEmpty()) {
            return "Routine name cannot be empty.";
        }
        
        if (exercises == null || exercises.isEmpty()) {
            return "A routine must have at least one exercise.";
        }

        Routine routine = new Routine(0, Session.getCurrentUser().getUserId(), routineName.trim());
        int routineId = routineDAO.createRoutine(routine);

        if (routineId > 0) {
            for (RoutineExercise ex : exercises) {
                ex.setRoutineId(routineId);
                routineDAO.addExerciseToRoutine(ex);
            }
            return "SUCCESS";
        }

        return "Failed to create routine.";
    }

    public List<Routine> getUserRoutines() {
        if (Session.getCurrentUser() == null) {
            return List.of();
        }
        return routineDAO.getRoutinesByUser(Session.getCurrentUser().getUserId());
    }

    public boolean deleteRoutine(int routineId) {
        if (Session.getCurrentUser() == null) {
            return false;
        }
        return routineDAO.deleteRoutine(routineId, Session.getCurrentUser().getUserId());
    }
}
