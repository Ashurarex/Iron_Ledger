package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseLibraryService {

    public Map<String, List<String>> getExerciseLibrary() {
        Map<String, List<String>> library = new HashMap<>();

        library.put("Chest", List.of(
            "Bench Press (Barbell/Dumbbell)",
            "Incline Bench Press",
            "Chest Fly",
            "Push-ups",
            "Dips"
        ));

        library.put("Back", List.of(
            "Deadlift",
            "Pull-ups / Lat Pulldowns",
            "Bent-over Rows",
            "Seated Cable Rows",
            "Face Pulls"
        ));

        library.put("Legs", List.of(
            "Squats (Back/Front)",
            "Leg Press",
            "Lunges",
            "Leg Extensions",
            "Leg Curls"
        ));

        library.put("Shoulders", List.of(
            "Overhead Press",
            "Lateral Raises",
            "Front Raises",
            "Rear Delt Fly",
            "Shrugs"
        ));

        library.put("Arms", List.of(
            "Bicep Curls",
            "Hammer Curls",
            "Tricep Extensions",
            "Skull Crushers",
            "Close-grip Bench Press"
        ));

        library.put("Core", List.of(
            "Plank",
            "Crunches",
            "Leg Raises",
            "Russian Twists",
            "Ab Rollouts"
        ));

        return library;
    }
}
