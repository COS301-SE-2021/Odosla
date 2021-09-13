package cs.superleague.notification.rabbit;

import cs.superleague.notification.NotificationService;
import cs.superleague.notification.exceptions.InvalidRequestException;
import cs.superleague.notification.requests.SendDirectEmailNotificationRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Component
public class NotificationListener implements MessageListener {
    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void onMessage(Message message) {
        SendDirectEmailNotificationRequest request = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            request = (SendDirectEmailNotificationRequest) in.readObject();
            notificationService.sendDirectEmailNotification(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
