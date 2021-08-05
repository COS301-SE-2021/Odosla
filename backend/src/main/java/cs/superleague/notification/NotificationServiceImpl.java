package cs.superleague.notification;

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

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
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
        return null;
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
        sendEmail(request.getEmail(), request.getSubject(), request.getMessage());

        return null;
    }

    public void sendEmail(String email, String Subject, String Body) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(Subject);
        msg.setText(Body);

        javaMailSender.send(msg);
    }
}
