package service;

import dao.WorkoutDAO;
import db.DBConnection;
import utils.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GamificationService {
    
    private final WorkoutDAO workoutDAO = new WorkoutDAO();
    
    public int calculateCurrentStreak() {
        if (Session.getCurrentUser() == null) return 0;
        
        // Get all unique dates the user worked out, sorted descending
        Set<LocalDate> workoutDates = new TreeSet<>((d1, d2) -> d2.compareTo(d1));
        
        String sql = "SELECT DISTINCT workout_date FROM workouts WHERE user_id = ? ORDER BY workout_date DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
             
            statement.setInt(1, Session.getCurrentUser().getUserId());
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                workoutDates.add(LocalDate.parse(rs.getString("workout_date")));
            }
        } catch (SQLException e) {
            System.out.println("Streak calculation failed: " + e.getMessage());
            return 0;
        }
        
        if (workoutDates.isEmpty()) return 0;
        
        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate expectedDate = today;
        
        List<LocalDate> sortedDates = new ArrayList<>(workoutDates);
        
        // If they haven't worked out today, see if they worked out yesterday.
        // If not today AND not yesterday, streak is 0.
        if (!sortedDates.contains(today) && !sortedDates.contains(today.minusDays(1))) {
            return 0;
        }
        
        // Count consecutive days going backwards
        for (LocalDate date : sortedDates) {
            if (date.equals(expectedDate) || date.equals(expectedDate.minusDays(1))) {
                // Found a workout on the expected day
                streak++;
                expectedDate = date.minusDays(1);
            } else if (date.isBefore(expectedDate.minusDays(1))) {
                // Gap in dates found
                break;
            }
        }
        
        return streak;
    }
    
    public List<String> getEarnedBadges() {
        List<String> badges = new ArrayList<>();
        if (Session.getCurrentUser() == null) return badges;
        
        ReportService reportService = new ReportService();
        Object[] summary = reportService.getWorkoutSummary();
        int totalWorkouts = (int) summary[0];
        double totalWeight = (double) summary[2];
        
        // Workout Count Badges
        if (totalWorkouts >= 1) badges.add("First Workout!");
        if (totalWorkouts >= 10) badges.add("10 Workouts Club");
        if (totalWorkouts >= 50) badges.add("50 Workouts Veteran");
        if (totalWorkouts >= 100) badges.add("100 Workouts Master");
        
        // Volume Badges
        if (totalWeight >= 1000) badges.add("1,000kg Volume");
        if (totalWeight >= 10000) badges.add("10,000kg Volume");
        if (totalWeight >= 50000) badges.add("50,000kg Volume");
        
        // Streak Badges
        int streak = calculateCurrentStreak();
        if (streak >= 3) badges.add("3-Day Streak");
        if (streak >= 7) badges.add("7-Day Streak");
        if (streak >= 30) badges.add("30-Day Streak");
        
        return badges;
    }
}
