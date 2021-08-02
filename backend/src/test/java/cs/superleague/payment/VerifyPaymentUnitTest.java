package cs.superleague.payment;

import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.StatusCodeException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.repos.TransactionRepo;
import cs.superleague.payment.requests.VerifyPaymentRequest;
import cs.superleague.shopping.dataclass.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.mockito.InjectMocks;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerifyPaymentUnitTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // Order
    Order order;

    // Transaction
    Transaction transaction;
    Transaction transaction2;

    // Item
    Item item;

    UUID orderID;
    UUID userID;
    UUID storeID;
    UUID shopperID;
    UUID transactionID;
    UUID transactionID2;

    // Order statuses
    OrderStatus awaitingPayment;

    List<Item> listOfItems=new ArrayList<>();
    @BeforeEach
    void setUp() {
        orderID = UUID.randomUUID();
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        transactionID = UUID.randomUUID();
        transactionID2 = UUID.randomUUID();

        awaitingPayment = OrderStatus.VERIFYING;

        item = new Item("Heinz Tomato Sauce","123456","123456",storeID,36.99,1,"description","img/");
        listOfItems.add(item);
        order = new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, 0.0, OrderType.DELIVERY, awaitingPayment, listOfItems, 0.0, null, null, false);
        transaction = new Transaction(transactionID, Calendar.getInstance(), order, 0, "");
        transaction2 = new Transaction(transactionID2, Calendar.getInstance(), null, 0, "");
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests for when verify payment is done with a null request object- exception should be thrown")
    @DisplayName("When request object is null")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.verifyPayment(null));
        assertEquals(thrown.getMessage(), "The request object is null.");
    }

    @Test
    @Description("Tests for when the parameter of the request object is null- exception should be thrown")
    @DisplayName("When transaction ID is null")
    void UnitTest_testingNullParameterTransactionID(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.verifyPayment(new VerifyPaymentRequest(null)));
        assertEquals(thrown.getMessage(), "The transactionID of the request object is null.");
    }

    @Test
    @Description("Tests for when the transaction does not exist- exception should be thrown")
    @DisplayName("When transaction ID is invalid")
    void UnitTest_testingInvalidTransactionID(){
        when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.verifyPayment(new VerifyPaymentRequest(transactionID)));
        assertEquals(thrown.getMessage(), "There does not exist a transaction with the provided transactionID.");
    }

    @Test
    @Description("Tests for when the transaction does not have an order associated- exception should be thrown")
    @DisplayName("When order does not come with transaction")
    void UnitTest_testingNullOrderWithTransaction(){
        when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(transaction2));
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.verifyPayment(new VerifyPaymentRequest(transactionID2)));
        assertEquals(thrown.getMessage(), "There is no order associated with this transaction.");
    }

    @Test
    @Description("Tests for when the order with the transaction has no orderID- exception should be thrown")
    @DisplayName("When order does not come with an ID")
    void UnitTest_testingNullOrderID(){
        order.setOrderID(null);
        transaction2.setOrder(order);
        when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(transaction2));
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.verifyPayment(new VerifyPaymentRequest(transactionID2)));
        assertEquals(thrown.getMessage(), "The order has no orderID.");
    }

    @Test
    @Description("Tests for when the order with the transaction is not found in the database- exception should be thrown")
    @DisplayName("When order is not found in the database")
    void UnitTest_testingWhenOrderNotFoundInDatabase(){
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(transaction));
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->paymentService.verifyPayment(new VerifyPaymentRequest(transactionID)));
        assertEquals(thrown.getMessage(), "The order does not exist in the database.");
    }

    @Test
    @Description("Tests for when the order with the transaction has the wrong Order status- exception should be thrown")
    @DisplayName("When order has incorrect status")
    void UnitTest_testingWhenOrderHasIncorrectStatus(){
        order.setStatus(OrderStatus.PURCHASED);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(transaction));
        Throwable thrown = Assertions.assertThrows(StatusCodeException.class, ()->paymentService.verifyPayment(new VerifyPaymentRequest(transactionID)));
        assertEquals(thrown.getMessage(), "Invalid status code for order.");
    }

    @Test
    @Description("Tests for when the order with the transaction has the wrong Order status- exception should be thrown")
    @DisplayName("When everything should work fine")
    void UnitTest_testingWhenEverythingIsValid(){
        order.setStatus(OrderStatus.VERIFYING);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(transaction));
        Assertions.assertDoesNotThrow(()->paymentService.verifyPayment(new VerifyPaymentRequest(transactionID)));
    }
}
