//package cs.superleague.notification.requests;
//
//import cs.superleague.user.dataclass.UserType;
//
//import java.util.UUID;
//
//public class SendPDFEmailNotificationRequest {
//    private byte[] PDF;
//    private UUID userID;
//    private String subject;
//    private String message;
//    private UserType userType;
//
//    public SendPDFEmailNotificationRequest(byte[] PDF, UUID userID, String subject, String message, UserType userType) {
//        this.PDF = PDF;
//        this.userID = userID;
//        this.subject = subject;
//        this.message = message;
//        this.userType = userType;
//    }
//
//    public UserType getUserType() {
//        return userType;
//    }
//
//    public void setUserType(UserType userType) {
//        this.userType = userType;
//    }
//
//    public byte[] getPDF() {
//        return PDF;
//    }
//
//    public void setPDF(byte[] PDF) {
//        this.PDF = PDF;
//    }
//
//    public UUID getUserID() {
//        return userID;
//    }
//
//    public void setUserID(UUID userID) {
//        this.userID = userID;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//}
