package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.RemoveDriverFromDeliveryRequest;
import cs.superleague.delivery.responses.RemoveDriverFromDeliveryResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class RemoveDriverFromDeliveryIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;

    Delivery delivery;
    UUID deliveryID;
    UUID orderID;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    UUID driverID;

    @BeforeEach
    void setUp(){
        driverID = UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        status = DeliveryStatus.WaitingForShoppers;
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, 0.0);
        delivery.setDriverId(driverID);
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
    }

    @Test
    @Description("Tests for when the deliveryID passed in is not in database.")
    @DisplayName("Invalid DeliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(driverID, UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.removeDriverFromDelivery(request));
        assertEquals("Delivery not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the driver was never assigned to the delivery.")
    @DisplayName("Driver not assigned")
    void driverWasNeverAssignedToDelivery_IntegrationTest(){
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(UUID.randomUUID(), deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.removeDriverFromDelivery(request));
        assertEquals("Driver was not assigned to delivery.", thrown.getMessage());
    }

    @Test
    @Description("Driver successfully removed from delivery.")
    @DisplayName("Driver removed")
    void driverSuccessfullyRemovedFromDelivery_IntegrationTest() throws InvalidRequestException{
        RemoveDriverFromDeliveryRequest request = new RemoveDriverFromDeliveryRequest(driverID, deliveryID);
        RemoveDriverFromDeliveryResponse response = deliveryService.removeDriverFromDelivery(request);
        assertEquals(response.getMessage(), "Driver successfully removed from delivery.");
        assertEquals(response.isSuccess(), true);
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getDriverId(), null);
    }
}
