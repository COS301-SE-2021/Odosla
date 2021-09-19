//package cs.superleague.user.integration;
//
//import cs.superleague.payment.dataclass.GeoPoint;
//import cs.superleague.payment.dataclass.Order;
//import cs.superleague.payment.dataclass.OrderStatus;
//import cs.superleague.payment.dataclass.OrderType;
//import cs.superleague.payment.exceptions.OrderDoesNotExist;
//import cs.superleague.payment.repos.OrderRepo;
//import cs.superleague.shopping.dataclass.Item;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.shopping.repos.StoreRepo;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.shopping.repos.ItemRepo;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.CompletePackagingOrderRequest;
//import cs.superleague.user.responses.CompletePackagingOrderResponse;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Description;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class CompletePackingOrderIntegrationTest {
//
//    @Autowired
//    ItemRepo itemRepo;
//
//    @Autowired
//    OrderRepo orderRepo;
//
//    @Autowired
//    ShopperRepo shopperRepo;
//
//    @Autowired
//    CustomerRepo customerRepo;
//
//    @Autowired
//    StoreRepo storeRepo;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    Shopper shopper;
//    Order o;
//    Order o2;
//    UUID o1UUID=UUID.randomUUID();
//    UUID o2UUID=UUID.randomUUID();
//    UUID expectedU1=UUID.randomUUID();
//    UUID expectedS1=UUID.randomUUID();
//    UUID expectedShopper1=UUID.randomUUID();
//    Double expectedDiscount;
//    Double totalC;
//    Item i1;
//    Item i2;
//    List<Item> listOfItems=new ArrayList<>();
//    String expectedMessage;
//    OrderStatus expectedStatus;
//    OrderType expectedType;
//    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
//    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworths, Hillcrest Boulevard");
//
//    CompletePackagingOrderRequest request;
//    CompletePackagingOrderResponse response;
//
//    UUID storeUUID1 = UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f7a27f");
//
//    Customer customer;
//
//    @BeforeEach
//    void setUp()
//    {
//        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
//        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
//        itemRepo.save(i1);
//        itemRepo.save(i2);
//        listOfItems.add(i1);
//        listOfItems.add(i2);
//
//        totalC=133.99;
//        expectedDiscount=0.0;
//        Calendar c1=Calendar.getInstance();
//        Date d1=new Date(2021,06,1,14,30);
//        c1.setTime(d1);
//
//        customer = new Customer();
//        customer.setName("Adam");
//        customer.setSurname("Isenberg");
//        customer.setCustomerID(UUID.fromString("7bc59ea6-aa30-465d-bcab-64e894bef586"));
//        customer.setAccountType(UserType.CUSTOMER);
//        customer.setEmail("adamisenberg@gmail.com");
//        customer.setPassword("fhkjdfh534534!");
//        customer.setPhoneNumber("0835233041");
//
//        customerRepo.save(customer);
//
//        Store store1=new Store(storeUUID1, -1, 24, "Pick n Pay", 2, 5, true, "shop/pnp.png");
//        GeoPoint store1Location = new GeoPoint(-25.762862391432126, 28.261305943073157, "Apple street");
//        store1.setStoreLocation(store1Location);
//        storeRepo.save(store1);
//
//        o=new Order(o1UUID, expectedU1, storeUUID1, expectedShopper1, Calendar.getInstance(), c1, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, listOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
//        o2=new Order(o2UUID, expectedU1, expectedS1, UUID.randomUUID(), Calendar.getInstance(), c1, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, listOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
//
//        o.setUserID(customer.getCustomerID());
//
//        orderRepo.save(o);
//        orderRepo.save(o2);
//
//        shopper = new Shopper();
//        shopper.setShopperID(expectedShopper1);
//        shopper.setName("JJ");
//
//        shopperRepo.save(shopper);
//
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    @Description("Tests for when completePackingOrder is submitted with a null request object- exception should be thrown")
//    @DisplayName("When request object is not specified")
//    void IntegrationTest_CompletePackingOrderNullRequestObject(){
//        request = null;
//
//        assertThrows(cs.superleague.user.exceptions.InvalidRequestException.class, ()-> {
//            response = userService.completePackagingOrder(request);
//        });
//    }
//
//    @Test
//    @Description("Tests for when completePackingOrder is submitted with a null request parameter- exception should be thrown")
//    @DisplayName("When orderID parameter is not specified")
//    void IntegrationTest_testingNullRequestOrderIDParameter(){
//        request = new CompletePackagingOrderRequest(null, true);
//        Throwable thrown = Assertions.assertThrows(cs.superleague.user.exceptions.InvalidRequestException.class, ()-> userService.completePackagingOrder(request));
//        assertEquals("OrderID is null in CompletePackagingOrderRequest request - could not retrieve order entity", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when completePackingOrder orderID doesnt exist - exception should be thrown")
//    @DisplayName("When orderID does not exist")
//    void IntegrationTest_testingInvalidOrder(){
//        request = new CompletePackagingOrderRequest(UUID.randomUUID(), true);
//        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.completePackagingOrder(request));
//        assertEquals("Order with ID does not exist in repository - could not get Order entity", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when completePackingOrder shopperID doesnt exist - exception should be thrown")
//    @DisplayName("When shopperID does not exist")
//    void IntegrationTest_testingInvalidShopper(){
//        request = new CompletePackagingOrderRequest(o2UUID, true);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completePackagingOrder(request));
//        assertEquals("Shopper with ID does not exist in repository - could not get Shopper entity", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Test for when Order exists in database - should return correct response")
//    @DisplayName("When Order exist and response is returned")
//    void IntegrationTest_Order_exist_completing_order() throws InvalidRequestException, OrderDoesNotExist, cs.superleague.delivery.exceptions.InvalidRequestException {
//        request = new CompletePackagingOrderRequest(o1UUID, true);
//        response= userService.completePackagingOrder(request);
//        assertNotNull(response);
//        assertTrue(response.isSuccess());
//        assertEquals("Order entity with corresponding ID is ready for collection",response.getMessage());
//
//    }
//
//    /** Checking request object is created correctly */
//    @Test
//    @Description("Tests whether the CompletePackingOrder request object was created correctly")
//    @DisplayName("CompletePackingOrderRequest correctly constructed")
//    void IntegrationTest_CompletePackingOrderRequestConstruction() {
//        request = new CompletePackagingOrderRequest(o1UUID, true);
//        assertNotNull(request);
//    }
//}
