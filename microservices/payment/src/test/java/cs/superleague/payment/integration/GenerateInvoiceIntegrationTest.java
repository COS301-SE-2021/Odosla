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
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    RabbitTemplate rabbitTemplate;

    Item item;
    CartItem cartItem;

    Order order;

    Transaction transaction;

    Invoice invoice;

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
    List<Item> listOfItems = new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();

    // Order List
    List<Order> listOfOrders = new ArrayList<>();

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
        item = new Item("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");
        cartItem = new CartItem("Heinz Tamatoe Sauce","123456","123456",storeID,36.99,1,"description","img/");


        rabbitTemplate.setChannelTransacted(true);
        SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(item);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);

        // Adding items to the items list
        listOfItems.add(item);
        cartItems.add(cartItem);


        // Assigning addresses
        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
        storeAddress = new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

        // Assigning order
        order = new Order();
        order.setOrderID(orderID);
        order.setUserID(userID);
        order.setStoreID(storeID);
        order.setShopperID(shopperID);
        order.setCreateDate(new Date());
        order.setTotalCost(totalCost);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setCartItems(cartItems);
        order.setDiscount(0.0);
        order.setDeliveryAddress(deliveryAddress);
        order.setStoreAddress(storeAddress);
        order.setRequiresPharmacy(false);

        transaction = new Transaction(transactionID, Calendar.getInstance(), order, 0);

        invoice = new Invoice(invoiceID, userID, Calendar.getInstance(), "Invoice Successfully Retrieved", 0.0, cartItems);
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
    void UnitTest_UserID_inRequest_NullRequestObject(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(transactionID, null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.generateInvoice(request));
        assertEquals("Customer ID cannot be null - Invoice generation unsuccessful", thrown.getMessage());
    }

    @Test
    @DisplayName("When the transactionID does not exist")
    void UnitTest_Invalid_TransactionID(){
        GenerateInvoiceRequest request = new GenerateInvoiceRequest(UUID.randomUUID(), userID);

        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> paymentService.generateInvoice(request));
        assertEquals("Invalid transactionID passed in - transaction does not exist.", thrown.getMessage());
    }

    @Test
    @DisplayName("When the transactionID exists")
    void UnitTest_Valid_TransactionID() {
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
