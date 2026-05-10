package service;

import dao.WorkoutDAO;
import utils.Session;

import java.util.List;

public class ReportService {

    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    public Object[] getWorkoutSummary() {
        return workoutDAO.getWorkoutSummary(Session.getCurrentUserId());
    }

    public List<Object[]> getWorkoutCountByCategory() {
        return workoutDAO.getWorkoutCountByCategory(Session.getCurrentUserId());
    }

    public List<Object[]> getWorkoutDurationByDate() {
        return workoutDAO.getWorkoutDurationByDate(Session.getCurrentUserId());
    }

    public List<Object[]> getWeightProgressByDate() {
        return workoutDAO.getWeightProgressByDate(Session.getCurrentUserId());
    }

    public List<Object[]> getVolumeByCategory() {
        return workoutDAO.getVolumeByCategory(Session.getCurrentUserId());
    }

    public List<Object[]> getWeeklyVolume() {
        return workoutDAO.getWeeklyVolume(Session.getCurrentUserId());
    }

    public List<Object[]> getMonthlyVolume() {
        return workoutDAO.getMonthlyVolume(Session.getCurrentUserId());
    }
}