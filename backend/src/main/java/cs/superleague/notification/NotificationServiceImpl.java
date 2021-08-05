package cs.superleague.notification;

import cs.superleague.notification.dataclass.Notification;
import cs.superleague.notification.dataclass.NotificationType;
import cs.superleague.notification.repos.NotificationRepo;
import cs.superleague.notification.requests.CreateNotificationRequest;
import cs.superleague.notification.requests.RetrieveNotificationRequest;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.CreateNotificationResponse;
import cs.superleague.notification.responses.RetrieveNotificationResponse;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import cs.superleague.payment.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("NotificationServiceImpl")
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final NotificationRepo notificationRepo;

    @Autowired
    public NotificationServiceImpl(JavaMailSender javaMailSender, NotificationRepo notificationRepo) {
        this.javaMailSender = javaMailSender;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public CreateNotificationResponse createNotification(CreateNotificationRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getPayLoad() == null || request.getCreatedDateTime() == null || request.getProperties() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        if (request.getPayLoad().equals("") || request.getCreatedDateTime().equals("") || request.getProperties().equals("")){
            throw new InvalidRequestException("Empty parameters.");
        }
        NotificationType notificationType = null;
        switch (request.getProperties().get("NotificationType").toLowerCase()){
            case "delivery":
                notificationType = NotificationType.DELIVERY;
                break;
            case "order":
                notificationType = NotificationType.ORDER;
                break;
            case "store":
                notificationType = NotificationType.STORE;
                break;
            case "verification":
                notificationType = NotificationType.VERIFICATION;
                break;
            case "invoice":
                notificationType = NotificationType.INVOICE;
                break;
            case "resetpassword":
                notificationType = NotificationType.RESETPASSWORD;
                break;
        }
        if (notificationType == null){
            throw new InvalidRequestException("Invalid notification type.");
        }
        UUID notificationID = UUID.randomUUID();
        while(notificationRepo.findById(notificationID).isPresent()){
            notificationID = UUID.randomUUID();
        }
        Notification notification = new Notification(
                notificationID,
                UUID.fromString(request.getProperties().get("userID")),
                request.getPayLoad(),
                request.getCreatedDateTime(),
                null,
                notificationType);
        notificationRepo.save(notification);
        return new CreateNotificationResponse("Notification successfully created.");
    }

    @Override
    public RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request) {
        return null;
    }

    @Override
    public SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) throws InvalidRequestException {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getEmail() == null){
            throw new InvalidRequestException("Null recipient email.");
        }
        if(request.getEmail().equals("")){
            throw new InvalidRequestException("Empty recipient email.");
        }
        Matcher matcher = pattern.matcher(request.getEmail());
        if(!matcher.matches()){
            throw new InvalidRequestException("Invalid recipient email address.");
        }
        if(request.getSubject() == null || request.getSubject().equals("") || request.getMessage() == null || request.getMessage().equals("")){
            throw new InvalidRequestException("Empty parameters.");
        }
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", request.getType());
        properties.put("Subject", request.getSubject());
        properties.put("userID", request.getUserID().toString());
        properties.put("ContactDetails", request.getEmail());
        CreateNotificationRequest request1 = new CreateNotificationRequest(request.getMessage(), Calendar.getInstance(), properties);
        createNotification(request1);
        sendEmail(request.getEmail(), request.getSubject(), request.getMessage());
        return new SendEmailNotificationResponse(true, String.format("Email sent to %s - Subject: %s - Content: %s",request.getEmail(), request.getSubject(), request.getMessage()));
    }

    public void sendEmail(String email, String Subject, String Body) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(Subject);
        msg.setText(Body);

        javaMailSender.send(msg);
    }
}
