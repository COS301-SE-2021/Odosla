package cs.superleague.payment;

import cs.superleague.payment.dataclass.Invoice;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.InvoiceRepo;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetInvoiceRequest;
import cs.superleague.payment.responses.GetInvoiceResponse;
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
public class GetInvoiceUnitTest {

    @Mock
    InvoiceRepo invoiceRepo;

    @Mock
    OrderRepo orderRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // Order
    Order order;

    // Invoice
    Invoice invoice;

    // Item
    Item item;

    UUID orderID;
    UUID userID;
    UUID storeID;
    UUID shopperID;
    UUID invoiceID;

    // Order statuses
    OrderStatus awaitingPayment;

    List<Item> listOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        orderID = UUID.randomUUID();
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        invoiceID = UUID.randomUUID();

        awaitingPayment = OrderStatus.AWAITING_PAYMENT;

        item = new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");
        listOfItems.add(item);
        order = new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, 0.0, OrderType.DELIVERY, awaitingPayment, listOfItems, 0.0, null, null, false);
        invoice = new Invoice(invoiceID, userID, Calendar.getInstance(), "Invoice Successfully Retrieved", 0.0, listOfItems);
    }

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getInvoice(null));
        assertEquals("Get Invoice Request cannot be null - Invoice retrieval unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null invoiceID attribute")
    void UnitTest_InvoiceID_inRequest_NullRequestObject(){
        GetInvoiceRequest request = new GetInvoiceRequest(null, userID);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getInvoice(request));
        assertEquals("Invoice ID cannot be null - Invoice retrieval unsuccessful.", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null user id attribute")
    void UnitTest_UserID_inRequest_NullRequestObject(){
        GetInvoiceRequest request = new GetInvoiceRequest(invoiceID, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getInvoice(request));
        assertEquals("User ID cannot be null - Invoice retrieval unsuccessful.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the invoiceID does not exist")
    void UnitTest_Invalid_InvoiceID(){
        GetInvoiceRequest request = new GetInvoiceRequest(UUID.randomUUID(), userID);

        Mockito.when(invoiceRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getInvoice(request));
        assertEquals("Invalid invoiceID passed in - invoice does not exist.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the invoice exists")
    void UnitTest_Valid_InvoiceID(){
        GetInvoiceRequest request = new GetInvoiceRequest(invoiceID, userID);

        Mockito.when(invoiceRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(invoice));
        try{
            GetInvoiceResponse response =  paymentService.getInvoice(request);
            assertEquals("Invoice Successfully Retrieved", response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When an unauthorised user attempts to getInvoice")
    void UnitTest_Unauthorised_User(){
        GetInvoiceRequest request = new GetInvoiceRequest(invoiceID, UUID.randomUUID());

        Mockito.when(invoiceRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(invoice));
        Throwable thrown = Assertions.assertThrows(NotAuthorisedException.class, () -> paymentService.getInvoice(request));
        assertEquals("Invalid customerID passed in - customer did not place this order.", thrown.getMessage());
    }
}