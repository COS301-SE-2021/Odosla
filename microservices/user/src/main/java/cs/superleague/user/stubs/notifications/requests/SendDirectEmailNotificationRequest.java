package cs.superleague.user.stubs.notifications.requests;

import java.util.Map;

public class SendDirectEmailNotificationRequest {
    private final String message;
    private final String subject;
    private final String email;
    public SendDirectEmailNotificationRequest(String message, Map<String, String> properties) {
        this.message = message;
        if (properties == null){
            this.subject = null;
            this.email = null;
        }else{
            if(properties.get("Subject") == null){
                this.subject = null;
            }else{
                this.subject = properties.get("Subject");
            }
            if (properties.get("Email") == null){
                this.email = null;
            }else{
                this.email = properties.get("Email");
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }


    public String getEmail() {
        return email;
    }
}
