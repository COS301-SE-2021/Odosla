package cs.superleague.notification;

import cs.superleague.notification.requests.CreateNotificationRequest;
import cs.superleague.notification.requests.RetrieveNotificationRequest;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.CreateNotificationResponse;
import cs.superleague.notification.responses.RetrieveNotificationResponse;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import cs.superleague.notification.exceptions.InvalidRequestException;

public interface NotificationService {

    CreateNotificationResponse createNotification(CreateNotificationRequest request) throws InvalidRequestException;

    RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request);

    SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) throws InvalidRequestException;
}
