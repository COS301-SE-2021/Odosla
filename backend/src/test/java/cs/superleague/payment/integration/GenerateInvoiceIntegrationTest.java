package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.InvoiceRepo;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.repos.TransactionRepo;
import cs.superleague.payment.requests.GenerateInvoiceRequest;
import cs.superleague.payment.responses.GenerateInvoiceResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.repos.CustomerRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
public class GenerateInvoiceIntegrationTest {

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

    @Autowired
    CustomerRepo customerRepo;

    Item item;

    Order order;

    Transaction transaction;

    Invoice invoice;

    Customer customer;

    // IDs
    UUID userID;
    UUID orderID;
    UUID storeID;
    UUID shopperID;
    UUID invoiceID;
    UUID transactionID;

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
        transactionID = UUID.randomUUID();

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

        customer = new Customer();
        customer.setEmail("u14254922@tuks.co.za");
        customer.setCustomerID(userID);
        customerRepo.save(customer);

        // Assigning order
        order =new Order(orderID, userID, storeID, shopperID, Calendar.getInstance(), null, totalCost, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, listOfItems, 0.0, deliveryAddress, storeAddress, false);

        transaction = new Transaction(transactionID, Calendar.getInstance(), order, 0);

        invoice = new Invoice(invoiceID, userID, Calendar.getInstance(), "Invoice Successfully Retrieved", 0.0, listOfItems);
        // adding orders to the orders list
        listOfOrders.add(order);

        orderRepo.save(order);
        transactionRepo.save(transaction);
        invoiceRepo.save(invoice);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(null));
        assertEquals("Generate Invoice Request cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null transaction parameter")
    void IntegrationTest_TransactionID_inRequest_NullRequestObject(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(null, userID);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(request));
        assertEquals("Transaction ID cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When request object has null user id attribute")
    void IntegrationTest_UserID_inRequest_NullRequestObject(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(transactionID, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(request));
        assertEquals("Customer ID cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When the transactionID does not exist")
    void IntegrationTest_Invalid_TransactionID(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(UUID.randomUUID(), userID);

        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.generateInvoice(request));
        assertEquals("Invalid transactionID passed in - transaction does not exist.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the transactionID exists")
    void IntegrationTest_Valid_TransactionID() {
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(transactionID, userID);

        try {
            GenerateInvoiceResponse response = paymentService.generateInvoice(request);
            assertEquals("Invoice successfully generated.", response.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
