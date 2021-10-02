package cs.superleague.notifications.rabbit;

import cs.superleague.notifications.NotificationService;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.requests.SendDirectEmailNotificationRequest;
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
            Object o = in.readObject();

            if (o instanceof SendDirectEmailNotificationRequest) {
                request = (SendDirectEmailNotificationRequest) o;
                notificationService.sendDirectEmailNotification(request);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
