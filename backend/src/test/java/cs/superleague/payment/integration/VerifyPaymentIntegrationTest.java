package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.VerifyPaymentRequest;
import cs.superleague.payment.responses.VerifyPaymentResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class VerifyPaymentIntegrationTest {

    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ItemRepo itemRepo;

    Item I1;
    Item I2;
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
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        orderID = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        order =new Order(orderID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(order);
        listOfOrders.add(o2);
        itemRepo.saveAll(expectedListOfItems);
        orderRepo.save(order);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
        itemRepo.deleteAll();
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
        VerifyPaymentRequest request = new VerifyPaymentRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.verifyPayment(request));
        assertEquals("Order doesn't exist in database - could not create transaction", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the orderID is valid")
    @DisplayName("Valid OrderID")
    void validOrderIDPassedInRequestObject_UnitTest() throws PaymentException, InterruptedException {
        VerifyPaymentRequest request = new VerifyPaymentRequest(orderID);
        VerifyPaymentResponse response = paymentService.verifyPayment(request);
        assertEquals(response.getMessage(), "Payment Successfully verified.");
        assertEquals(response.isSuccess(), true);
        Optional<Order> order1 = orderRepo.findById(orderID);
        assertEquals(order1.get().getStatus(), OrderStatus.PURCHASED);
    }
}
