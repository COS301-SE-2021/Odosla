package cs.superleague.analytics.AnalyticsHelpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.analytics.exceptions.AnalyticsException;
import cs.superleague.user.dataclass.Driver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

public class UserAnalyticsHelper {

    private final HashMap<String, Object> data;

    public UserAnalyticsHelper(HashMap<String, Object> data){
        this. data = data;
    }

    public byte[] createPDF() throws Exception{

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        try{
            document.open();

            Paragraph Title = new Paragraph("Odosla", FontFactory.getFont(FontFactory.TIMES, 40, Font.BOLD));

            document.add(Title);

            document.add(new Paragraph(" "));

            Paragraph Report = new Paragraph("User Statistics Report", FontFactory.getFont(FontFactory.TIMES, 30, Font.BOLD));

            document.add(Report);

            document.add(new Paragraph(" "));

            Paragraph Date = new Paragraph(new Date().toString(), FontFactory.getFont(FontFactory.TIMES, 20, Font.BOLD));

            document.add(Date);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(3);

            table.getDefaultCell().setMinimumHeight(25f);
            table.setWidths(new int[]{2, 5, 2});

            PdfPCell cl = new PdfPCell(new Phrase("Top Drivers", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table.addCell(cl);

            cl = new PdfPCell(new Phrase("Email", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table.addCell(cl);

            cl = new PdfPCell(new Phrase("Rating", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table.addCell(cl);
            Driver[] topDrivers;
            topDrivers =  (Driver[]) data.get("top10Drivers");

            for (int i = 0; i < topDrivers.length; i++) {
                table.addCell(String.valueOf(i + 1));

                if(topDrivers[0] != null) {
                    table.addCell(String.valueOf(topDrivers[i].getEmail()));
                    table.addCell(String.valueOf(topDrivers[i].getRating()));
                }
            }

            PdfPTable table1 = new PdfPTable(2);

            table1.getDefaultCell().setMinimumHeight(35f);
            table1.setWidths(new int[]{2, 5});
            table1.setWidthPercentage(100);
            table1.addCell(new Paragraph("Reporting Period", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(data.get("startDate")+"   -   "+ data.get("endDate"));
            table1.addCell(new Paragraph("Total Number Of Users", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_Users")));
            table1.addCell(new Paragraph("Total Number Of Admins", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_Admins")));
            table1.addCell(new Paragraph("Total Number Of Customers", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_Customers")));
            table1.addCell(new Paragraph("Total Number Of Shoppers", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_Shoppers")));
            table1.addCell(new Paragraph("Total Number Of Drivers", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_Drivers")));
            table1.addCell(new Paragraph("Total Number Of Drivers On Shift", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_DriversOnShift")));
            table1.addCell(new Paragraph("Total Number Of Orders Completed", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalNum_OrdersCompletedByShoppers")));
            table1.addCell(new Paragraph("Average driver rating", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("averageRating_Drivers")));
            table1.addCell(new Paragraph("Top 10 Drivers", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(table);

            document.add(table1);

            document.close();

            System.out.println("Report Complete!");

        }catch (Exception e){
            throw new AnalyticsException("Problem with creating PDF ", e);
        }
        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return pdfBytes;
    }

    public StringBuilder createCSVReport() {
        try {
            StringBuilder sb = new StringBuilder(); //variable to start writing to csv

            sb.append("Total users");
            sb.append(",");
            sb.append("Total Number Of Admins");
            sb.append(",");
            sb.append("Total Number Of Customers");
            sb.append(",");
            sb.append("Total Number Of Shoppers");
            sb.append(",");
            sb.append("Total Number Of Drivers");
            sb.append(",");
            sb.append("Total Number Of Drivers On Shift");
            sb.append(",");
            sb.append("Total Number Of Orders Completed");
            sb.append(",");
            sb.append("Average driver rating");
            sb.append(",");
            sb.append("\n");
            sb.append(data.get("totalNum_Users"));
            sb.append(",");
            sb.append(data.get("totalNum_Admins"));
            sb.append(",");
            sb.append(data.get("totalNum_Customers"));
            sb.append(",");
            sb.append(data.get("totalNum_Shoppers"));
            sb.append(",");
            sb.append(data.get("totalNum_Drivers"));
            sb.append(",");
            sb.append(data.get("totalNum_DriversOnShift"));
            sb.append(",");
            sb.append(data.get("totalNum_OrdersCompletedByShoppers"));
            sb.append(",");
            sb.append(data.get("averageRating_Drivers"));
            sb.append(",");

            System.out.println("finished");

            return sb;
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
