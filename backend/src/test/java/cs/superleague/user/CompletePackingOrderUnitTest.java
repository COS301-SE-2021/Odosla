package cs.superleague.user;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetNextQueuedRequest;
import cs.superleague.shopping.requests.GetShoppersRequest;
import cs.superleague.shopping.responses.GetNextQueuedResponse;
import cs.superleague.shopping.responses.GetShoppersResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.User;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.responses.CompletePackagingOrderResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompletePackingOrderUnitTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private UserServiceImpl userService;

    Shopper shopper;
    Order o;
    UUID o1UUID=UUID.randomUUID();
    UUID storeUUID1= UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    Double totalC;
    Item i1;
    Item i2;
    List<Item> listOfItems=new ArrayList<>();
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworths, Hillcrest Boulevard");

    @BeforeEach
    void setUp()
    {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        totalC=133.99;
        expectedDiscount=0.0;
        Calendar c1=Calendar.getInstance();
        Date d1=new Date(2021,06,1,14,30);
        c1.setTime(d1);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), c1, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, listOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        shopper = new Shopper();
        shopper.setShopperID(expectedShopper1);
        shopper.setName("JJ");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when completePackingOrderRequest is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completePackagingOrder(null));
        assertEquals("CompletePackagingOrderRequest is null - could not fetch order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when orderID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -orderID - is not specified")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        CompletePackagingOrderRequest request=new CompletePackagingOrderRequest(null, true);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completePackagingOrder(request));
        assertEquals("OrderID is null in CompletePackagingOrderRequest request - could not retrieve order entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Order with orderID does not exist in database - OrderDoesNotExist Exception should be thrown")
    @DisplayName("When Order with ID doesn't exist")
    void UnitTest_Order_doesnt_exist(){

        CompletePackagingOrderRequest request=new CompletePackagingOrderRequest(UUID.randomUUID(), true);
        when(orderRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.completePackagingOrder(request));
        assertEquals("Order with ID does not exist in repository - could not get Order entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when order does exist")
    @DisplayName("When Order with ID does exist")
    void UnitTest_Order_does_exist() throws OrderDoesNotExist, cs.superleague.user.exceptions.InvalidRequestException {

        CompletePackagingOrderRequest request= new CompletePackagingOrderRequest(expectedShopper1, true);
        when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(o));
        CompletePackagingOrderResponse response= userService.completePackagingOrder(request);
        assertNotNull(response);
        assertEquals(true,response.isSuccess());
        assertEquals("Order entity with corresponding ID is ready for collection",response.getMessage());
    }


}
