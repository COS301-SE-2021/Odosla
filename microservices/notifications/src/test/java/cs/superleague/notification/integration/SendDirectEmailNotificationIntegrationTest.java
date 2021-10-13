package cs.superleague.notification.integration;

import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.requests.SendDirectEmailNotificationRequest;
import cs.superleague.notifications.responses.SendDirectEmailNotificationResponse;
import cs.superleague.notifications.NotificationServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SendDirectEmailNotificationIntegrationTest {
    @Autowired
    private NotificationServiceImpl notificationService;

    String validEmail;
    String invalidEmail;

    @BeforeEach
    void setUp() {
        invalidEmail = "u19060468tuks.co.za";
        validEmail = "u19060468@tuks.co.za";
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when the email in invalid, an exception should be thrown.")
    @DisplayName("Invalid email")
    void invalidEmailPassedInRequestObject_IntegrationTest(){
        Map<String, String> properties = new HashMap<>();
        properties.put("Email",invalidEmail);
        properties.put("Subject","Odosla");
        SendDirectEmailNotificationRequest request = new SendDirectEmailNotificationRequest("message", properties);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendDirectEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when a valid email address is passed in, email should be sent.")
    @DisplayName("Valid email")
    void validEmailPassedInRequestObjectEmailSent_IntegrationTest() throws InvalidRequestException{
        Map<String, String> properties = new HashMap<>();
        properties.put("Email",validEmail);
        properties.put("Subject","Odosla");
        SendDirectEmailNotificationRequest request = new SendDirectEmailNotificationRequest("message", properties);
        SendDirectEmailNotificationResponse response = notificationService.sendDirectEmailNotification(request);
        assertEquals(true, response.isSuccess());
        assertEquals("Email sent to u19060468@tuks.co.za - Subject: Odosla - Content: message", response.getResponseMessage());
    }
}
