package cs.superleague.payment;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetItemsRequest;
import cs.superleague.payment.responses.GetItemsResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetItemsUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    CartItem I1;
    CartItem I2;

    Order order;
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
    GeoPoint storeAddress = new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");
    List<CartItem> expectedListOfItems = new ArrayList<>();
    List<CartItem> newListOfItems = new ArrayList<>();
    List<Order> listOfOrders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        I1 = new CartItem("Heinz Tomato Sauce", "123456", "123456", expectedS1, 36.99, 1, "description", "img/");
        I2 = new CartItem("Bar one", "012345", "012345", expectedS1, 14.99, 3, "description", "img/");

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

        order = new Order();
        order.setOrderID(o1UUID);
        order.setUserID(expectedU1);
        order.setStoreID(expectedS1);
        order.setShopperID(expectedShopper1);
        order.setCreateDate(new Date());
        order.setTotalCost(totalC);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setCartItems(expectedListOfItems);
        order.setDiscount(expectedDiscount);
        order.setDeliveryAddress(deliveryAddress);
        order.setStoreAddress(storeAddress);
        order.setRequiresPharmacy(false);

        o2 = new Order();
        o2.setOrderID(o2UUID);
        o2.setUserID(expectedU1);
        o2.setStoreID(expectedS1);
        o2.setShopperID(expectedShopper1);
        o2.setCreateDate(new Date());
        o2.setTotalCost(totalC);
        o2.setType(OrderType.DELIVERY);
        o2.setStatus(OrderStatus.AWAITING_PAYMENT);
        o2.setCartItems(expectedListOfItems);
        o2.setDiscount(expectedDiscount);
        o2.setDeliveryAddress(deliveryAddress);
        o2.setStoreAddress(storeAddress);
        o2.setRequiresPharmacy(false);

        listOfOrders.add(order);
        listOfOrders.add(o2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getItems(null));
        assertEquals("GetItemsRequest is null - could not get Items", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object is not specified")
    void UnitTest_testingNull_OrderID_Parameter_RequestObject() {
        GetItemsRequest request = new GetItemsRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getItems(request));
        assertEquals("OrderID attribute is null - could not get Items", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID in the request object does not exist")
    void UnitTest_OrderID_Parameter_RequestObject_Not_In_DB() {
        GetItemsRequest request = new GetItemsRequest(UUID.randomUUID().toString());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, () -> paymentService.getItems(request));
        assertEquals("Order with given ID does not exist - could not get Items", thrown.getMessage());
    }

    @Test
    @DisplayName("when the order is found the DB")
    void UnitTest_testingOrderExists(){
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));

        GetItemsRequest request = new GetItemsRequest(o1UUID.toString());

        try {
            GetItemsResponse response = paymentService.getItems(request);
            assertEquals("Items successfully retrieved", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getCartItems());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}