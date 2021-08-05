package cs.superleague.notification.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

public class SendEmailNotificationRequest {

    private final String message;
    private final UUID userID;
    private final String subject;
    private final String type;

    public SendEmailNotificationRequest(String message, Map<String, String> properties) {
        this.message = message;
        this.type = properties.get("Type");
        this.subject = properties.get("Subject");
        this.userID = UUID.fromString(properties.get("UUID"));
    }

    public String getMessage() {
        return message;
    }

    public UUID getUserID() {
        return userID;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }
}
