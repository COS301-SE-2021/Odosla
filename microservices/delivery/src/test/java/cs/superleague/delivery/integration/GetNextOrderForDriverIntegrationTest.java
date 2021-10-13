package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetNextOrderForDriverRequest;
import cs.superleague.delivery.responses.GetNextOrderForDriverResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveDriverToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetNextOrderForDriverIntegrationTest {

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

    Delivery delivery;
    UUID deliveryID;
    UUID orderID;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    Driver driver;
    UUID driverID;
    GeoPoint driverLocationFar;

    @BeforeEach
    void setUp(){
        deliveryID = UUID.fromString("e6732473-7e91-4949-8811-d71e6fb6255d");
        orderID = UUID.fromString("54287c26-ae7d-4137-afbe-353789533a47");
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        storeID = UUID.fromString("0b3837de-a2d7-4fdc-a819-63ebd6dea1aa");
        driverID = UUID.fromString("00b310ec-4d81-409a-9231-3e42e012da7c");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        status = DeliveryStatus.WaitingForShoppers;
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, 0.0);

        driverLocationFar = new GeoPoint(12.0, 12.0, "address");
        driver = new Driver();
        driver.setDriverID(driverID);
        driver.setName("Seamus");
        driver.setEmail("u19060468@tuks.co.za");
        driver.setAccountType(UserType.DRIVER);
        SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);
        deliveryRepo.save(delivery);

        String jwt = jwtUtil.generateJWTTokenDriver(driver);
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

//    @Test
//    @Description("Tests for when the driver passed in isn't in the database.")
//    @DisplayName("Invalid driverID")
//    void invalidDriverIDPassedInRequestObject_IntegrationTest(){
//        Driver driver1 = new Driver();
//        driver1.setDriverID(UUID.randomUUID());
//        driver1.setName("John");
//        driver1.setEmail("hello@gmail.com");
//        driver1.setAccountType(UserType.DRIVER);
//        JwtUtil jwtUtil = new JwtUtil();
//        String jwt = jwtUtil.generateJWTTokenDriver(driver1);
//        jwt = jwt.replace("Bearer ","");
//        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
//        List<String> authorities = (List) claims.get("authorities");
//        String userType= (String) claims.get("userType");
//        String email = (String) claims.get("email");
//        System.out.println(userType);
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        HashMap<String, Object> info=new HashMap<String, Object>();
//        info.put("userType",userType);
//        info.put("email",email);
//        auth.setDetails(info);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(pickUpLocation);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getNextOrderForDriver(request));
//        assertEquals("Invalid user.", thrown.getMessage());
//    }

    @Test
    @Description("Tests for when there are no available deliveries in the database.")
    @DisplayName("No available deliveries")
    void noAvailableDeliveriesInDatabase_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(UUID.randomUUID());
        deliveryRepo.save(delivery);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(pickUpLocation);
        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request);
        assertEquals(response.getMessage(), "No available deliveries in the database.");
        assertEquals(response.getDelivery(), null);
    }

    @Test
    @Description("Tests for when there are no deliveries within the range specified.")
    @DisplayName("No deliveries in range")
    void noAvailableDeliveriesInRange_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(null);
        deliveryRepo.save(delivery);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(driverLocationFar);
        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request);
        assertEquals(response.getMessage(), "No available deliveries in the range specified.");
        assertEquals(response.getDelivery(), null);
    }

    @Test
    @Description("Tests for when there is an available delivery for the driver.")
    @DisplayName("Valid delivery assigned")
    void driverIsGivenDeliveryToTake_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(null);
        delivery.setOrderOnePacked(true);
        delivery.setOrderTwoPacked(true);
        delivery.setOrderThreePacked(true);
        deliveryRepo.save(delivery);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(pickUpLocation);
        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request);
        assertEquals(response.getMessage(), "Driver can take the following delivery.");
        assertEquals(response.getDelivery().getDeliveryID(), deliveryID);

    }

}
