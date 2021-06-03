package payment;

import payment.dataclass.*;
import payment.exceptions.PaymentException;
import payment.dataclass.Order;
import payment.dataclass.OrderType;
import payment.dataclass.OrderStatus;
import payment.exceptions.InvalidRequestException;
import payment.repos.OrderRepo;
import payment.requests.SubmitOrderRequest;
import payment.responses.SubmitOrderResponse;
import shopping.dataclass.Item;
import shopping.dataclass.Catalogue;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import payment.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class SubmitOrderUnitTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private PaymentServiceImpl paymentService;

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
        totalC=81.96;
        expectedStatus = OrderStatus.AWAITING_PAYMENT;
        expectedType= OrderType.DELIVERY;
        expectedListOfItems.add(I1);
        expectedListOfItems.add(I2);
        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), null, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedListOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        listOfOrders.add(o);
        listOfOrders.add(o2);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
    }

    /** InvalidRequest tests */
    @Test
    @Description("Tests for whether an order is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(null));
        assertEquals("Invalid submit order request received - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for userID in request object- exception should be thrown")
    @DisplayName("When request object parameter -userID - is not specificed")
    void UnitTest_testingNull_UserID_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(null,list,0.0, UUID.randomUUID(), OrderType.DELIVERY,deliveryAddress,storeAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("UserID cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for listOfItems in request object- exception should be thrown")
    @DisplayName("When request object parameter -listOfItems - is not specificed")
    void UnitTest_testingNull_listOfItems_Parameter_RequestObject(){
        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),null,0.0, UUID.randomUUID(), OrderType.DELIVERY,deliveryAddress,storeAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("List of items cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for discount in request object- exception should be thrown")
    @DisplayName("When request object parameter - discount - is not specificed")
    void UnitTest_testingNull_discount_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),list,null, UUID.randomUUID(), OrderType.DELIVERY,deliveryAddress,storeAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Discount cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for storeID in request object- exception should be thrown")
    @DisplayName("When request object parameter - storeID - is not specificed")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),list,0.0, null, OrderType.DELIVERY,deliveryAddress,storeAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Store ID cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for orderType in request object- exception should be thrown")
    @DisplayName("When request object parameter - orderType - is not specificed")
    void UnitTest_testingNull_orderType_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),list,0.0, UUID.randomUUID(), null,deliveryAddress,storeAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Order type cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for delivery address in request object- exception should be thrown")
    @DisplayName("When request object parameter - delivery address - is not specificed")
    void UnitTest_testingNull_deliveryAddress_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),list,0.0, UUID.randomUUID(), expectedType,null,storeAddress);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Delivery Address GeoPoint cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for storeAddress in request object- exception should be thrown")
    @DisplayName("When request object parameter - storeAddress - is not specificed")
    void UnitTest_testingNull_storeAddress_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),list,0.0, UUID.randomUUID(), expectedType,deliveryAddress,null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    /** Checking response object is created correctly */
    @Test
    @Description("Tests whether the SubmitOrderRequest object was created correctly")
    @DisplayName("SubmitOrderRequest correctly constructed")
    void UnitTest_SubmitOrderRequestConstruction() {

        SubmitOrderRequest request=new SubmitOrderRequest(expectedU1,expectedListOfItems,expectedDiscount,expectedS1,expectedType,deliveryAddress,storeAddress);

        assertNotNull(request);
        assertEquals(expectedU1,request.getUserID());
        assertEquals(expectedListOfItems,request.getListOfItems());
        assertEquals(expectedDiscount,request.getDiscount());
        assertEquals(expectedS1,request.getStoreID());
        assertEquals(expectedType,request.getOrderType());
        assertEquals(deliveryAddress,request.getDeliveryAddress());
        assertEquals(storeAddress,request.getStoreAddress());
    }

    @Test
    @Description("This test is to check order is created correctly- should return valid data stored in order entity")
    @DisplayName("When Order is created correctly")
    void UnitTest_StartOrderConstruction() throws PaymentException {
        SubmitOrderRequest request=new SubmitOrderRequest(expectedU1,expectedListOfItems,expectedDiscount,expectedS1,expectedType,deliveryAddress,storeAddress);
        when(orderRepo.findById(Mockito.any())).thenReturn(null);
        SubmitOrderResponse response=paymentService.submitOrder(request);
        assertNotNull(response);
        assertNotNull(o);
        assertEquals(request.getUserID(),o.getUserID());
        assertEquals(request.getListOfItems(),o.getItems());
        assertEquals(request.getDiscount(),o.getDiscount());
        assertEquals(request.getStoreID(),o.getStoreID());
        assertEquals(request.getOrderType(),o.getType());
        assertEquals(request.getDeliveryAddress(),o.getDeliveryAddress());
        assertEquals(request.getStoreAddress(),o.getStoreAddress());

        assertEquals("Order successfully created.", response.getMessage());
        Order order=response.getOrder();
        if (order!=null) {
                     assertEquals(o.getTotalCost(), order.getTotalCost());
                     assertEquals(o.getDiscount(), order.getDiscount());
                     assertEquals(o.getItems(), order.getItems());
                     assertEquals(o.getStatus(), order.getStatus());
                     assertEquals(o.getDeliveryAddress(), order.getDeliveryAddress());
                     assertEquals(o.getStoreAddress(), order.getStoreAddress());
                     assertEquals(null, order.getShopperID());
                     assertEquals(o.getType(), order.getType());
        }
    }
}
