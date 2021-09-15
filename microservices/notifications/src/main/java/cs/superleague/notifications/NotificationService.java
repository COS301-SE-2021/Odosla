package cs.superleague.notifications;

import cs.superleague.notifications.requests.*;
import cs.superleague.notifications.responses.*;
import cs.superleague.notifications.exceptions.InvalidRequestException;

public interface NotificationService {

    CreateNotificationResponse createNotification(CreateNotificationRequest request) throws InvalidRequestException;

    RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request);

    SendDirectEmailNotificationResponse sendDirectEmailNotification(SendDirectEmailNotificationRequest request) throws InvalidRequestException;

    SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) throws InvalidRequestException;

    SendPDFEmailNotificationResponse sendPDFEmailNotification(SendPDFEmailNotificationRequest request) throws InvalidRequestException;
}
