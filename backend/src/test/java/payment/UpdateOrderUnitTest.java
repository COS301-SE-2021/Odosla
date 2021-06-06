package payment;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import payment.exceptions.PaymentException;
import payment.exceptions.InvalidRequestException;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import payment.dataclass.GeoPoint;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.dataclass.OrderType;
import payment.exceptions.OrderDoesNotExist;
import payment.repos.OrderRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import shopping.dataclass.Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateOrderUnitTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepo orderRepo;

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

    @BeforeEach
    void setUp() {
        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        expectedMessage="Order successfully created.";
        expectedDiscount=0.0;
        totalC=66.97;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
    }

    @Test
    @Description("Tests for when an order is updated with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.updateOrder(null));
        assertEquals("Invalid update order request received - order unsuccessfully updated.", thrown.getMessage());
    }

}
