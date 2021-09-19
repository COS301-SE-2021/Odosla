package cs.superleague.payment;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.VerifyPaymentRequest;
import cs.superleague.payment.responses.VerifyPaymentResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class verifyPaymentUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

    CartItem I1;
    CartItem I2;
    Order order;
    Order o2;

    UUID userID;
    UUID orderID;
    UUID o2UUID=UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    double totalC;
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

    List<CartItem> expectedListOfItems=new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        orderID = UUID.randomUUID();

        I1 = new CartItem("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2 = new CartItem("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);

        order = new Order();
        order.setOrderID(orderID);
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
    @Description("Tests the creation of the request object.")
    @DisplayName("Request object creation")
    void testsTheCreationOfARequestObject_UnitTest(){
        VerifyPaymentRequest request = new VerifyPaymentRequest(orderID);
        assertEquals(request.getOrderID(), orderID);
    }

    @Test
    @Description("Test for when a null request object is passed in")
    @DisplayName("Null request")
    void nullRequestObjectPassedIn_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.verifyPayment(null));
        assertEquals("Invalid request received - request cannot be null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the orderID is null.")
    @DisplayName("Null OrderID")
    void nullOrderIDInRequestObject_UnitTest(){
        VerifyPaymentRequest request = new VerifyPaymentRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.verifyPayment(request));
        assertEquals("Invalid request received - orderID cannot be null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there is no order in the database")
    @DisplayName("Invalid orderID")
    void invalidOrderIDPassedInRequestObject_UnitTest(){
        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(null);
        VerifyPaymentRequest request = new VerifyPaymentRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.verifyPayment(request));
        assertEquals("Order doesn't exist in database - could not create transaction", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the orderID is valid")
    @DisplayName("Valid OrderID")
    void validOrderIDPassedInRequestObject_UnitTest() throws PaymentException, InterruptedException {
        Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        VerifyPaymentRequest request = new VerifyPaymentRequest(orderID);
        VerifyPaymentResponse response = paymentService.verifyPayment(request);
        assertEquals(response.getMessage(), "Payment Successfully verified.");
        assertTrue(response.isSuccess());
    }
}
