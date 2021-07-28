package cs.superleague.payment;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.dataclass.Transaction;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.InvoiceRepo;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.repos.TransactionRepo;
import cs.superleague.payment.requests.GenerateInvoiceRequest;
import cs.superleague.payment.responses.GenerateInvoiceResponse;
import cs.superleague.shopping.dataclass.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
public class GenerateInvoiceUnitTest {

    @Mock
    TransactionRepo transactionRepo;

    @Mock
    InvoiceRepo invoiceRepo;

    @Mock
    OrderRepo orderRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // Order
    Order order;

    // Transaction
    Transaction transaction;

    // Item
    Item item;

    UUID orderID;
    UUID userID;
    UUID storeID;
    UUID shopperID;
    UUID transactionID;

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

        awaitingPayment = OrderStatus.AWAITING_PAYMENT;

        item = new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");
        listOfItems.add(item);
        order = new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, 0.0, OrderType.DELIVERY, awaitingPayment, listOfItems, 0.0, null, null, false);
        transaction = new Transaction(Calendar.getInstance(), order, 0);
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(null));
        assertEquals("Generate Invoice Request cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null transaction parameter")
    void UnitTest_TransactionID_inRequest_NullRequestObject(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(null, userID);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(request));
        assertEquals("Transaction ID cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null user id attribute")
    void UnitTest_UserID_inRequest_NullRequestObject(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(transactionID, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(request));
        assertEquals("Customer ID cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When the transactionID does not exist")
    void UnitTest_Invalid_TransactionID(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(UUID.randomUUID(), userID);

        Mockito.when(transactionRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.generateInvoice(request));
        assertEquals("Invalid transactionID passed in - transaction does not exist.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the transactionID exists")
    void UnitTest_Valid_TransactionID(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(transactionID, userID);

        Mockito.when(transactionRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(transaction));
        try{
            GenerateInvoiceResponse response =  paymentService.generateInvoice(request);
            assertEquals("Invoice successfully generated.", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}