package utils;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.List;

public class PDFExporter {

    public boolean exportWorkoutDataToPDF(List<Object[]> workoutData, String filePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            Paragraph title = new Paragraph("Iron Ledger - Workout Records", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Paragraph description = new Paragraph("Generated workout report from Iron Ledger application.", normalFont);
            description.setSpacingAfter(15);
            document.add(description);

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            table.addCell("ID");
            table.addCell("Category");
            table.addCell("Exercise");
            table.addCell("Sets");
            table.addCell("Reps");
            table.addCell("Weight");
            table.addCell("Duration");
            table.addCell("Date");

            for (Object[] row : workoutData) {
                table.addCell(String.valueOf(row[0]));
                table.addCell(String.valueOf(row[1]));
                table.addCell(String.valueOf(row[2]));
                table.addCell(String.valueOf(row[3]));
                table.addCell(String.valueOf(row[4]));
                table.addCell(String.valueOf(row[5]));
                table.addCell(String.valueOf(row[6]));
                table.addCell(String.valueOf(row[7]));
            }

            document.add(table);

            Paragraph footer = new Paragraph("\nReport generated successfully.", normalFont);
            footer.setSpacingBefore(15);
            document.add(footer);

            document.close();

            return true;

        } catch (Exception e) {
            System.out.println("PDF export failed: " + e.getMessage());
            return false;
        }
    }
}