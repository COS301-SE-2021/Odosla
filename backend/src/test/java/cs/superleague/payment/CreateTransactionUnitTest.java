package cs.superleague.payment;

import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.StatusCodeException;
import cs.superleague.payment.repos.TransactionRepo;
import cs.superleague.payment.requests.CreateTransactionRequest;
import cs.superleague.payment.responses.CreateTransactionResponse;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Store;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.CancelOrderRequest;
import cs.superleague.payment.responses.CancelOrderResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import cs.superleague.shopping.dataclass.Item;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionUnitTest {
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    String transactionAddress = "3572e554071a5e559e576c5c1c179fcc692e6eca3a627f47694837b0bd8bf4e7";
    Item I1;
    Item I2;
    Order o;
    Order o2;
    UUID o1UUID=UUID.randomUUID();
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
    Store expectedStore;
    Catalogue c;

    @BeforeEach
    void setUp() {
        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=81.96;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);
        c=new Catalogue(expectedS1,expectedListOfItems);
        expectedStore=new Store(expectedS1,"Woolworthes",c,3,listOfOrders,null,4,true);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
    }

    @Test
    @Description("Tests that the request object is created successfully")
    @DisplayName("Request object creation")
    void UnitTest_RequestObjectCreation(){
        CreateTransactionRequest request = new CreateTransactionRequest(o1UUID, transactionAddress);
        assertEquals(request.getOrderID(), o1UUID);
        assertEquals(request.getTransactionAddress(), transactionAddress);
    }

    @Test
    @Description("Tests for when the request object is null that the function throws the correct exception")
    @DisplayName("Null request object")
    void UnitTest_NullRequestObject() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.createTransaction(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the orderID is null that the function throws the correct exception")
    @DisplayName("Null orderID parameter")
    void UnitTest_NullOrderID() {
        CreateTransactionRequest request = new CreateTransactionRequest(null, transactionAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.createTransaction(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the transaction address is null that the function throws the correct exception")
    @DisplayName("Null transaction address parameter")
    void UnitTest_NullTransactionAddress() {
        CreateTransactionRequest request = new CreateTransactionRequest(o1UUID, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.createTransaction(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when both parameters are null that the function throws the correct exception")
    @DisplayName("Both parameters null")
    void UnitTest_NullParameters() {
        CreateTransactionRequest request = new CreateTransactionRequest(o1UUID, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.createTransaction(request));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the order does not exist, exception should be thrown")
    @DisplayName("Order does not exist")
    void UnitTest_OrderDoesNotExist() {
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        CreateTransactionRequest request = new CreateTransactionRequest(UUID.randomUUID(), transactionAddress);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()->paymentService.createTransaction(request));
        assertEquals("Invalid orderID.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the order does exist, but the status of the order is null")
    @DisplayName("Null status")
    void UnitTest_OrderHasNullStatus() {
        o.setStatus(null);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CreateTransactionRequest request = new CreateTransactionRequest(o1UUID, transactionAddress);
        Throwable thrown = Assertions.assertThrows(StatusCodeException.class, ()->paymentService.createTransaction(request));
        assertEquals("Invalid statusCode.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the order does exist, but the status of the order means it is not awaiting payment")
    @DisplayName("Invalid status")
    void UnitTest_OrderHasInvalidStatus() {
        o.setStatus(OrderStatus.AWAITING_COLLECTION);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CreateTransactionRequest request = new CreateTransactionRequest(o1UUID, transactionAddress);
        Throwable thrown = Assertions.assertThrows(StatusCodeException.class, ()->paymentService.createTransaction(request));
        assertEquals("Invalid statusCode.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the order does exist, and the transaction needs to be created")
    @DisplayName("Valid Transaction creation")
    void UnitTest_OrderIsValidAndTransactionCreated() {
        o.setStatus(OrderStatus.AWAITING_PAYMENT);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(o));
        CreateTransactionRequest request = new CreateTransactionRequest(o1UUID, transactionAddress);
        assertDoesNotThrow(()->paymentService.createTransaction(request));
    }

}
