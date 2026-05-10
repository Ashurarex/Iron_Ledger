package model;

public class Goal {
    private int goalId;
    private int userId;
    private String goalType; // e.g., "WEIGHT_LIFTED", "WORKOUTS_COMPLETED", "WEIGHT_LOSS"
    private double targetValue;
    private double currentValue;    private String targetName;

    public Goal(int goalId, int userId, String goalType, double targetValue, double currentValue, String targetName) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.targetName = targetName;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
    
    public double getProgressPercentage() {
        if (targetValue == 0) return 0;
        double pct = (currentValue / targetValue) * 100;
        return Math.min(pct, 100.0); // Cap at 100%
    }
    
    public String getTargetName() {
        return targetName;
    }
    
    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}
