package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.AssignDriverToDeliveryRequest;
import cs.superleague.delivery.requests.GetDeliveryDriverByOrderIDRequest;
import cs.superleague.delivery.responses.AssignDriverToDeliveryResponse;
import cs.superleague.delivery.responses.GetDeliveryDriverByOrderIDResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.dataclass.Customer;
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
public class GetDeliveryDriverByOrderIDIntegrationTest {

    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;
    @Autowired
    OrderRepo orderRepo;
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
    Order order;

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
        delivery.setDriverId(driverID);
        order = new Order();
        order.setOrderID(orderID);
        order.setDriverID(driverID);
        orderRepo.save(order);
        driverRepo.save(driver);
        deliveryRepo.save(delivery);

    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
        driverRepo.deleteAll();
    }

    @Test
    @Description("Tests for when the orderID does not exist in the database.")
    @DisplayName("Invalid orderID")
    void invalidOrderIDPassedInRequestObject_IntegrationTest(){
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDriverByOrderID(request));
        assertEquals("Order does not exist", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the driver is successfully found.")
    @DisplayName("Successful found driver")
    void successfulFoundDriverInDelivery_IntegrationTest() throws InvalidRequestException {
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(orderID);
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request);
        assertEquals(response.getMessage(), "Driver successfully retrieved");
        assertEquals(response.getDriver().getDriverID(), driver.getDriverID());
    }

    @Test
    @Description("Tests for when the orderID doesnt match in the delivery")
    @DisplayName("Invalid delivery orderID")
    void invalidDeliveryOrderID_IntegrationTest() throws InvalidRequestException {
        delivery.setOrderID(UUID.randomUUID());
        deliveryRepo.save(delivery);
        GetDeliveryDriverByOrderIDRequest request = new GetDeliveryDriverByOrderIDRequest(orderID);
        GetDeliveryDriverByOrderIDResponse response = deliveryService.getDeliveryDriverByOrderID(request);
        assertEquals(response.getMessage(), "Driver not found");

    }

}
