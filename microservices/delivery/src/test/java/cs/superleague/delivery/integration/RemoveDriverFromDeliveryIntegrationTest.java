package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.RemoveDriverFromDeliveryRequest;
import cs.superleague.delivery.responses.RemoveDriverFromDeliveryResponse;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class RemoveDriverFromDeliveryIntegrationTest {
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
    UUID driverID;
    Driver driver;

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
        delivery.setDriverId(driverID);
        deliveryRepo.save(delivery);
        driver = new Driver();
        driver.setDriverID(driverID);
        driver.setName("Seamus");
        driver.setEmail("u19060468@tuks.co.za");
        driver.setAccountType(UserType.DRIVER);
        SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);


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

    @Test
    @Description("Tests for when the deliveryID passed in is not in database.")
    @DisplayName("Invalid DeliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.removeDriverFromDelivery(request));
        assertEquals("Delivery not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the driver was never assigned to the delivery.")
    @DisplayName("Driver not assigned")
    void driverWasNeverAssignedToDelivery_IntegrationTest(){
        delivery.setDriverId(UUID.randomUUID());
        deliveryRepo.save(delivery);
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.removeDriverFromDelivery(request));
        assertEquals("Driver was not assigned to delivery.", thrown.getMessage());
    }

    @Test
    @Description("Driver successfully removed from delivery.")
    @DisplayName("Driver removed")
    void driverSuccessfullyRemovedFromDelivery_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(deliveryID);
        RemoveDriverFromDeliveryResponse response = deliveryService.removeDriverFromDelivery(request);
        assertEquals(response.getMessage(), "Driver successfully removed from delivery.");
        assertEquals(response.isSuccess(), true);
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), null);
    }

    @Test
    @Description("Tests for when there is no driver assigned to the delivery")
    @DisplayName("No driver assigned")
    void noDriverAssignedToDelivery_IntegrationTest() {
        delivery.setDriverId(null);
        deliveryRepo.save(delivery);
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.removeDriverFromDelivery(request));
        assertEquals("No driver is assigned to this delivery.", thrown.getMessage());

    }
}
