package service;

import dao.GoalDAO;
import model.Goal;
import utils.Session;

import java.util.List;

public class GoalService {
    
    private final GoalDAO goalDAO = new GoalDAO();
    
    public String createGoal(String goalType, String targetValueStr, String targetName) {
        if (Session.getCurrentUser() == null) {
            return "You must be logged in to create a goal.";
        }
        
        try {
            double targetValue = Double.parseDouble(targetValueStr);
            if (targetValue <= 0) {
                return "Target value must be greater than zero.";
            }
            
            Goal goal = new Goal(0, Session.getCurrentUser().getUserId(), goalType, targetValue, 0.0, targetName);
            
            if (goalDAO.createGoal(goal)) {
                return "SUCCESS";
            } else {
                return "Failed to save goal.";
            }
        } catch (NumberFormatException e) {
            return "Invalid target value. Please enter a valid number.";
        }
    }
    
    public List<Goal> getUserGoals() {
        if (Session.getCurrentUser() == null) {
            return List.of();
        }
        return goalDAO.getGoalsByUser(Session.getCurrentUser().getUserId());
    }
    
    public boolean updateGoalProgress(int goalId, double currentValue) {
        return goalDAO.updateGoalCurrentValue(goalId, currentValue);
    }
    
    public boolean deleteGoal(int goalId) {
        if (Session.getCurrentUser() == null) {
            return false;
        }
        return goalDAO.deleteGoal(goalId, Session.getCurrentUser().getUserId());
    }
    
    // Automatically recalculates goals based on workout data
    public void syncGoalsWithWorkouts() {
        if (Session.getCurrentUser() == null) return;
        
        ReportService reportService = new ReportService();
        Object[] summary = reportService.getWorkoutSummary();
        // summary = {totalWorkouts, totalDuration, totalWeight, totalSets, totalReps}
        
        int totalWorkouts = (int) summary[0];
        double totalWeight = (double) summary[2];
        
        dao.WorkoutDAO workoutDAO = new dao.WorkoutDAO();
        
        List<Goal> goals = getUserGoals();
        for (Goal g : goals) {
            double newValue = g.getCurrentValue();
            
            if (g.getGoalType().equals("WORKOUTS_COMPLETED")) {
                newValue = totalWorkouts;
            } else if (g.getGoalType().equals("WEIGHT_LIFTED")) {
                newValue = totalWeight;
            } else if (g.getGoalType().equals("SPECIFIC_EXERCISE_WEIGHT")) {
                if (g.getTargetName() != null && !g.getTargetName().isBlank()) {
                    newValue = workoutDAO.getMaxWeightForExercise(Session.getCurrentUser().getUserId(), g.getTargetName());
                }
            }
            // More types can be added here
            
            if (newValue != g.getCurrentValue()) {
                updateGoalProgress(g.getGoalId(), newValue);
            }
        }
    }
    public String editGoal(int goalId, String goalType, String targetValueStr, String targetName) {
        if (Session.getCurrentUser() == null) {
            return "You must be logged in to edit a goal.";
        }
        
        try {
            double targetValue = Double.parseDouble(targetValueStr);
            if (targetValue <= 0) {
                return "Target value must be greater than zero.";
            }
            
            if (goalDAO.updateGoal(goalId, Session.getCurrentUser().getUserId(), goalType, targetValue, targetName)) {
                return "SUCCESS";
            } else {
                return "Failed to update goal.";
            }
        } catch (NumberFormatException e) {
            return "Invalid target value. Please enter a valid number.";
        }
    }
}
