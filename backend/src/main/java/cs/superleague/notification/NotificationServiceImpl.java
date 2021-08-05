package cs.superleague.notification;

import cs.superleague.notification.repos.NotificationRepo;
import cs.superleague.notification.requests.CreateNotificationRequest;
import cs.superleague.notification.requests.RetrieveNotificationRequest;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.CreateNotificationResponse;
import cs.superleague.notification.responses.RetrieveNotificationResponse;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
    public CreateNotificationResponse createNotification(CreateNotificationRequest request) {
        return null;
    }

    @Override
    public RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request) {
        return null;
    }

    @Override
    public SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) {
        return null;
    }


}
