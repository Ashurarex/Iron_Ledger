package model;

import java.sql.Date;

public class BMIRecord {
    private int bmiId;
    private int userId;
    private double height;
    private double weight;
    private double bmiValue;
    private String bmiCategory;
    private Date calculatedDate;

    public BMIRecord() {
    }

    public BMIRecord(int userId, double height, double weight, double bmiValue,
                     String bmiCategory, Date calculatedDate) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.bmiValue = bmiValue;
        this.bmiCategory = bmiCategory;
        this.calculatedDate = calculatedDate;
    }

    public BMIRecord(int bmiId, int userId, double height, double weight,
                     double bmiValue, String bmiCategory, Date calculatedDate) {
        this.bmiId = bmiId;
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.bmiValue = bmiValue;
        this.bmiCategory = bmiCategory;
        this.calculatedDate = calculatedDate;
    }

    public int getBmiId() {
        return bmiId;
    }

    public int getUserId() {
        return userId;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getBmiValue() {
        return bmiValue;
    }

    public String getBmiCategory() {
        return bmiCategory;
    }

    public Date getCalculatedDate() {
        return calculatedDate;
    }
}