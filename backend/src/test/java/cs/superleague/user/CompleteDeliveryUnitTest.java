package cs.superleague.user;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CollectOrderRequest;
import cs.superleague.user.requests.CompleteDeliveryRequest;
import cs.superleague.user.responses.CompleteDeliveryResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CompleteDeliveryUnitTest {

    @Mock
    OrderRepo orderRepo;

    @Mock
    DriverRepo driverRepo;

    @InjectMocks
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
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(null));
        assertEquals("CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request.setOrderID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(request));
        assertEquals("OrderID in CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void UnitTest_testingOrderDoesNotExistException(){
        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.completeDelivery(request));
        assertEquals("Order does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID exists - but couldn't update Order")
    void UnitTest_testingCouldntUpdateOrder() throws InvalidRequestException, OrderDoesNotExist {
        cs.superleague.payment.dataclass.Order newOrder = new Order();
        newOrder.setStatus(OrderStatus.AWAITING_COLLECTION);
        order.setStatus(OrderStatus.AWAITING_COLLECTION);
        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(order)).thenReturn(java.util.Optional.of(newOrder));
        response=userService.completeDelivery(request);
        assertFalse(response.isSuccess());
        assertNotNull(response);
        assertNotNull(response.getTimestamp());
        assertEquals("Couldn't update that order has been delivered in database",response.getMessage());
    }

    @Test
    @DisplayName("Order correctly collected")
    void UnitTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist {
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(driver));
        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(order));
        response=userService.completeDelivery(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been delivered and status has been changed",response.getMessage());
    }





}
