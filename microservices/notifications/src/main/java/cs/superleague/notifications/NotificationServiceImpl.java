package cs.superleague.notifications;

import cs.superleague.notifications.dataclass.Notification;
import cs.superleague.notifications.dataclass.NotificationType;
import cs.superleague.notifications.repos.NotificationRepo;
import cs.superleague.notifications.requests.*;
import cs.superleague.notifications.responses.*;
import cs.superleague.notifications.exceptions.InvalidRequestException;
//import cs.superleague.user.dataclass.*;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.responses.GetAdminByUUIDResponse;
import cs.superleague.user.responses.GetCustomerByUUIDResponse;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("notificationServiceImpl")
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;
    private final NotificationRepo notificationRepo;
    private final RestTemplate restTemplate;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    @Autowired
    public NotificationServiceImpl(JavaMailSender javaMailSender, NotificationRepo notificationRepo, RestTemplate restTemplate){
        this.javaMailSender = javaMailSender;
        this.notificationRepo = notificationRepo;
        this.restTemplate = restTemplate;
    }

    @Override
    public CreateNotificationResponse createNotification(CreateNotificationRequest request) throws InvalidRequestException {
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getPayLoad() == null || request.getCreatedDateTime() == null || request.getProperties() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        if (request.getPayLoad().equals("") || request.getCreatedDateTime().equals("") || request.getProperties().equals("")){
            throw new InvalidRequestException("Empty parameters.");
        }
        NotificationType notificationType = null;
        if(request.getProperties().get("NotificationType") == null){
            throw new InvalidRequestException("Null notification type.");
        }
        switch (request.getProperties().get("NotificationType").toLowerCase()){
            case "delivery":
                notificationType = NotificationType.DELIVERY;
                break;
            case "order":
                notificationType = NotificationType.ORDER;
                break;
            case "store":
                notificationType = NotificationType.STORE;
                break;
            case "verification":
                notificationType = NotificationType.VERIFICATION;
                break;
            case "invoice":
                notificationType = NotificationType.INVOICE;
                break;
            case "resetpassword":
                notificationType = NotificationType.RESETPASSWORD;
                break;
        }
        if (notificationType == null){
            throw new InvalidRequestException("Invalid notification type.");
        }
        if (request.getProperties().get("UserID") == null || request.getProperties().get("UserID").equals("")){
            throw new InvalidRequestException("Null UserID.");
        }
        UserType userType = null;
        if (request.getProperties().get("UserType") != null){
            switch (request.getProperties().get("UserType").toLowerCase()){
                case "admin":
                    userType = UserType.ADMIN;
                    break;
                case "customer":
                    userType = UserType.CUSTOMER;
                    break;
                case "driver":
                    userType = UserType.DRIVER;
                    break;
                case "shopper":
                    userType = UserType.SHOPPER;
                    break;
                default:
                    userType = null;
                    break;
            }
        }else{
            userType = null;
        }
        if (userType == null){
            throw new InvalidRequestException("Invalid UserType.");
        }
        if (userType == UserType.ADMIN){

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", request.getProperties().get("UserID"));
            ResponseEntity<GetAdminByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getAdminByUUID", parts, GetAdminByUUIDResponse.class);
            Admin admin = useCaseResponseEntity.getBody().getAdmin();
            if (admin == null){
                throw new InvalidRequestException("User does not exist in database.");
            }
        } else if (userType == UserType.CUSTOMER){

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", request.getProperties().get("UserID"));
            ResponseEntity<GetCustomerByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getCustomerByUUID", parts, GetCustomerByUUIDResponse.class);
            Customer customer = useCaseResponseEntity.getBody().getCustomer();
            if (customer == null){
                throw new InvalidRequestException("User does not exist in database.");
            }
        } else if (userType == UserType.DRIVER){

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", request.getProperties().get("UserID"));
            ResponseEntity<GetDriverByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getDriverByUUID", parts, GetDriverByUUIDResponse.class);
            Driver driver = useCaseResponseEntity.getBody().getDriver();
            if (driver == null){
                throw new InvalidRequestException("User does not exist in database.");
            }
        } else if (userType == UserType.SHOPPER){

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", request.getProperties().get("UserID"));
            ResponseEntity<GetShopperByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getShopperByUUID", parts, GetShopperByUUIDResponse.class);
            Shopper shopper = useCaseResponseEntity.getBody().getShopper();
            if (shopper == null){
                throw new InvalidRequestException("User does not exist in database.");
            }
        }

        UUID notificationID = UUID.randomUUID();
        while(notificationRepo.findById(notificationID).isPresent()){
            notificationID = UUID.randomUUID();
        }
        Notification notification = new Notification(
                notificationID,
                UUID.fromString(request.getProperties().get("UserID")),
                request.getPayLoad(),
                request.getCreatedDateTime(),
                null,
                notificationType);
        notificationRepo.save(notification);
        return new CreateNotificationResponse("Notification successfully created.");
    }

    @Override
    public RetrieveNotificationResponse retrieveNotification(RetrieveNotificationRequest request) {
        return null;
    }

    @Override
    public SendDirectEmailNotificationResponse sendDirectEmailNotification(SendDirectEmailNotificationRequest request) throws InvalidRequestException {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getSubject() == null || request.getSubject().equals("") || request.getMessage() == null || request.getMessage().equals("") || request.getEmail() == null || request.getEmail().equals("")){
            throw new InvalidRequestException("Empty parameters.");
        }
        Matcher matcher = pattern.matcher(request.getEmail());
        if (!matcher.matches()){
            throw new InvalidRequestException("Invalid recipient email address.");
        }
        sendEmail(request.getEmail(), request.getSubject(), request.getMessage());
        SendDirectEmailNotificationResponse response = new SendDirectEmailNotificationResponse(true, String.format("Email sent to %s - Subject: %s - Content: %s",request.getEmail(), request.getSubject(), request.getMessage()));
        return response;
    }

    @Override
    public SendEmailNotificationResponse sendEmailNotification(SendEmailNotificationRequest request) throws InvalidRequestException {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        if(request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if(request.getUserID() == null || request.getUserID().equals("")){
            throw new InvalidRequestException("Null userID.");
        }
        if (request.getUserType() == null){
            throw new InvalidRequestException("Invalid UserType.");
        }
        String email = getEmail(request.getUserType(), request.getUserID());
        if (email == ""){
            throw new InvalidRequestException("User not found in database.");
        }
        if (email == null){
            throw new InvalidRequestException("Null recipient email address.");
        }
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new InvalidRequestException("Invalid recipient email address.");
        }
        if(request.getSubject() == null || request.getSubject().equals("") || request.getMessage() == null || request.getMessage().equals("")){
            throw new InvalidRequestException("Empty parameters.");
        }
        Map<String, String> properties = new HashMap<>();
        properties.put("NotificationType", request.getType());
        properties.put("Subject", request.getSubject());
        properties.put("UserID", request.getUserID().toString());
        properties.put("UserType", request.getUserType().toString());
        properties.put("ContactDetails", email);
        CreateNotificationRequest request1 = new CreateNotificationRequest(request.getMessage(), Calendar.getInstance(), properties);
        createNotification(request1);
        sendEmail(email, request.getSubject(), request.getMessage());
        return new SendEmailNotificationResponse(true, String.format("Email sent to %s - Subject: %s - Content: %s",email, request.getSubject(), request.getMessage()));
    }

    @Override
    public SendPDFEmailNotificationResponse sendPDFEmailNotification(SendPDFEmailNotificationRequest request) throws InvalidRequestException {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getPDF() == null || request.getUserID() == null || request.getMessage() == null || request.getSubject() == null || request.getUserType() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        String email = getEmail(request.getUserType(), request.getUserID());
        if (email == ""){
            throw new InvalidRequestException("User not found in database.");
        }
        if (email == null){
            throw new InvalidRequestException("Null recipient email address.");
        }
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            throw new InvalidRequestException("Invalid recipient email address.");
        }
        if(request.getSubject().equals("") || request.getMessage().equals("")){
            throw new InvalidRequestException("Empty parameters.");
        }
        SendPDFEmailNotificationResponse response;
        try{
            sendEmailWithPDF(email, request.getSubject(), request.getMessage(), request.getPDF());
            response = new SendPDFEmailNotificationResponse(true, "Email sent successfully");
        }catch(Exception e){
             response = new SendPDFEmailNotificationResponse(false, e.getMessage());
        }
        return response;
    }

    public void sendEmail(String email, String Subject, String Body) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(Subject);
        msg.setText(Body);

        javaMailSender.send(msg);
    }

    public void sendEmailWithPDF(String email, String subject, String body, byte[] pdf){

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        Session session = Session.getDefaultInstance(properties, null);
        try {
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(body);

            //construct the pdf body part
            DataSource dataSource = new ByteArrayDataSource(pdf, "application/pdf");
            MimeBodyPart pdfBodyPart = new MimeBodyPart();
            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
            pdfBodyPart.setFileName("Odosla.pdf");

            //construct the mime multi part
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(textBodyPart);
            mimeMultipart.addBodyPart(pdfBodyPart);

            //create the sender/recipient addresses
            InternetAddress iaSender = new InternetAddress("superleague301@gmail.com");
            InternetAddress iaRecipient = new InternetAddress(email);

            //construct the mime message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSender(iaSender);
            mimeMessage.setSubject(subject);
            mimeMessage.setRecipient(Message.RecipientType.TO, iaRecipient);
            mimeMessage.setContent(mimeMultipart);

            //send off the email
            javaMailSender.send(mimeMessage);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getEmail(UserType userType, UUID userID){
        String email = "";
        if (userType == UserType.ADMIN){
            //Admin admin = adminRepo.findById(request.getUserID()).orElse(null);

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", userID);
            ResponseEntity<GetAdminByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getAdminByUUID", parts, GetAdminByUUIDResponse.class);
            Admin admin = useCaseResponseEntity.getBody().getAdmin();
            if (admin == null){
                return "";
            }
            email = admin.getEmail();
        } else if (userType == UserType.CUSTOMER){
            //Customer customer = customerRepo.findById(request.getUserID()).orElse(null);

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", userID);
            ResponseEntity<GetCustomerByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getCustomerByUUID", parts, GetCustomerByUUIDResponse.class);
            Customer customer = useCaseResponseEntity.getBody().getCustomer();
            if (customer == null){
                return "";
            }
            email = customer.getEmail();
        }else if (userType == UserType.DRIVER){
            //Driver driver = driverRepo.findById(request.getUserID()).orElse(null);

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", userID);
            ResponseEntity<GetDriverByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getDriverByUUID", parts, GetDriverByUUIDResponse.class);
            Driver driver = useCaseResponseEntity.getBody().getDriver();
            if (driver == null){
                return "";
            }
            email = driver.getEmail();
        }else if (userType == UserType.SHOPPER) {
            //Shopper shopper = shopperRepo.findById(request.getUserID()).orElse(null);
            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", userID);
            ResponseEntity<GetShopperByUUIDResponse> useCaseResponseEntity = restTemplate.postForEntity("http://"+userHost+":"+userPort+"/user/getShopperByUUID", parts, GetShopperByUUIDResponse.class);
            Shopper shopper = useCaseResponseEntity.getBody().getShopper();
            if (shopper == null) {
                return "";
            }
            email = shopper.getEmail();
        }
        return email;
    }
}
