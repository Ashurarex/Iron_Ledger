package model;

import java.util.ArrayList;
import java.util.List;

public class Routine {
    private int routineId;
    private int userId;
    private String routineName;
    private List<RoutineExercise> exercises;

    public Routine(int routineId, int userId, String routineName) {
        this.routineId = routineId;
        this.userId = userId;
        this.routineName = routineName;
        this.exercises = new ArrayList<>();
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRoutineName() {
        return routineName;
    }

    public void setRoutineName(String routineName) {
        this.routineName = routineName;
    }

    public List<RoutineExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<RoutineExercise> exercises) {
        this.exercises = exercises;
    }
    
    public void addExercise(RoutineExercise exercise) {
        this.exercises.add(exercise);
    }
}
