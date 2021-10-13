package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.AssignDriverToDeliveryRequest;
import cs.superleague.delivery.requests.GetDeliveryDriverByOrderIDRequest;
import cs.superleague.delivery.responses.AssignDriverToDeliveryResponse;
import cs.superleague.delivery.responses.GetDeliveryDriverByOrderIDResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
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
public class GetDeliveryDriverByOrderIDIntegrationTest {

    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JwtUtil jwtUtil;
    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    UUID driverID;
    UUID deliveryID;
    Driver driver;
    Delivery delivery;
    Calendar time;
    UUID customerID;
    UUID orderID;
    UUID storeID;
    DeliveryStatus status;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    Order order;


    @BeforeEach
    void setUp(){
        driverID = UUID.fromString("00b310ec-4d81-409a-9231-3e42e012da7c");
        time = Calendar.getInstance();
        deliveryID = UUID.fromString("e6732473-7e91-4949-8811-d71e6fb6255d");
        orderID = UUID.fromString("54287c26-ae7d-4137-afbe-353789533a47");
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        storeID = UUID.fromString("0b3837de-a2d7-4fdc-a819-63ebd6dea1aa");
        status = DeliveryStatus.CollectedByDriver;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        driver = new Driver("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.DRIVER, driverID);
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.WaitingForShoppers, 0.0);
        delivery.setDriverID(driverID);
        order = new Order();
        order.setOrderID(orderID);
        order.setDriverID(driverID);
        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);
        SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);
        deliveryRepo.save(delivery);
        String jwt = jwtUtil.generateJWTTokenDriver(driver);

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
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when the orderID does not exist in the database.")
    @DisplayName("Invalid orderID")
    void invalidOrderIDPassedInRequestObject_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(UUID.randomUUID());
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request);
        assertEquals("Driver not found", response.getMessage());
    }

    @Test
    @Description("Tests for when the driver is successfully found.")
    @DisplayName("Successful found driver")
    void successfulFoundDriverInDelivery_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(deliveryID);
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request);
        assertEquals(response.getMessage(), "Driver successfully retrieved");
        assertEquals(response.getDriver().getDriverID(), driver.getDriverID());
    }

    @Test
    @Description("Tests for when the orderID doesnt match in the delivery")
    @DisplayName("Invalid delivery orderID")
    void invalidDeliveryOrderID_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDeliveryID(UUID.randomUUID());
        deliveryRepo.save(delivery);
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(UUID.randomUUID());
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request);
        assertEquals(response.getMessage(), "Driver not found");

    }

}
