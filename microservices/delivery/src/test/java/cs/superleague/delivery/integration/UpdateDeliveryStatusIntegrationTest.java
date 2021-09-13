package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.UpdateDeliveryStatusRequest;
import cs.superleague.delivery.responses.UpdateDeliveryStatusResponse;
import cs.superleague.delivery.stub.dataclass.Driver;
import cs.superleague.delivery.stub.dataclass.GeoPoint;
import cs.superleague.delivery.stub.dataclass.Order;
import cs.superleague.delivery.stub.requests.SaveDriverRequest;
import cs.superleague.delivery.stub.requests.SaveOrderToRepoRequest;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
public class UpdateDeliveryStatusIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    DeliveryRepo deliveryRepo;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    UUID deliveryID;
    Delivery delivery;
    Calendar time;
    UUID customerID;
    UUID orderID;
    UUID storeID;
    UUID driverID;
    Driver driver;
    Order order;
    DeliveryStatus status;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;

    String uri;

    @BeforeEach
    void setUp(){
        time = Calendar.getInstance();
        customerID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        status = DeliveryStatus.CollectedByDriver;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.WaitingForShoppers, 0.0);
        delivery.setDriverId(driverID);

        orderID = UUID.randomUUID();
        delivery.setOrderID(orderID);
        deliveryRepo.save(delivery);

        uri = "http://localhost:8089/user/findDriverById";

        driver = new Driver();
        driver.setDriverID(UUID.randomUUID());

        order = new Order();
        order.setOrderID(UUID.randomUUID());

        // Store Driver
        SaveDriverRequest saveDriverRequest = new SaveDriverRequest(driver);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriver", saveDriverRequest);

        // Save Order
        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_saveOrderToRepo", saveOrderToRepoRequest);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_IntegrationTest() {
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, deliveryID, "detail");
        assertEquals(request.getDeliveryID(), deliveryID);
        assertEquals(request.getStatus(), status);
        assertEquals(request.getDetail(), "detail");
    }

    @Test
    @Description("Tests for when a null request object is passed in.")
    @DisplayName("Null request")
    void nullRequestObject_IntegrationTest() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.updateDeliveryStatus(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there is null parameters in the request object")
    @DisplayName("Null parameters")
    void nullParametersInRequestObject_IntegrationTest(){
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(null, deliveryID, "detail");
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request1));
        assertEquals("Null parameters.", thrown1.getMessage());

        UpdateDeliveryStatusRequest request2 = new UpdateDeliveryStatusRequest(status, null, "detail");
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request2));
        assertEquals("Null parameters.", thrown2.getMessage());

        UpdateDeliveryStatusRequest request3 = new UpdateDeliveryStatusRequest(status, deliveryID, null);
        Throwable thrown3 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request3));
        assertEquals("Null parameters.", thrown3.getMessage());
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
    @Description("Tests for when the request delivery status is 'DELIVERED', Driver not found")
    @DisplayName("Successful update -> DELIVERED")
    void deliveryStatusUpdatedSuccessfully_DELIVERED_IntegrationTest(){
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(DeliveryStatus.Delivered, deliveryID, "detail");

        try {
            UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request1);
            assertEquals(response.getMessage(), "Successful status update.");
            Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
            assertEquals(delivery1.get().getStatus(), DeliveryStatus.Delivered);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

//    @Test
//    @Description("Delivery successfully updated in the database")
//    @DisplayName("Successful update")
//    void deliveryStatusUpdatedSuccessfully_IntegrationTest() throws InvalidRequestException {
//        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(DeliveryStatus.Delivered, deliveryID, "detail");
//        UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request);
//        assertEquals(response.getMessage(), "Successful status update.");
//        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
//        assertEquals(delivery1.get().getStatus(), DeliveryStatus.Delivered);
//    }
}
