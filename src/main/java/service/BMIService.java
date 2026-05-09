package service;

import dao.BMIDAO;
import model.BMIRecord;
import utils.Session;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class BMIService {

    private final BMIDAO bmiDAO = new BMIDAO();

    public double calculateBMI(double heightCm, double weightKg) {
        double heightMeters = heightCm / 100.0;
        return weightKg / (heightMeters * heightMeters);
    }

    public String getBMICategory(double bmiValue) {
        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue < 25) {
            return "Normal weight";
        } else if (bmiValue < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public String calculateAndSaveBMI(String heightText, String weightText) {
        try {
            if (heightText.isBlank() || weightText.isBlank()) {
                return "Height and weight are required.";
            }

            double height = Double.parseDouble(heightText);
            double weight = Double.parseDouble(weightText);

            if (height <= 0 || weight <= 0) {
                return "Height and weight must be positive values.";
            }

            if (height < 50 || height > 250) {
                return "Enter height in centimeters between 50 and 250.";
            }

            if (weight < 10 || weight > 300) {
                return "Enter weight in kilograms between 10 and 300.";
            }

            double bmiValue = calculateBMI(height, weight);
            bmiValue = Math.round(bmiValue * 100.0) / 100.0;

            String category = getBMICategory(bmiValue);

            BMIRecord bmiRecord = new BMIRecord(
                    Session.getCurrentUserId(),
                    height,
                    weight,
                    bmiValue,
                    category,
                    Date.valueOf(LocalDate.now())
            );

            boolean saved = bmiDAO.saveBMIRecord(bmiRecord);

            if (saved) {
                return "SUCCESS:" + bmiValue + ":" + category;
            } else {
                return "Failed to save BMI record.";
            }

        } catch (NumberFormatException e) {
            return "Height and weight must be valid numbers.";
        }
    }

    public List<Object[]> getBMIHistory() {
        return bmiDAO.getBMIHistoryByUser(Session.getCurrentUserId());
    }

    public boolean deleteBMIRecord(int bmiId) {
        return bmiDAO.deleteBMIRecord(bmiId, Session.getCurrentUserId());
    }
}