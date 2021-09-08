package cs.superleague.analytics.responses;

import com.itextpdf.text.Document;

import java.util.Date;

public class CreateFinancialReportResponse {

    private final boolean success;
    private final String message;
    private final Date timestamp;
    private final byte[] document;
    private final StringBuilder stringBuilder;

    public CreateFinancialReportResponse(boolean success, String message, Date timestamp, byte[] document, StringBuilder stringBuilder) {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
        this.document = document;
        this.stringBuilder = stringBuilder;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public byte[] getDocument() {
        return document;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}