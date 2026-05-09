package model;

import java.sql.Date;

public class Workout {
    private int workoutId;
    private int userId;
    private int categoryId;
    private String exerciseName;
    private int setsCount;
    private int reps;
    private double weight;
    private int duration;
    private Date workoutDate;

    public Workout() {
    }

    public Workout(int userId, int categoryId, String exerciseName, int setsCount,
                   int reps, double weight, int duration, Date workoutDate) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.exerciseName = exerciseName;
        this.setsCount = setsCount;
        this.reps = reps;
        this.weight = weight;
        this.duration = duration;
        this.workoutDate = workoutDate;
    }

    public Workout(int workoutId, int userId, int categoryId, String exerciseName,
                   int setsCount, int reps, double weight, int duration, Date workoutDate) {
        this.workoutId = workoutId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.exerciseName = exerciseName;
        this.setsCount = setsCount;
        this.reps = reps;
        this.weight = weight;
        this.duration = duration;
        this.workoutDate = workoutDate;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getSetsCount() {
        return setsCount;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public int getDuration() {
        return duration;
    }

    public Date getWorkoutDate() {
        return workoutDate;
    }
}