package service;

import dao.WorkoutDAO;
import utils.ExcelExporter;
import utils.PDFExporter;
import utils.Session;

import java.util.List;

public class ExportService {

    private final WorkoutDAO workoutDAO = new WorkoutDAO();
    private final PDFExporter pdfExporter = new PDFExporter();
    private final ExcelExporter excelExporter = new ExcelExporter();

    public List<Object[]> getWorkoutDataForExport() {
        return workoutDAO.getAllWorkoutsByUser(Session.getCurrentUserId());
    }

    public String exportToPDF(String filePath) {
        List<Object[]> workoutData = getWorkoutDataForExport();

        if (workoutData.isEmpty()) {
            return "No workout records available to export.";
        }

        boolean success = pdfExporter.exportWorkoutDataToPDF(workoutData, filePath);

        return success ? "SUCCESS" : "Failed to export PDF.";
    }

    public String exportToExcel(String filePath) {
        List<Object[]> workoutData = getWorkoutDataForExport();

        if (workoutData.isEmpty()) {
            return "No workout records available to export.";
        }

        boolean success = excelExporter.exportWorkoutDataToExcel(workoutData, filePath);

        return success ? "SUCCESS" : "Failed to export Excel file.";
    }
}