package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryStatusRequest;
import cs.superleague.delivery.responses.GetDeliveryStatusResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetDeliveryStatusIntegrationTest {
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
        deliveryRepo.save(delivery);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
    }

    @Test
    @Description("Tests for when the delivery is not in the database.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDInRequestObject_IntegrationTest(){
        GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryStatus(request));
        assertEquals("Delivery not found in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the delivery is found in the database, the status should be returned.")
    @DisplayName("Status returned")
    void statusOfDeliveryReturned_IntegrationTest() throws InvalidRequestException{
        GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(deliveryID);
        GetDeliveryStatusResponse response = deliveryService.getDeliveryStatus(request);
        assertEquals(response.getStatus(), status);
        assertEquals(response.getMessage(), "Delivery Found.");
    }
}
