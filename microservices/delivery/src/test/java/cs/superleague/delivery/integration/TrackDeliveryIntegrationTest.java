package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.TrackDeliveryRequest;
import cs.superleague.delivery.responses.TrackDeliveryResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Customer;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TrackDeliveryIntegrationTest {
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
    UUID driver2ID;
    UUID deliveryID;
    Driver driver;
    Driver driver2;
    Delivery delivery;
    Calendar time;
    UUID customerID;
    UUID orderID;
    UUID storeID;
    DeliveryStatus status;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    GeoPoint driverLocation;
    Customer customer;

    @BeforeEach
    void setUp(){
        deliveryID = UUID.fromString("e6732473-7e91-4949-8811-d71e6fb6255d");
        orderID = UUID.fromString("54287c26-ae7d-4137-afbe-353789533a47");
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        storeID = UUID.fromString("0b3837de-a2d7-4fdc-a819-63ebd6dea1aa");
        driverID = UUID.fromString("00b310ec-4d81-409a-9231-3e42e012da7c");
        status = DeliveryStatus.CollectedByDriver;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        driver = new Driver("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.DRIVER, driverID);
        driver2 = new Driver();
        driver2ID = UUID.fromString("f1299f1e-27bc-42fe-af16-d68d8a91df35");
        driver2.setDriverID(driver2ID);
        driver2.setOnShift(false);
        driver2.setEmail("u14254922@tuks.co.za");
        driver2.setAccountType(UserType.DRIVER);
        SaveDriverToRepoRequest saveDriverToRepoRequest2 = new SaveDriverToRepoRequest(driver2);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest2);

        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.WaitingForShoppers, 0.0);
        driverLocation = new GeoPoint(10.0, 10.0, "address");
        driver.setCurrentAddress(driverLocation);
        driver.setOnShift(true);
        SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);

        customer = new Customer();
        customer.setEmail("seamus.peter.brennan@gmail.com");
        customer.setCustomerID(customerID);
        customer.setAccountType(UserType.CUSTOMER);
//        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
//        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);
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
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
    }

    @Test
    @Description("Tests for when the delivery passed in is not in the database.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliverIDPassedInRequestObject_IntegrationTest(){
        TrackDeliveryRequest request = new TrackDeliveryRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.trackDelivery(request));
        assertEquals("Delivery not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when no driver assigned to delivery.")
    @DisplayName("No driver assigned")
    void noDriverAssignedToDelivery_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(null);
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getCurrentLocation().getLatitude(), pickUpLocation.getLatitude());
        assertEquals(response.getCurrentLocation().getLongitude(), pickUpLocation.getLongitude());
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
    }

    @Test
    @Description("Tests for when the driver assigned to the delivery is not in the database.")
    @DisplayName("Invalid driver assigned")
    void invalidDriverAssignedToDelivery_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(UUID.randomUUID());
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getCurrentLocation().getLatitude(), pickUpLocation.getLatitude());
        assertEquals(response.getCurrentLocation().getLongitude(), pickUpLocation.getLongitude());
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverID(), null);
    }

    @Test
    @Description("Tests for when the driver assigned is not on shift")
    @DisplayName("Driver not on shift")
    void driverIsNoLongerOnShift_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(driver2ID);
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        assertEquals(response.getCurrentLocation().getLatitude(),
                pickUpLocation.getLatitude());
        assertEquals(response.getCurrentLocation().getLongitude(), pickUpLocation.getLongitude());

        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverID(), null);
    }

    @Test
    @Description("Tests for when a driver is assigned and returns the location of the driver.")
    @DisplayName("Gets driver location")
    void driverIsAssignedAndDriverLocationIsReturned_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        delivery.setDriverID(driverID);
        driver.setOnShift(true);
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getMessage(), "Drivers current location.");
        assertEquals(response.getCurrentLocation().getLongitude(),
                driverLocation.getLongitude());
        assertEquals(response.getCurrentLocation().getLatitude(), driverLocation.getLatitude());

        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverID(), driverID);
    }
}
