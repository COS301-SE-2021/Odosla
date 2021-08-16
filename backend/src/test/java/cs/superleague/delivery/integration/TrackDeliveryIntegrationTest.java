package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.TrackDeliveryRequest;
import cs.superleague.delivery.responses.TrackDeliveryResponse;
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
public class TrackDeliveryIntegrationTest {
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
    GeoPoint driverLocation;

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
        driverLocation = new GeoPoint(10.0, 10.0, "address");
        driver.setCurrentAddress(driverLocation);
        driver.setOnShift(true);
        driverRepo.save(driver);
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
        driverRepo.deleteAll();
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
    void noDriverAssignedToDelivery_IntegrationTest() throws InvalidRequestException{
        delivery.setDriverId(null);
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
    void invalidDriverAssignedToDelivery_IntegrationTest() throws InvalidRequestException{
        delivery.setDriverId(UUID.randomUUID());
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getCurrentLocation().getLatitude(), pickUpLocation.getLatitude());
        assertEquals(response.getCurrentLocation().getLongitude(), pickUpLocation.getLongitude());
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), null);
    }

    @Test
    @Description("Tests for when the driver assigned is not on shift")
    @DisplayName("Driver not on shift")
    void driverIsNoLongerOnShift_IntegrationTest() throws InvalidRequestException{
        delivery.setDriverId(driverID);
        driver.setOnShift(false);
        driverRepo.save(driver);
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getCurrentLocation().getLatitude(), pickUpLocation.getLatitude());
        assertEquals(response.getCurrentLocation().getLongitude(), pickUpLocation.getLongitude());
        assertEquals(response.getMessage(), "No driver has been assigned to this delivery.");
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), null);
    }

    @Test
    @Description("Tests for when a driver is assigned and returns the location of the driver.")
    @DisplayName("Gets driver location")
    void driverIsAssignedAndDriverLocationIsReturned_IntegrationTest() throws InvalidRequestException{
        delivery.setDriverId(driverID);
        driver.setOnShift(true);
        driverRepo.save(driver);
        deliveryRepo.save(delivery);
        TrackDeliveryRequest request = new TrackDeliveryRequest(deliveryID);
        TrackDeliveryResponse response = deliveryService.trackDelivery(request);
        assertEquals(response.getCurrentLocation().getLongitude(), driverLocation.getLongitude());
        assertEquals(response.getCurrentLocation().getLatitude(), driverLocation.getLatitude());
        assertEquals(response.getMessage(), "Drivers current location.");
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), driverID);
    }
}
