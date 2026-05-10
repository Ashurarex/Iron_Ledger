package model;

public class RoutineExercise {
    private int id;
    private int routineId;
    private int categoryId;
    private String exerciseName;
    private int defaultSets;
    private int defaultReps;
    
    // Additional transient field for display purposes
    private String categoryName;

    public RoutineExercise(int id, int routineId, int categoryId, String exerciseName, int defaultSets, int defaultReps) {
        this.id = id;
        this.routineId = routineId;
        this.categoryId = categoryId;
        this.exerciseName = exerciseName;
        this.defaultSets = defaultSets;
        this.defaultReps = defaultReps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getDefaultSets() {
        return defaultSets;
    }

    public void setDefaultSets(int defaultSets) {
        this.defaultSets = defaultSets;
    }

    public int getDefaultReps() {
        return defaultReps;
    }

    public void setDefaultReps(int defaultReps) {
        this.defaultReps = defaultReps;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
