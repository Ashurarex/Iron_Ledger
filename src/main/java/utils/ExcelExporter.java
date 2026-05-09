package utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter {

    public boolean exportWorkoutDataToExcel(List<Object[]> workoutData, String filePath) {
        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Workout Records");

            Row headerRow = sheet.createRow(0);

            String[] headers = {
                    "Workout ID",
                    "Category",
                    "Exercise",
                    "Sets",
                    "Reps",
                    "Weight",
                    "Duration",
                    "Date"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIndex = 1;

            for (Object[] data : workoutData) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(String.valueOf(data[0]));
                row.createCell(1).setCellValue(String.valueOf(data[1]));
                row.createCell(2).setCellValue(String.valueOf(data[2]));
                row.createCell(3).setCellValue(String.valueOf(data[3]));
                row.createCell(4).setCellValue(String.valueOf(data[4]));
                row.createCell(5).setCellValue(String.valueOf(data[5]));
                row.createCell(6).setCellValue(String.valueOf(data[6]));
                row.createCell(7).setCellValue(String.valueOf(data[7]));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                workbook.write(fileOutputStream);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Excel export failed: " + e.getMessage());
            return false;
        }
    }
}