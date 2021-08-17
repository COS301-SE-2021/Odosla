package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CompleteDeliveryRequest;
import cs.superleague.user.responses.CompleteDeliveryResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CompleteDeliveryIntegrationTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    private UserServiceImpl userService;

    CompleteDeliveryResponse response;
    CompleteDeliveryRequest request;

    Order order;
    UUID orderId= UUID.randomUUID();

    Driver driver;
    UUID driverId= UUID.randomUUID();

    @BeforeEach
    void setup(){
        request=new CompleteDeliveryRequest(orderId);
        order=new Order();
        driver = new Driver();
        driver.setDriverID(driverId);
        order.setOrderID(orderId);
        order.setDriverID(driverId);

    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(null));
        assertEquals("CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request.setOrderID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(request));
        assertEquals("OrderID in CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void IntegrationTest_testingOrderDoesNotExistException(){
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.completeDelivery(request));
        assertEquals("Order does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("Order correctly collected")
    void IntegrationTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist {
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        orderRepo.save(order);
        driverRepo.save(driver);
        response=userService.completeDelivery(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been delivered and status has been changed",response.getMessage());

        Optional<Order> o=orderRepo.findById(request.getOrderID());

        assertNotNull(o);
        assertEquals(orderId,o.get().getOrderID());
        assertEquals(OrderStatus.DELIVERED,o.get().getStatus());
    }

}
