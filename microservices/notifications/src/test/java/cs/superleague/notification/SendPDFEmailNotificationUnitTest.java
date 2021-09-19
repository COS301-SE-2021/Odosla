package cs.superleague.notification;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.notifications.NotificationServiceImpl;
import cs.superleague.notifications.exceptions.InvalidRequestException;
import cs.superleague.notifications.repos.NotificationRepo;
import cs.superleague.notifications.requests.SendPDFEmailNotificationRequest;
import cs.superleague.user.dataclass.*;

import cs.superleague.user.responses.GetAdminByUUIDResponse;
import cs.superleague.user.responses.GetCustomerByUUIDResponse;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SendPDFEmailNotificationUnitTest {
    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private RestTemplate restTemplate;

    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    UUID adminID;
    UUID customerID;
    UUID driverID;
    UUID shopperID;
    UUID adminID2;
    Admin adminIncorrectEmail;
    Customer customerIncorrectEmail;
    Driver driverIncorrectEmail;
    Shopper shopperIncorrectEmail;
    Admin adminCorrectEmail;
    byte[] pdfInput;

    @BeforeEach
    void setUp() throws DocumentException {
        adminID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        adminIncorrectEmail = new Admin("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        customerIncorrectEmail = new Customer("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.CUSTOMER, customerID);
        driverIncorrectEmail = new Driver("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        shopperIncorrectEmail = new Shopper("John", "Doe", "u19060468tuks.co.za", "0743149813", "Hello123", "123", UserType.SHOPPER, shopperID);
        adminCorrectEmail = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph();
        paragraph.add(new Chunk("Hi this is Odosla"));
        document.add(paragraph);
        document.close();
        pdfInput = outputStream.toByteArray();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests that the request object is being created successfully")
    @DisplayName("Object creation")
    void requestObjectCreation_UnitTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, "Odosla", "message", UserType.ADMIN);
        assertEquals(request.getMessage(), "message");
        assertEquals(request.getSubject(), "Odosla");
        assertEquals(request.getUserID(), adminID);
        assertEquals(request.getUserType(), UserType.ADMIN);
    }

    @Test
    @Description("Tests when the request object is null that the exception is thrown")
    @DisplayName("Null request object")
    void requestIsNull_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the pdf is null, exception should be thrown.")
    @DisplayName("Null pdf in request object")
    void requestObjectHasNullPDFObject_UnitTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(null, adminID, "Odosla", "message", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has null userID")
    @DisplayName("Null userID in request object")
    void requestObjectHasNullUserID_UnitTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, null, "Odosla", "message", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has null subject")
    @DisplayName("Null subject in request object")
    void requestObjectHasNullSubject_UnitTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, null, "message", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has null message")
    @DisplayName("Null message in request object")
    void requestObjectHasNullMessage_UnitTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, "Odosla", null, UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has an null UserType")
    @DisplayName("Null UserType in request object")
    void requestObjectHasNullUserType_UnitTest(){
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, "Odosla", "message", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID not found on the database")
    @DisplayName("Invalid userID in request object")
    void requestObjectInvalidUserID_UnitTest() throws URISyntaxException {
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(null, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        UUID randomID = UUID.randomUUID();
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", randomID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, randomID, "Odosla", "message", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("User not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in admin repo")
    @DisplayName("Invalid email in admin user")
    void requestObjectInvalidEmailAdminRepo_UnitTest() throws URISyntaxException {
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminIncorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminIncorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, "Odosla", "message", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in customer repo")
    @DisplayName("Invalid email in customer user")
    void requestObjectInvalidEmailCustomerRepo_UnitTest() throws URISyntaxException {
       // when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customerIncorrectEmail));
        GetCustomerByUUIDResponse getCustomerByUUIDResponse = new GetCustomerByUUIDResponse(customerIncorrectEmail, new Date(), "");
        ResponseEntity<GetCustomerByUUIDResponse> responseEntity = new ResponseEntity<>(getCustomerByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getCustomerByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", customerID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetCustomerByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, customerID, "Odosla", "message", UserType.CUSTOMER);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in driver repo")
    @DisplayName("Invalid email in driver user")
    void requestObjectInvalidEmailDriverRepo_UnitTest() throws URISyntaxException {
        //when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driverIncorrectEmail));
        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driverIncorrectEmail, new Date(), "");
        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getDriverByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", driverID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetDriverByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, driverID, "Odosla", "message", UserType.DRIVER);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the request object has userID with an invalid email in shopper repo")
    @DisplayName("Invalid email in shopper user")
    void requestObjectInvalidEmailShopperRepo_UnitTest() throws URISyntaxException {
        //when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopperIncorrectEmail));
        GetShopperByUUIDResponse getShopperByUUIDResponse = new GetShopperByUUIDResponse(shopperIncorrectEmail, new Date(), "");
        ResponseEntity<GetShopperByUUIDResponse> responseEntity = new ResponseEntity<>(getShopperByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getShopperByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", shopperID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetShopperByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, shopperID, "Odosla", "message", UserType.SHOPPER);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Invalid recipient email address.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the subject is empty, should throw an exception.")
    @DisplayName("Empty subject")
    void requestObjectEmptySubject_UnitTest() throws URISyntaxException {
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminCorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, "", "message", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests when the message is empty, should throw an exception.")
    @DisplayName("Empty message")
    void requestObjectEmptyMessage_UnitTest() throws URISyntaxException {
        //when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(adminCorrectEmail));
        GetAdminByUUIDResponse getAdminByUUIDResponse = new GetAdminByUUIDResponse(adminCorrectEmail, new Date(), "");
        ResponseEntity<GetAdminByUUIDResponse> responseEntity = new ResponseEntity<>(getAdminByUUIDResponse, HttpStatus.OK);
        String uri = "http://"+userHost+":"+userPort+"/user/getAdminByUUID";
        URI uri1 = new URI(uri);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("userID", adminID.toString());
        when(restTemplate.postForEntity(uri1, parts, GetAdminByUUIDResponse.class)).thenReturn(responseEntity);
        SendPDFEmailNotificationRequest request = new SendPDFEmailNotificationRequest(pdfInput, adminID, "Odosla", "", UserType.ADMIN);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->notificationService.sendPDFEmailNotification(request));
        assertEquals("Empty parameters.", thrown.getMessage());
    }
}
