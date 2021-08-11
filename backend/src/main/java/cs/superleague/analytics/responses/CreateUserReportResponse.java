package cs.superleague.analytics.responses;

import com.itextpdf.text.Document;

public class CreateUserReportResponse {

    private final Document PDFReport;
    private final StringBuilder CSVReport;

    public CreateUserReportResponse(Document PDFReport, StringBuilder CSVReport) {
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