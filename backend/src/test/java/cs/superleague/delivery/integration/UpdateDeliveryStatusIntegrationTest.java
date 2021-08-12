package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.UpdateDeliveryStatusRequest;
import cs.superleague.delivery.responses.UpdateDeliveryStatusResponse;
import cs.superleague.payment.dataclass.GeoPoint;
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
public class UpdateDeliveryStatusIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;

    UUID deliveryID;
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
        time = Calendar.getInstance();
        customerID = UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        status = DeliveryStatus.CollectedByDriver;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.WaitingForShoppers, 0.0);
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
    }

    @Test
    @Description("Tests for when there is no delivery with the ID in the database.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, UUID.randomUUID(), "detail");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request));
        assertEquals("Delivery does not exist in database.", thrown.getMessage());
    }

    @Test
    @Description("Delivery successfully updated in the database")
    @DisplayName("Successful update")
    void deliveryStatusUpdatedSuccessfully_IntegrationTest() throws InvalidRequestException{
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(DeliveryStatus.Delivered, deliveryID, "detail");
        UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request);
        assertEquals(response.getMessage(), "Successful status update.");
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getStatus(), DeliveryStatus.Delivered);
    }
}
