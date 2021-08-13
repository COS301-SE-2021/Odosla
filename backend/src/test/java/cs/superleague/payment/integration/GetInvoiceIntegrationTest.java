package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.InvoiceRepo;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.repos.TransactionRepo;
import cs.superleague.payment.requests.GetInvoiceRequest;
import cs.superleague.payment.responses.GetInvoiceResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
public class GetInvoiceIntegrationTest {

    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    InvoiceRepo invoiceRepo;

    @Autowired
    ItemRepo itemRepo;

    Item item;

    Order order;

    Invoice invoice;

    // IDs
    UUID userID;
    UUID orderID;
    UUID storeID;
    UUID shopperID;
    UUID invoiceID;

    // total cost
    double totalCost;

    // Items List
    List<Item> listOfItems=new ArrayList<>();

    // Order List
    List<Order> listOfOrders=new ArrayList<>();

    // Addresses
    GeoPoint deliveryAddress;
    GeoPoint storeAddress;

    @BeforeEach
    void setUp() {

        // Assigning IDs
        orderID = UUID.randomUUID();
        userID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        shopperID = UUID.randomUUID();
        invoiceID = UUID.randomUUID();

        // Assigning total cost
        totalCost = 66.97;

        // Assigning items
        item=new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");

        itemRepo.save(item);

        // Adding items to the items list
        listOfItems.add(item);

        // Assigning addresses
        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
        storeAddress = new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

        // Assigning order
        order =new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, totalCost, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, listOfItems, 0.0, deliveryAddress, storeAddress, false);

        invoice = new Invoice(invoiceID, userID, Calendar.getInstance(), "Invoice Successfully Retrieved", 0.0, listOfItems);
        // adding orders to the orders list
        listOfOrders.add(order);

        orderRepo.save(order);
        invoiceRepo.save(invoice);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
        invoiceRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void Integration_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getInvoice(null));
        assertEquals("Get Invoice Request cannot be null - Invoice retrieval unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null invoiceID attribute")
    void IntegrationTest_InvoiceID_inRequest_NullRequestObject(){
        GetInvoiceRequest request = new GetInvoiceRequest(null, userID);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getInvoice(request));
        assertEquals("Invoice ID cannot be null - Invoice retrieval unsuccessful.", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null user id attribute")
    void Integration_UserID_inRequest_NullRequestObject(){
        GetInvoiceRequest request = new GetInvoiceRequest(invoiceID, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getInvoice(request));
        assertEquals("User ID cannot be null - Invoice retrieval unsuccessful.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the invoiceID does not exist")
    void IntegrationTest_Invalid_InvoiceID(){
        GetInvoiceRequest request = new GetInvoiceRequest(UUID.randomUUID(), userID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.getInvoice(request));
        assertEquals("Invalid invoiceID passed in - invoice does not exist.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the Invoice exists")
    void IntegrationTest_Valid_InvoiceID(){
        GetInvoiceRequest request = new GetInvoiceRequest(invoiceID, userID);

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
    void IntegrationTest_Unauthorised_User(){
        GetInvoiceRequest request = new GetInvoiceRequest(invoiceID, UUID.randomUUID());

        Throwable thrown = Assertions.assertThrows(NotAuthorisedException.class, () -> paymentService.getInvoice(request));
        assertEquals("Invalid customerID passed in - customer did not place this order.", thrown.getMessage());
    }
}