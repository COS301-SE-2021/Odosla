package cs.superleague.notification;

import cs.superleague.notification.requests.*;
import cs.superleague.notification.responses.*;
import cs.superleague.notification.exceptions.InvalidRequestException;

public interface NotificationService {

    CreateNotificationResponse createNotification(CreateNotificationRequest request) throws InvalidRequestException;

    RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request);

    SendDirectEmailNotificationResponse sendDirectEmailNotification(SendDirectEmailNotificationRequest request) throws InvalidRequestException;

    SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) throws InvalidRequestException;

    SendPDFEmailNotificationResponse sendPDFEmailNotification(SendPDFEmailNotificationRequest request) throws InvalidRequestException;
}
