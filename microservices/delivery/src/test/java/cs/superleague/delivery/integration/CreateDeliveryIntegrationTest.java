//package cs.superleague.delivery.integration;
//
//import cs.superleague.delivery.DeliveryServiceImpl;
//import cs.superleague.delivery.dataclass.Delivery;
//import cs.superleague.delivery.dataclass.DeliveryStatus;
//import cs.superleague.delivery.exceptions.InvalidRequestException;
//import cs.superleague.delivery.repos.DeliveryRepo;
//import cs.superleague.delivery.requests.CreateDeliveryRequest;
//import cs.superleague.delivery.responses.CreateDeliveryResponse;
//import cs.superleague.delivery.stub.dataclass.GeoPoint;
//import cs.superleague.payment.dataclass.GeoPoint;
//import cs.superleague.payment.dataclass.Order;
//import cs.superleague.payment.dataclass.OrderStatus;
//import cs.superleague.payment.dataclass.OrderType;
//import cs.superleague.payment.repos.OrderRepo;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.shopping.repos.StoreRepo;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.repos.CustomerRepo;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Description;
//
//import javax.transaction.Transactional;
//import java.util.Calendar;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//public class CreateDeliveryIntegrationTest {
//    @Autowired
//    private DeliveryServiceImpl deliveryService;
//
//
//    UUID deliveryID;
//    UUID orderID;
//    Calendar time;
//    GeoPoint invalidDropOffLocation;
//    GeoPoint pickUpLocation;
//    GeoPoint dropOffLocation;
//    UUID customerID;
//    UUID storeID;
//    DeliveryStatus status;
//    double cost;
//    Customer customer;
//    Store store;
//    Order order;
//    UUID shopperID;
//
//    @BeforeEach
//    void setUp(){
//        cost = 0.0;
//        shopperID = UUID.randomUUID();
//        deliveryID = UUID.randomUUID();
//        orderID = UUID.randomUUID();
//        time = Calendar.getInstance();
//        status = DeliveryStatus.WaitingForShoppers;
//        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
//        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
//        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
//        customerID = UUID.randomUUID();
//        storeID = UUID.randomUUID();
//        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER,customerID);
//        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
//        store.setStoreLocation(pickUpLocation);
//        order = new Order(orderID, customerID, storeID, shopperID, Calendar.getInstance(), null, 50.0, OrderType.DELIVERY, OrderStatus.PURCHASED, null, 0.0, dropOffLocation, pickUpLocation, false);
//        orderRepo.save(order);
//        customerRepo.save(customer);
//        storeRepo.save(store);
//    }
//
//    @AfterEach
//    void tearDown(){
//        deliveryRepo.deleteAll();
//        storeRepo.deleteAll();
//        customerRepo.deleteAll();
//        orderRepo.deleteAll();
//    }
//
//    @Test
//    @Description("Tests for when customer is not in database, should throw an exception.")
//    @DisplayName("Invalid customer")
//    void invalidCustomerIDPassedInRequest_IntegrationTest(){
//        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, UUID.randomUUID(), storeID, time, dropOffLocation);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
//        assertEquals("Invalid customerID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the orderID is not found in the database, should throw an exception.")
//    @DisplayName("Invalid orderID")
//    void invalidOrderIDPassedInRequestObject_IntegrationTest(){
//        CreateDeliveryRequest request = new CreateDeliveryRequest(UUID.randomUUID(), customerID, storeID, time, dropOffLocation);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
//        assertEquals("Invalid orderID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the store is not found in the database, should throw an exception.")
//    @DisplayName("Invalid storeID")
//    void invalidStoreIDPassedInRequestObject_IntegrationTest(){
//        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, UUID.randomUUID(), time, dropOffLocation);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
//        assertEquals("Invalid storeID.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the store does not have a location saved, should throw an exception.")
//    @DisplayName("Null store location")
//    void nullStoreLocationOnStoreObjectFound_IntegrationTest(){
//        store.setStoreLocation(null);
//        storeRepo.save(store);
//        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
//        assertEquals("Store has no location set.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when valid delivery is placed.")
//    @DisplayName("Valid delivery created")
//    void validDeliveryCreatedAndSaved_IntegrationTest() throws InvalidRequestException{
//        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
//        CreateDeliveryResponse response = deliveryService.createDelivery(request);
//        assertEquals(response.isSuccess(), true);
//        assertEquals(response.getMessage(), "Delivery request placed.");
//        List<Delivery> delivery = deliveryRepo.findAll();
//        assertEquals(delivery.get(0).getDeliveryID(), response.getDeliveryID());
//    }
//}
