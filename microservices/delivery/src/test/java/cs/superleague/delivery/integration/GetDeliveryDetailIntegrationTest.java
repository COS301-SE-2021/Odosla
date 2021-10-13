package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryDetailRequest;
import cs.superleague.delivery.responses.GetDeliveryDetailResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.requests.SaveDriverToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetDeliveryDetailIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    DeliveryRepo deliveryRepo;

    @Autowired
    DeliveryDetailRepo deliveryDetailRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    Admin admin;
    UUID adminID;
    UUID deliveryID;
    Delivery delivery;
    UUID orderID;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID, storeID;
    DeliveryStatus status;
    Calendar time;
    DeliveryDetail deliveryDetail1;
    DeliveryDetail deliveryDetail2;
    Driver driver;

    @BeforeEach
    void setUp(){
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        deliveryID = UUID.fromString("e6732473-7e91-4949-8811-d71e6fb6255d");
        orderID = UUID.fromString("54287c26-ae7d-4137-afbe-353789533a47");
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        storeID = UUID.fromString("0b3837de-a2d7-4fdc-a819-63ebd6dea1aa");
        adminID=UUID.fromString("91443e06-46b0-48b9-a2d4-f36799eb092d");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        admin = new Admin("John", "Doe", "u14254922@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, 0.0);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, DeliveryStatus.CollectedByDriver, "detail1");
        deliveryDetail2 = new DeliveryDetail(UUID.randomUUID(), Calendar.getInstance(), DeliveryStatus.DeliveringToCustomer, "detail2");
        deliveryRepo.save(delivery);
        deliveryDetailRepo.save(deliveryDetail1);
        deliveryDetailRepo.save(deliveryDetail2);

        driver = new Driver();
        driver.setDriverID(UUID.fromString("5c7c3a02-9ff9-4efc-8950-eecbf9b4a65b"));
        driver.setEmail("u19060468@tuks.co.za");
        driver.setAccountType(UserType.DRIVER);

        String jwt = jwtUtil.generateJWTTokenAdmin(admin);

        System.out.println(jwt);

        Header header = new BasicHeader("Authorization", jwt);
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        jwt = jwt.replace(HEADER,"");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");
        System.out.println(userType);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);

//        SaveAdminToRepoRequest saveAdminToRepoRequest = new SaveAdminToRepoRequest(admin);
//        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveAdminToRepoTest", saveAdminToRepoRequest);

    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    void runSetUp(){

    }

    @Test
    @Description("Tests for when the user looking for the data is not an admin.")
    @DisplayName("Invalid adminID")
    void invalidAdminIDInRequestObject_IntegrationTest(){
        admin.setAdminID(UUID.randomUUID());
        admin.setEmail("hello@gmail.com");
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenAdmin(admin);
        jwt = jwt.replace("Bearer ","");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDetail(request));
        assertEquals("User is not an admin.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are no details for the delivery or delivery does not exist.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDetail(request));
        assertEquals("No details found for this delivery.", thrown.getMessage());
    }

    @Test
    @Description("Tests for delivery details are found.")
    @DisplayName("Delivery details returned")
    void detailsReturnedSuccessfully_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(deliveryID);
        GetDeliveryDetailResponse response = deliveryService.getDeliveryDetail(request);
        assertEquals(response.getMessage(), "Details successfully found.");
        assertEquals(response.getDetail().get(0).getDetail(), "detail1");
    }
}
