package cs.superleague.analytics.AnalyticsHelpers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.analytics.exceptions.AnalyticsException;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Driver;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

public class MonthlyAnalyticsHelper {

    private final HashMap<String, Object> data;
    private StoreRepo storeRepo;

    public MonthlyAnalyticsHelper(HashMap<String, Object> data, StoreRepo storeRepo){
        this. data = data;
        this.storeRepo = storeRepo;
    }

    public byte[] createPDF() throws Exception{

        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        try{
            String home = System.getProperty("user.home");
            String file_name = home + "/Downloads/Odosla_MonthlyReport.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();

            Paragraph Title = new Paragraph("Odosla", FontFactory.getFont(FontFactory.TIMES, 40, Font.BOLD));

            document.add(Title);

            document.add(new Paragraph(" "));

            Paragraph Report = new Paragraph("Monthly Statistics Report", FontFactory.getFont(FontFactory.TIMES, 30, Font.BOLD));

            document.add(Report);

            document.add(new Paragraph(" "));

            Paragraph Date = new Paragraph(new Date().toString(), FontFactory.getFont(FontFactory.TIMES, 20, Font.BOLD));

            document.add(Date);

            document.add(new Paragraph(" "));

            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------"));

            document.add(new Paragraph(" "));

            PdfPTable table3 = new PdfPTable(3);

            table3.getDefaultCell().setMinimumHeight(25f);
            table3.setWidths(new int[]{2, 5, 2});

            PdfPCell cl = new PdfPCell(new Phrase("Top Orders", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table3.addCell(cl);

            cl = new PdfPCell(new Phrase("Store", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table3.addCell(cl);

            cl = new PdfPCell(new Phrase("Price", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table3.addCell(cl);
            Order[] topOrders;
            topOrders =  (Order[]) data.get("topTenOrders");

            Optional<Store> storeOptional;
            Store store;
            if(topOrders != null)
            for (int i = 0; i < topOrders.length; i++) {
                table3.addCell(String.valueOf(i + 1));

                if(topOrders[0] != null) {
                    storeOptional = storeRepo.findById(topOrders[i].getStoreID());

                    if(storeOptional == null || !storeOptional.isPresent()){
                        throw new AnalyticsException(storeOptional.toString());
                    }

                    store = storeOptional.get();

                    if(store != null) {
                        table3.addCell(String.valueOf(store.getStoreBrand()));
                        table3.addCell(String.valueOf(topOrders[i].getTotalCost()));
                    }
                }
            }

            PdfPTable table = new PdfPTable(3);

            table.getDefaultCell().setMinimumHeight(25f);
            table.setWidths(new int[]{2, 5, 2});

            PdfPCell cell = new PdfPCell(new Phrase("Top Drivers", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Email", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Rating", FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD)));
            table.addCell(cell);
            Driver[] topDrivers;
            topDrivers =  (Driver[]) data.get("top10Drivers");

            if(topDrivers != null)
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
            table1.addCell(new Paragraph("Total Number Of Orders", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalOrders")));
            table1.addCell(new Paragraph("Total Price", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("totalPrice")));
            table1.addCell(new Paragraph("Average Number of Order Per User", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("averageNumberOfOrderPerUser")));
            table1.addCell(new Paragraph("Average Order Price", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(String.valueOf(data.get("averagePriceOfOrders")));
            table1.addCell(new Paragraph("Top 10 Orders", FontFactory.getFont(FontFactory.TIMES, 13, Font.BOLD)));
            table1.addCell(table3);

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
            String home = System.getProperty("user.home");
            String file_name = home + "/Downloads/Odosla_MonthlyReport.csv";
            PrintWriter pw = new PrintWriter(new FileOutputStream(file_name));
            StringBuilder sb = new StringBuilder(); //variable to start writing to csv

            sb.append("Total Orders");
            sb.append(",");
            sb.append("Total Price");
            sb.append(",");
            sb.append("Average Number Of Orders Per Customer");
            sb.append(",");
            sb.append("Average Price of Orders");
            sb.append(",");
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
            sb.append(data.get("totalOrders"));
            sb.append(",");
            sb.append(data.get("totalPrice"));
            sb.append(",");
            sb.append(data.get("averageNumberOfOrderPerUser"));
            sb.append(",");
            sb.append(data.get("averagePriceOfOrders"));
            sb.append(",");
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

            pw.write(sb.toString()); // write to the csv file
            pw.close();  //stop writing
            System.out.println("finished");

            return sb;
        } catch ( FileNotFoundException e) {
            e.printStackTrace();
            //throw new ReportException("Not able to create CSV file");
        } //lets us know if its successfully completed
        return null;
    }
}