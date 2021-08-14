package cs.superleague.notification.requests;

import java.util.Calendar;
import java.util.Map;

public class CreateNotificationRequest {
    private final String payLoad;
    private final Calendar createdDateTime;
    private Map<String, String> properties;

    public CreateNotificationRequest(String payLoad, Calendar createdDateTime, Map<String, String> properties) {
        this.payLoad = payLoad;
        this.createdDateTime = createdDateTime;
        this.properties = properties;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public Calendar getCreatedDateTime() {
        return createdDateTime;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}
