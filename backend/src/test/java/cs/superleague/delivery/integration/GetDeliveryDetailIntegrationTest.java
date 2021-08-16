package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryDetailRequest;
import cs.superleague.delivery.responses.GetDeliveryDetailResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetDeliveryDetailIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    DeliveryRepo deliveryRepo;

    @Autowired
    DeliveryDetailRepo deliveryDetailRepo;

    @Autowired
    AdminRepo adminRepo;

    Admin admin;
    UUID adminID;
    UUID deliveryID;
    Delivery delivery;
    UUID orderID;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID, storeID;
    DeliveryStatus status;
    Calendar time;
    DeliveryDetail deliveryDetail1;
    DeliveryDetail deliveryDetail2;

    @BeforeEach
    void setUp(){
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        orderID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        adminID=UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, 0.0);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, DeliveryStatus.CollectedByDriver, "detail1");
        deliveryDetail2 = new DeliveryDetail(UUID.randomUUID(), Calendar.getInstance(), DeliveryStatus.DeliveringToCustomer, "detail2");
        adminRepo.save(admin);
        deliveryRepo.save(delivery);
        deliveryDetailRepo.save(deliveryDetail1);
        deliveryDetailRepo.save(deliveryDetail2);
    }

    @AfterEach
    void tearDown(){
        adminRepo.deleteAll();
        deliveryRepo.deleteAll();
        deliveryDetailRepo.deleteAll();
    }

    @Test
    @Description("Tests for when the user looking for the data is not an admin.")
    @DisplayName("Invalid adminID")
    void invalidAdminIDInRequestObject_IntegrationTest(){
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(deliveryID, UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDetail(request));
        assertEquals("User is not an admin.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are no details for the delivery or delivery does not exist.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(UUID.randomUUID(), adminID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDetail(request));
        assertEquals("No details found for this delivery.", thrown.getMessage());
    }

    @Test
    @Description("Tests for delivery details are found.")
    @DisplayName("Delivery details returned")
    void detailsReturnedSuccessfully_IntegrationTest() throws InvalidRequestException{
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(deliveryID, adminID);
        GetDeliveryDetailResponse response = deliveryService.getDeliveryDetail(request);
        assertEquals(response.getMessage(), "Details successfully found.");
        assertEquals(response.getDetail().get(0).getDetail(), "detail1");
    }
}
