package cs.superleague.notifications.requests;//package cs.superleague.notification.requests;

import cs.superleague.user.dataclass.UserType;

import java.util.Map;
import java.util.UUID;

public class SendEmailNotificationRequest {

    private final String message;
    private final UUID userID;
    private final String subject;
    private final String type;
    private final UserType userType;

    public SendEmailNotificationRequest(String message, Map<String, String> properties) {
        this.message = message;
        if (properties == null) {
            this.type = null;
            this.subject = null;
            this.userType = null;
            this.userID = null;
        } else {
            if (properties.get("Type") == null) {
                this.type = null;
            } else {
                this.type = properties.get("Type");
            }
            if (properties.get("Subject") == null) {
                this.subject = null;
            } else {
                this.subject = properties.get("Subject");
            }
            if (properties.get("UserType") != null) {
                switch (properties.get("UserType").toLowerCase()) {
                    case "admin":
                        this.userType = UserType.ADMIN;
                        break;
                    case "customer":
                        this.userType = UserType.CUSTOMER;
                        break;
                    case "driver":
                        this.userType = UserType.DRIVER;
                        break;
                    case "shopper":
                        this.userType = UserType.SHOPPER;
                        break;
                    default:
                        this.userType = null;
                        break;
                }
            } else {
                this.userType = null;
            }
            if (properties.get("UserID") != null && !properties.get("UserID").equals("")) {
                this.userID = UUID.fromString(properties.get("UserID"));
            } else {
                this.userID = null;
            }
        }

    }

    public UserType getUserType() {
        return userType;
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
