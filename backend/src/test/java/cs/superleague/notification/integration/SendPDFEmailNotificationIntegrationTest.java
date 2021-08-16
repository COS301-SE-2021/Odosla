package cs.superleague.notification.integration;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.notification.NotificationServiceImpl;
import cs.superleague.notification.exceptions.InvalidRequestException;
import cs.superleague.notification.requests.SendPDFEmailNotificationRequest;
import cs.superleague.notification.responses.SendPDFEmailNotificationResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SendPDFEmailNotificationIntegrationTest {

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    DriverRepo driverRepo;

    Customer invalidEmailCustomer;

    Customer customer;
    UUID customerID;
    byte[] pdfInput;

    @BeforeEach
    void setUp() throws DocumentException {
        customerID = UUID.randomUUID();
        customer = new Customer();
        customer.setEmail("seamus@ipmc.co.uk");
        customer.setCustomerID(customerID);
        customerRepo.save(customer);
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
        customerRepo.deleteAll();
    }

    @Test
    @Description("Tests for valid PDF being sent")
    @DisplayName("Valid pdf")
    void validPDFBeingSentOverEmail_IntegrationTest() throws InvalidRequestException {
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, customerID, "Hi", "message", UserType.CUSTOMER);
        SendPDFEmailNotificationResponse response = notificationService.sendPDFEmailNotification(request);
        assertEquals(true, response.isSuccess());
        assertEquals("Email sent successfully", response.getMessage());
    }

}
