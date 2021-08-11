package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.CreateDeliveryRequest;
import cs.superleague.delivery.responses.CreateDeliveryResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateDeliveryUnitTest {
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Mock
    DeliveryRepo deliveryRepo;
    @Mock
    CustomerRepo customerRepo;
    @Mock
    StoreRepo storeRepo;
    @Mock
    OrderRepo orderRepo;

    UUID deliveryID;
    UUID orderID;
    UUID shopperID;
    Calendar time;
    Delivery delivery;
    GeoPoint invalidDropOffLocation;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    double cost;
    Customer customer;
    Store store;
    Order order;

    @BeforeEach
    void setUp(){
        cost = 0.0;
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER,customerID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true);
        store.setStoreLocation(pickUpLocation);
        order = new Order(orderID, customerID, storeID, shopperID, Calendar.getInstance(), null, 50.0, OrderType.DELIVERY, OrderStatus.PURCHASED, null, 0.0, dropOffLocation, pickUpLocation, false);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){
        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        assertEquals(request.getCustomerID(), customerID);
        assertEquals(request.getOrderID(), orderID);
        assertEquals(request.getPlaceOfDelivery(), dropOffLocation);
        assertEquals(request.getTimeOfDelivery(), time);
        assertEquals(request.getStoreID(), storeID);
    }

    @Test
    @Description("Tests null request object, should throw an error.")
    @DisplayName("Null request object")
    void nullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for null parameters, should throw an exceprion.")
    @DisplayName("Tests for null parameters")
    void requestObjectContainsNullParameters_UnitTest(){
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(null, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Missing parameters.", thrown1.getMessage());
        CreateDeliveryRequest request2 = new CreateDeliveryRequest(orderID, null, storeID, time, dropOffLocation);
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request2));
        assertEquals("Missing parameters.", thrown2.getMessage());
        CreateDeliveryRequest request3 = new CreateDeliveryRequest(orderID, customerID, null, time, dropOffLocation);
        Throwable thrown3 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request3));
        assertEquals("Missing parameters.", thrown3.getMessage());
        CreateDeliveryRequest request5 = new CreateDeliveryRequest(orderID, customerID, storeID, time, null);
        Throwable thrown5 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request5));
        assertEquals("Missing parameters.", thrown5.getMessage());
    }

    @Test
    @Description("Tests for when the customer ID is invalid.")
    @DisplayName("Invalid customer ID")
    void customerIDInRequestObjectNotValid_UnitTest(){
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid customerID.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the store ID is invalid.")
    @DisplayName("Invalid store ID")
    void storeIDInRequestObjectNotValid_UnitTest(){
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid storeID.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the store does not contain a valid location.")
    @DisplayName("Invalid store location")
    void invalidStoreLocationInRequestObject_UnitTest(){
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        store.setStoreLocation(null);
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, invalidDropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Store has no location set.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when the geoPoints are invalid.")
    @DisplayName("Invalid geoPoints")
    void invalidGeoPointsInRequestObject_UnitTest(){
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, invalidDropOffLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request1));
        assertEquals("Invalid geoPoints.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when all the data is valid.")
    @DisplayName("Successful run")
    void deliveryCreatedSuccessfully_UnitTest() throws InvalidRequestException {
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        when(customerRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(store));
        CreateDeliveryRequest request1 = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        CreateDeliveryResponse response = deliveryService.createDelivery(request1);
        assertEquals(response.getMessage(), "Delivery request placed.");
        assertEquals(response.isSuccess(), true);
    }
}
