package cs.superleague.analytics.responses;


import java.util.Date;

public class CreateMonthlyReportResponse {

    private final boolean success;
    private final String message;
    private final Date timestamp;
    private final byte[] document;
    private final String stringBuilder;

    public CreateMonthlyReportResponse(boolean success, String message, Date timestamp, byte[] document,
                                       String stringBuilder) {
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

    public String getStringBuilder() {
        return stringBuilder;
    }
}