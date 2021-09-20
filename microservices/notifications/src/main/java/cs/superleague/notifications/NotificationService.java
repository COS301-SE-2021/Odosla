package cs.superleague.notifications;

import cs.superleague.notifications.requests.*;
import cs.superleague.notifications.responses.*;
import cs.superleague.notifications.exceptions.InvalidRequestException;

import java.net.URISyntaxException;

public interface NotificationService {

    CreateNotificationResponse createNotification(CreateNotificationRequest request) throws InvalidRequestException, URISyntaxException;

    RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request);

    SendDirectEmailNotificationResponse sendDirectEmailNotification(SendDirectEmailNotificationRequest request) throws InvalidRequestException;

    SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) throws InvalidRequestException, URISyntaxException;

    SendPDFEmailNotificationResponse sendPDFEmailNotification(SendPDFEmailNotificationRequest request) throws InvalidRequestException, URISyntaxException;
}
