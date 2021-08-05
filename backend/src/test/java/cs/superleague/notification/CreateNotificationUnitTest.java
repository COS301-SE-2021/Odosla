package cs.superleague.notification;

import cs.superleague.notification.repos.NotificationRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateNotificationUnitTest {
    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @Mock
    private NotificationRepo notificationRepo;

}
