package notification;

import java.util.Calendar;
import java.util.Map;

public class Notification {
    private String payload;
    private Calendar readDateTime;
    private Calendar createdDateTime;
    private Map<String, String> map;
    private NotificationType type;

    public Notification(String payload, Calendar readDateTime, Calendar createdDateTime, Map<String, String> map, NotificationType type) {
        this.payload = payload;
        this.readDateTime = readDateTime;
        this.createdDateTime = createdDateTime;
        this.map = map;
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Calendar getReadDateTime() {
        return readDateTime;
    }

    public void setReadDateTime(Calendar readDateTime) {
        this.readDateTime = readDateTime;
    }

    public Calendar getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Calendar createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
