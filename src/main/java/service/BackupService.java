package service;

import db.DBConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BackupService {

    public String createBackup(String directoryPath) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "IronLedger_Backup_" + timestamp + ".json";
        File file = new File(directoryPath, filename);

        StringBuilder json = new StringBuilder("{\n");

        String[] tables = {"users", "exercise_categories", "workouts", "routines", "routine_exercises", "goals"};

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            for (int i = 0; i < tables.length; i++) {
                String table = tables[i];
                json.append("  \"").append(table).append("\": [\n");
                
                try {
                    ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    
                    List<String> rows = new ArrayList<>();
                    while (rs.next()) {
                        StringBuilder rowJson = new StringBuilder("    {");
                        for (int c = 1; c <= columnCount; c++) {
                            String colName = rsmd.getColumnName(c);
                            String colValue = rs.getString(c);
                            
                            rowJson.append("\"").append(colName).append("\": ");
                            if (colValue == null) {
                                rowJson.append("null");
                            } else if (rsmd.getColumnType(c) == java.sql.Types.INTEGER || 
                                       rsmd.getColumnType(c) == java.sql.Types.DOUBLE ||
                                       rsmd.getColumnType(c) == java.sql.Types.FLOAT) {
                                rowJson.append(colValue);
                            } else {
                                // Escape quotes
                                colValue = colValue.replace("\"", "\\\"");
                                rowJson.append("\"").append(colValue).append("\"");
                            }
                            
                            if (c < columnCount) rowJson.append(", ");
                        }
                        rowJson.append("}");
                        rows.add(rowJson.toString());
                    }
                    
                    json.append(String.join(",\n", rows));
                    json.append("\n  ]");
                    if (i < tables.length - 1) json.append(",");
                    json.append("\n");
                    
                } catch (Exception e) {
                    System.out.println("Could not backup table " + table + ": " + e.getMessage());
                }
            }
            json.append("}\n");

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json.toString());
            }

            return "SUCCESS|" + file.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create backup: " + e.getMessage();
        }
    }
}
