package cs.superleague.notification.integration;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.responses.SendPDFEmailNotificationResponse;
import cs.superleague.notifications.requests.SendPDFEmailNotificationRequest;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SendPDFEmailNotificationIntegrationTest {

    @Autowired
    private NotificationServiceImpl notificationService;



    Customer customer;
    UUID customerID;
    byte[] pdfInput;

    @BeforeEach
    void setUp() throws DocumentException {
        customerID = UUID.fromString("44225142-f557-4fb7-9946-f733e0a5d5f5");;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("Hi this is an Odosla test"));
        document.add(paragraph);
        document.close();
        pdfInput = outputStream.toByteArray();
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    @Description("Tests for when the userID is not found in the database")
    @DisplayName("No user found")
    void noUserFoundWithAssociatedID_IntegrationTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, UUID.randomUUID(), "Hi", "message", UserType.SHOPPER);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("User not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for valid PDF being sent")
    @DisplayName("Valid pdf")
    void validPDFBeingSentOverEmail_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, customerID, "Hi", "message", UserType.CUSTOMER);
        SendPDFEmailNotificationResponse response = notificationService.sendPDFEmailNotification(request);
        assertEquals(true, response.isSuccess());
        assertEquals("Email sent successfully", response.getMessage());
    }

}
