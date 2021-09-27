package cs.superleague.notifications.dataclass;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "notificationTable")
public class Notification {

    /* Attributes */
    @Id
    private UUID notificationID;

    private UUID userID;
    private String payload;
    private Calendar createdDateTime;
    private Calendar readDateTime;

    @Enumerated(EnumType.ORDINAL)
    private NotificationType type;

    /* Constructor  */

    public Notification() {
    }

    public Notification(UUID notificationID, UUID userID, String payload, Calendar createdDateTime, Calendar readDateTime, NotificationType type) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.payload = payload;
        this.createdDateTime = createdDateTime;
        this.readDateTime = readDateTime;
        this.type = type;
    }

    public UUID getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(UUID notificationID) {
        this.notificationID = notificationID;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Calendar getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Calendar createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Calendar getReadDateTime() {
        return readDateTime;
    }

    public void setReadDateTime(Calendar readDateTime) {
        this.readDateTime = readDateTime;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
