package cs.superleague.analytics.responses;

import javax.swing.text.Document;

public class CreateCustomerReportResponse{

    private final Document PDFReport;
    private final StringBuilder CSVReport;

    public CreateCustomerReportResponse(Document PDFReport, StringBuilder CSVReport) {
        this.PDFReport = PDFReport;
        this.CSVReport = CSVReport;
    }

    public Document getPDFReport() {
        return PDFReport;
    }

    public StringBuilder getCSVReport() {
        return CSVReport;
    }
}