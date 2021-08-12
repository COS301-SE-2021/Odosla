package cs.superleague.notification;

import cs.superleague.notification.exceptions.InvalidRequestException;
import cs.superleague.notification.requests.SendDirectEmailNotificationRequest;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.user.dataclass.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SendDirectEmailNotificationUnitTest {
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private JavaMailSender javaMailSender;

    String invalidEmail;
    String validEmail;

    @BeforeEach
    void setUp() {
        invalidEmail = "u19060468tuks.co.za";
        validEmail = "u19060468@tuks.co.za";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests that the request object is being created successfully")
    @DisplayName("Object creation")
    void requestObjectCreation_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Email",validEmail);
        properties.put("Subject","Odosla");
        SendDirectEmailNotificationRequest request = new SendDirectEmailNotificationRequest("message", properties);
        assertEquals(request.getMessage(), "message");
        assertEquals(request.getSubject(), "Odosla");
        assertEquals(request.getEmail(), validEmail);
    }

    @Test
    @Description("Tests when the request object is null that the exception is thrown.")
    @DisplayName("Null request object")
    void requestIsNull_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are null parameters, an exception should be thrown.")
    @DisplayName("Null parameters")
    void requestObjectContainsNullParameters_UnitTest(){
        SendDirectEmailNotificationRequest request1 = new SendDirectEmailNotificationRequest("message", null);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(request1));
        assertEquals("Empty parameters.", thrown1.getMessage());

        Map<String, String> properties2 = new HashMap<>();
        properties2.put("Email",validEmail);
        properties2.put("Subject","Odosla");
        SendDirectEmailNotificationRequest request2 = new SendDirectEmailNotificationRequest(null, properties2);
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(request2));
        assertEquals("Empty parameters.", thrown2.getMessage());

        Map<String, String> properties3 = new HashMap<>();
        properties3.put("Subject","Odosla");
        SendDirectEmailNotificationRequest request3 = new SendDirectEmailNotificationRequest("message", properties3);
        Throwable thrown3 = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(request3));
        assertEquals("Empty parameters.", thrown3.getMessage());

        Map<String, String> properties4 = new HashMap<>();
        properties4.put("Email",validEmail);
        SendDirectEmailNotificationRequest request4 = new SendDirectEmailNotificationRequest("message", properties4);
        Throwable thrown4 = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(request4));
        assertEquals("Empty parameters.", thrown4.getMessage());
    }

    @Test
    @Description("Tests for when the email being used in invalid, exception should be thrown.")
    @DisplayName("Invalid email")
    void invalidEmailInRequestObject_UnitTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Email",invalidEmail);
        properties.put("Subject","Odosla");
        SendDirectEmailNotificationRequest request = new SendDirectEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());

    }
}
