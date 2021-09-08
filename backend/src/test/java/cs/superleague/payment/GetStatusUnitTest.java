package cs.superleague.payment;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetOrderRequest;
import cs.superleague.payment.requests.GetStatusRequest;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.payment.responses.GetStatusResponse;
import cs.superleague.shopping.dataclass.Item;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class GetStatusUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    Item I1;
    Item I2;
    Order o;
    Order o2;
    UUID o1UUID = UUID.randomUUID();
    UUID o2UUID = UUID.randomUUID();
    UUID expectedU1 = UUID.randomUUID();
    UUID expectedS1 = UUID.randomUUID();
    UUID expectedShopper1 = UUID.randomUUID();
    Double expectedDiscount;
    Double newDiscount;
    double totalC;
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress = new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems = new ArrayList<>();
    List<Item> newListOfItems = new ArrayList<>();
    List<Order> listOfOrders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        I1 = new Item("Heinz Tamatoe Sauce", "123456", "123456", expectedS1, 36.99, 1, "description", "img/");
        I2 = new Item("Bar one", "012345", "012345", expectedS1, 14.99, 3, "description", "img/");
        expectedMessage = "Order successfully created.";
        expectedDiscount = 0.0;
        newDiscount = 13.68;
        totalC = 66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType = OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        newListOfItems.add(I2);
        newListOfItems.add(I1);
        o = new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2 = new Order(o2UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getStatus(null));
        assertEquals("Invalid getStatusRequest received - could not get status.", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object is not specified")
    void UnitTest_testingNull_OrderID_Parameter_RequestObject() {
        GetStatusRequest request = new GetStatusRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getStatus(request));
        assertEquals("OrderID cannot be null in request object - could not get status.", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object does not exist")
    void UnitTest_OrderID_Parameter_RequestObject_Not_In_DB() {
        GetStatusRequest request = new GetStatusRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, () -> paymentService.getStatus(request));
        assertEquals("Order doesn't exist in database - could not get status.", thrown.getMessage());
    }

    // success
    @Test
    @Description("Tests for when order requested is found - should return the object")
    @DisplayName("when the order is found the DB")
    void UnitTest_testingOrderExists() throws InvalidRequestException, OrderDoesNotExist{
        GetStatusRequest request = new GetStatusRequest(o1UUID);
        when(orderRepo.findAll()).thenReturn(listOfOrders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));

        try {
            GetStatusResponse response = paymentService.getStatus(request);

            assertTrue(response.isSuccess());
            assertEquals("Status retrieval successful.", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Description("Tests for when order requested is found - should return the object")
    @DisplayName("when the order is found the DB")
    void UnitTest_testingOrderExistsInvalidUserType() throws InvalidRequestException, OrderDoesNotExist{

        o.setStatus(null);

        GetStatusRequest request = new GetStatusRequest(o1UUID);
        when(orderRepo.findAll()).thenReturn(listOfOrders);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));

        try {
            GetStatusResponse response = paymentService.getStatus(request);
            assertFalse(response.isSuccess());
            assertEquals("Order does not have a valid Status", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
