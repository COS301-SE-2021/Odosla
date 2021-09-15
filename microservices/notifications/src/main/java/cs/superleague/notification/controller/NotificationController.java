package cs.superleague.notification.controller;

import cs.superleague.api.NotificationApi;
//import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.NotificationSendDirectEmailNotificationRequest;
import cs.superleague.models.NotificationSendDirectEmailNotificationResponse;
//import cs.superleague.models.NotificationSendEmailNotificationRequest;
//import cs.superleague.models.NotificationSendEmailNotificationResponse;
import cs.superleague.models.NotificationSendEmailNotificationRequest;
import cs.superleague.models.NotificationSendEmailNotificationResponse;
import cs.superleague.notification.NotificationService;
import cs.superleague.notification.NotificationServiceImpl;
import cs.superleague.notification.repos.NotificationRepo;
import cs.superleague.notification.requests.SendDirectEmailNotificationRequest;
//import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.requests.SendEmailNotificationRequest;
import cs.superleague.notification.responses.SendDirectEmailNotificationResponse;
//import cs.superleague.user.dataclass.Admin;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.notification.responses.SendEmailNotificationResponse;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class NotificationController implements NotificationApi {

    JavaMailSender javaMailSender;
    NotificationRepo notificationRepo;
    NotificationService notificationService;
    RestTemplate restTemplate;
    HttpServletRequest httpServletRequest;

    @Autowired
    public NotificationController(JavaMailSender javaMailSender, NotificationRepo notificationRepo,
                                  NotificationService notificationService, RestTemplate restTemplate,
                                  HttpServletRequest httpServletRequest){
        this.javaMailSender = javaMailSender;
        this.notificationRepo = notificationRepo;
        this.notificationService = notificationService;
        this.restTemplate = restTemplate;
        this.httpServletRequest = httpServletRequest;
    }


    UUID adminID = UUID.fromString("b2cbc86b-ec77-456d-b293-62977d16188a");
    @Override
    public ResponseEntity<NotificationSendEmailNotificationResponse> sendEmailNotification(NotificationSendEmailNotificationRequest body) {
        //Mock data
//        Admin admin= new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
//        adminRepo.save(admin);
        //End of Mock data
        NotificationSendEmailNotificationResponse response = new NotificationSendEmailNotificationResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            SendEmailNotificationRequest request = new SendEmailNotificationRequest(body.getMessage(), body.getProperties());
            SendEmailNotificationResponse sendEmailNotificationResponse = notificationService.sendEmailNotification(request);
            try{
                response.setSuccess(sendEmailNotificationResponse.getSuccessMessage());
                response.setResponseMessage(sendEmailNotificationResponse.getResponseMessage());
            }catch (Exception e){
                response.setSuccess(false);
                response.setResponseMessage(e.getMessage());
            }
        }catch (Exception e){
            response.setSuccess(false);
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        //adminRepo.deleteAll();
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<NotificationSendDirectEmailNotificationResponse> sendDirectEmailNotification(NotificationSendDirectEmailNotificationRequest body){
        NotificationSendDirectEmailNotificationResponse response = new NotificationSendDirectEmailNotificationResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            SendDirectEmailNotificationRequest request = new SendDirectEmailNotificationRequest(body.getMessage(), body.getProperties());
            SendDirectEmailNotificationResponse sendDirectEmailNotificationResponse = notificationService.sendDirectEmailNotification(request);
            try{
                response.setSuccess(sendDirectEmailNotificationResponse.isSuccess());
                response.setResponseMessage(sendDirectEmailNotificationResponse.getResponseMessage());
            }catch (Exception e){
                response.setSuccess(false);
                response.setResponseMessage(e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e){
            response.setSuccess(false);
            response.setResponseMessage(e.getMessage());
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
