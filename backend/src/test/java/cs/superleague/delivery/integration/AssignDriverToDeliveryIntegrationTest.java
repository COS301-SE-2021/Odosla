package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.AssignDriverToDeliveryRequest;
import cs.superleague.delivery.responses.AssignDriverToDeliveryResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class AssignDriverToDeliveryIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;
    @Autowired
    DriverRepo driverRepo;

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

    @BeforeEach
    void setUp(){
        driverID = UUID.randomUUID();
        time = Calendar.getInstance();
        customerID = UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        status = DeliveryStatus.CollectedByDriver;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        driver = new Driver("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.DRIVER, driverID);
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.WaitingForShoppers, 0.0);
        driverRepo.save(driver);
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
        driverRepo.deleteAll();
    }

    @Test
    @Description("Test for when the driver does not exist in the database.")
    @DisplayName("Invalid driverID")
    void invalidDriverIDPassedInTheRequestObject_IntegrationTest(){
        AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(UUID.randomUUID(), deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request));
        assertEquals("Driver does not exist in the database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the deliveryID does not exist in the database.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(driverID, UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request));
        assertEquals("Delivery does not exist in the database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the delivery has already been taken by a different driver.")
    @DisplayName("Delivery already taken")
    void deliveryAlreadyTakenByADifferentDriver_IntegrationTest(){
        delivery.setDriverId(UUID.randomUUID());
        deliveryRepo.save(delivery);
        AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(driverID, deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request));
        assertEquals("This delivery has already been taken by another driver.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the driver is successfully assigned to the delivery.")
    @DisplayName("Successful assigning")
    void successfulAssigningOfDriverToDelivery_IntegrationTest() throws InvalidRequestException{
        AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(driverID, deliveryID);
        AssignDriverToDeliveryResponse response = deliveryService.assignDriverToDelivery(request);
        assertEquals(response.getMessage(), "Driver successfully assigned to delivery.");
        assertEquals(response.isAssigned(), true);
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), driverID);
    }

    @Test
    @Description("Tests for when the driver is already assigned to that delivery.")
    @DisplayName("Same driver is assigned already")
    void sameDriverIsAlreadyAssigned_IntegrationTest() throws InvalidRequestException{
        delivery.setDriverId(driverID);
        deliveryRepo.save(delivery);
        AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(driverID, deliveryID);
        AssignDriverToDeliveryResponse response = deliveryService.assignDriverToDelivery(request);
        assertEquals(response.getMessage(), "Driver was already assigned to delivery.");
        assertEquals(response.isAssigned(), true);
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), driverID);
    }
}