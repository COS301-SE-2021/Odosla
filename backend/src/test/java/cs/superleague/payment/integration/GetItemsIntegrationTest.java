package cs.superleague.payment.integration;

import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetItemsRequest;
import cs.superleague.payment.responses.GetItemsResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class GetItemsIntegrationTest {
    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ItemRepo itemRepo;

    Item I1;
    Item I2;
    Order o;
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
    GeoPoint storeAddress = new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems = new ArrayList<>();
    List<Item> newListOfItems = new ArrayList<>();
    List<Order> listOfOrders = new ArrayList<>();

    @BeforeEach
    void setUp() {
        I1 = new Item("Heinz Tamatoe Sauce", "123456", "123456", expectedS1, 36.99, 1, "description", "img/");
        I2 = new Item("Bar one", "012345", "012345", expectedS1, 14.99, 3, "description", "img/");
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
        o = new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2 = new Order(o2UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);
        itemRepo.save(I1);
        itemRepo.save(I2);
        orderRepo.saveAll(listOfOrders);
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

        GetItemsRequest request = new GetItemsRequest(o1UUID.toString());

        try {
            GetItemsResponse response = paymentService.getItems(request);
            assertEquals("Items successfully retrieved", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getItems());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
