package cs.superleague.delivery.integration;


import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.AddDeliveryDetailRequest;
import cs.superleague.delivery.responses.AddDeliveryDetailResponse;
import cs.superleague.delivery.stubs.payment.dataclass.GeoPoint;
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
public class AddDeliveryDetailIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    DeliveryRepo deliveryRepo;

    @Autowired
    DeliveryDetailRepo deliveryDetailRepo;

    DeliveryStatus status;
    UUID deliveryID;
    UUID orderID;
    Calendar time;
    Delivery delivery;
    GeoPoint dropOffLocation;
    GeoPoint pickUpLocation;
    UUID customerID;
    UUID storeID;

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
        deliveryDetailRepo.deleteAll();
    }

    @Test
    @Description("Tests when there is invalid deliveryID, no delivery found in database")
    @DisplayName("Invalid DeliveryID")
    void invalidDeliveryIDPassedIn_IntegrationTest(){
        AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(status, "detail", UUID.randomUUID(), time);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.addDeliveryDetail(request));
        assertEquals("Delivery does not exist in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests that the delivery detail is added successfully.")
    @DisplayName("Successful adding of detail")
    void addDetailSuccessfullyToDatabase_IntegrationTest() throws InvalidRequestException{
        AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(status, "detail", deliveryID, time);
        AddDeliveryDetailResponse response = deliveryService.addDeliveryDetail(request);
        assertEquals(response.getMessage(), "Delivery details added successfully.");
        Optional<DeliveryDetail> deliveryDetail = deliveryDetailRepo.findById(response.getId());
        assertEquals(deliveryDetail.get().getDeliveryID(), deliveryID);
        assertEquals(deliveryDetail.get().getDetail(), "detail");
        assertEquals(deliveryDetail.get().getStatus(), status);
        assertEquals(deliveryDetail.get().getTime(), time);
        assertEquals(deliveryDetail.get().getId(), response.getId());
    }
}
