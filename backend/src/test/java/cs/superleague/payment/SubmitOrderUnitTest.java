package cs.superleague.payment;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetStoreByUUIDRequest;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class SubmitOrderUnitTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private StoreRepo storeRepo;

    @Mock(name = "ShoppingServiceImpl")
    private ShoppingService shoppingService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

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
    Store expectedStore;
    Catalogue c;
    String jwtToken;
    Customer customer;

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
        c=new Catalogue(expectedS1,expectedListOfItems);
        expectedStore=new Store(expectedS1,"Woolworthes",c,3,listOfOrders,null,4,true);
        expectedStore.setStoreLocation(storeAddress);
        customer=new Customer();
        customer.setCustomerID(expectedU1);
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);
        jwtToken=jwtTokenUtil.generateJWTTokenCustomer(customer);
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
        SubmitOrderRequest request=new SubmitOrderRequest(list,0.0, UUID.randomUUID(), OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("JwtToken cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for listOfItems in request object- exception should be thrown")
    @DisplayName("When request object parameter -listOfItems - is not specificed")
    void UnitTest_testingNull_listOfItems_Parameter_RequestObject(){
        SubmitOrderRequest request=new SubmitOrderRequest(null,0.0, UUID.randomUUID(), OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("List of items cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for discount in request object- exception should be thrown")
    @DisplayName("When request object parameter - discount - is not specificed")
    void UnitTest_testingNull_discount_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(list,null, UUID.randomUUID(), OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Discount cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for storeID in request object- exception should be thrown")
    @DisplayName("When request object parameter - storeID - is not specificed")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(list,0.0, null, OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Store ID cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for orderType in request object- exception should be thrown")
    @DisplayName("When request object parameter - orderType - is not specificed")
    void UnitTest_testingNull_orderType_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        SubmitOrderRequest request=new SubmitOrderRequest(list,0.0, UUID.randomUUID(), null, 3.3, 3.5, "Homer Street");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Order type cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
    }

//    @Test
//    @Description("Tests for whether an order is submited with a null parameter for storeAddress in request object- exception should be thrown")
//    @DisplayName("When request object parameter - storeAddress - is not specificed")
//    void UnitTest_testingNull_storeAddress_Parameter_RequestObject(){
//        List<Item> list=new ArrayList<>();
//        SubmitOrderRequest request=new SubmitOrderRequest(UUID.randomUUID(),list,0.0, UUID.randomUUID(), expectedType);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> paymentService.submitOrder(request));
//        assertEquals("Store Address GeoPoint cannot be null in request object - order unsuccessfully created.", thrown.getMessage());
//    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the SubmitOrderRequest object was created correctly")
    @DisplayName("SubmitOrderRequest correctly constructed")
    void UnitTest_SubmitOrderRequestConstruction() {

        SubmitOrderRequest request=new SubmitOrderRequest(expectedListOfItems,expectedDiscount,expectedS1,expectedType, 3.3, 3.5, "Homer Street");

        assertNotNull(request);
        assertEquals(expectedListOfItems,request.getListOfItems());
        assertEquals(expectedDiscount,request.getDiscount());
        assertEquals(expectedS1,request.getStoreID());
        assertEquals(expectedType,request.getOrderType());
    }

    @Test
    @Description("This test is to check if the invalid request exception for finding store by UUID - throw Invalid exception from shoppingService")
    @DisplayName("Exception for Store doesn't exist")
    void UnitTest_IvalidRequest_ShoppingService() throws cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException {
        SubmitOrderRequest request=new SubmitOrderRequest(expectedListOfItems,expectedDiscount,expectedS1,expectedType, 3.3, 3.5, "Homer Street");
        //when(orderRepo.findById(Mockito.any())).thenReturn(null);
        when(shoppingService.getStoreByUUID(Mockito.any())).thenThrow(new cs.superleague.shopping.exceptions.InvalidRequestException("Invalid submit order request received - order unsuccessfully created."));
        Throwable thrown = Assertions.assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Invalid submit order request received - order unsuccessfully created.",thrown.getMessage());
    }
    /** Checking if Store Does not exist exception */
    @Test
    @Description("This test is to check if the store with ID does not exist - throw Store Does Not exist exception")
    @DisplayName("Exception for Store doesn't exist")
    void UnitTest_StoreDoesNotExist() throws cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, PaymentException, StoreClosedException {
        SubmitOrderRequest request=new SubmitOrderRequest(expectedListOfItems,expectedDiscount,expectedS1,expectedType, 3.3, 3.5, "Homer Street");
        //when(orderRepo.findById(Mockito.any())).thenReturn(null);
        GetStoreByUUIDRequest storeRequest=new GetStoreByUUIDRequest(expectedS1);
        when(shoppingService.getStoreByUUID(Mockito.any())).thenThrow(new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Store entity"));
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> paymentService.submitOrder(request));
        assertEquals("Store with ID does not exist in repository - could not get Store entity",thrown.getMessage());

    }

//    @Test
//    @Description("This test is to check if the store with ID is returned but closed - throw Store Closed exception")
//    @DisplayName("Exception for Store is closed")
//    void UnitTest_StoreIsClosed() throws cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, PaymentException, StoreClosedException {
//        SubmitOrderRequest request=new SubmitOrderRequest(jwtToken,expectedListOfItems,expectedDiscount,expectedS1,expectedType, 3.3, 3.5, "Homer Street");
//        when(orderRepo.findById(Mockito.any())).thenReturn(null);
//        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
//        GetStoreByUUIDRequest storeRequest=new GetStoreByUUIDRequest(expectedS1);
//        expectedStore.setOpen(false);
//        GetStoreByUUIDResponse storeResponse=new GetStoreByUUIDResponse(expectedStore,Calendar.getInstance().getTime(), "Store successfully returned");
//        when(shoppingService.getStoreByUUID(Mockito.any())).thenReturn(storeResponse);
//        Throwable thrown = Assertions.assertThrows(StoreClosedException.class, ()-> paymentService.submitOrder(request));
//        assertEquals("Store is currently closed - could not create order",thrown.getMessage());
//
//
//    }




    /** Checking response object is created correctly */
//    @Test
//    @Description("This test is to check order is created correctly- should return valid data stored in order entity")
//    @DisplayName("When Order is created correctly")
//    void UnitTest_StartOrderConstruction() throws PaymentException, StoreClosedException, cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, InterruptedException, cs.superleague.user.exceptions.InvalidRequestException {
//        SubmitOrderRequest request=new SubmitOrderRequest(jwtToken,expectedListOfItems,expectedDiscount,expectedS1,expectedType, 3.3, 3.5, "Homer Street");
//        when(orderRepo.findById(Mockito.any())).thenReturn(null);
//        GetStoreByUUIDResponse storeResponse=new GetStoreByUUIDResponse(expectedStore,Calendar.getInstance().getTime(), "Store successfully returned");
//        when(shoppingService.getStoreByUUID(Mockito.any())).thenReturn(storeResponse);
//        SubmitOrderResponse response=paymentService.submitOrder(request);
//        assertNotNull(response);
//        assertNotNull(o);
//        assertEquals(request.getListOfItems(),o.getItems());
//        assertEquals(request.getDiscount(),o.getDiscount());
//        assertEquals(request.getStoreID(),o.getStoreID());
//        assertEquals(request.getOrderType(),o.getType());
//
//        assertEquals("Order successfully created.", response.getMessage());
//        Order order=response.getOrder();
//        if (order!=null) {
//                     assertEquals(o.getTotalCost(), order.getTotalCost());
//                     assertEquals(o.getDiscount(), order.getDiscount());
//                     assertEquals(o.getItems(), order.getItems());
//                     //assertEquals(o.getDeliveryAddress(), order.getDeliveryAddress());
//                     assertEquals(null, order.getShopperID());
//                     assertEquals(o.getType(), order.getType());
//        }
//    }
}
