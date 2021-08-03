package cs.superleague.notification;

import cs.superleague.notification.requests.CreateNotificationRequest;
import cs.superleague.notification.requests.RetrieveNotificationRequest;
import cs.superleague.notification.responses.CreateNotificationResponse;
import cs.superleague.notification.responses.RetrieveNotificationResponse;

public interface NotificationService {

    CreateNotificationResponse createNotification(CreateNotificationRequest request);

    RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request);
}
