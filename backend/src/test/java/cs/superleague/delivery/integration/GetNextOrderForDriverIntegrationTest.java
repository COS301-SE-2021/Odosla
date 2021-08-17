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
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetNextOrderForDriverIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;
    @Autowired
    DriverRepo driverRepo;
    @Autowired
    JwtUtil jwtUtil;

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
    String jwtToken;

    @BeforeEach
    void setUp(){
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        status = DeliveryStatus.WaitingForShoppers;
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, 0.0);

        driverLocationFar = new GeoPoint(12.0, 12.0, "address");
        driverID = UUID.randomUUID();
        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        jwtToken=jwtUtil.generateJWTTokenDriver(driver);
        driverRepo.save(driver);
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        driverRepo.deleteAll();
        deliveryRepo.deleteAll();
    }

    @Test
    @Description("Tests for when the driver passed in isn't in the database.")
    @DisplayName("Invalid driverID")
    void invalidDriverIDPassedInRequestObject_IntegrationTest(){
        Driver driver2 = new Driver();
        driver2.setDriverID(UUID.randomUUID());
        String jwtToken2= jwtUtil.generateJWTTokenDriver(driver2);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(jwtToken2, pickUpLocation);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getNextOrderForDriver(request));
        assertEquals("Driver not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are no available deliveries in the database.")
    @DisplayName("No available deliveries")
    void noAvailableDeliveriesInDatabase_IntegrationTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
        delivery.setDriverId(UUID.randomUUID());
        deliveryRepo.save(delivery);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(jwtToken, pickUpLocation);
        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request);
        assertEquals(response.getMessage(), "No available deliveries in the database.");
        assertEquals(response.getDelivery(), null);
    }

    @Test
    @Description("Tests for when there are no deliveries within the range specified.")
    @DisplayName("No deliveries in range")
    void noAvailableDeliveriesInRange_IntegrationTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
        delivery.setDriverId(null);
        deliveryRepo.save(delivery);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(jwtToken, driverLocationFar);
        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request);
        assertEquals(response.getMessage(), "No available deliveries in the range specified.");
        assertEquals(response.getDelivery(), null);
    }

    @Test
    @Description("Tests for when there is an available delivery for the driver.")
    @DisplayName("Valid delivery assigned")
    void driverIsGivenDeliveryToTake_IntegrationTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
        delivery.setDriverId(driverID);
        deliveryRepo.save(delivery);
        GetNextOrderForDriverRequest request = new GetNextOrderForDriverRequest(jwtToken, pickUpLocation);
        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request);
        assertEquals(response.getMessage(), "Driver can take the following delivery.");
        assertEquals(response.getDelivery().getDeliveryID(), deliveryID);

    }

}
