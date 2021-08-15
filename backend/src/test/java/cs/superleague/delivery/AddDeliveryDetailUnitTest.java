package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.AddDeliveryDetailRequest;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.responses.AddDeliveryDetailResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.repos.StoreRepo;
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
public class AddDeliveryDetailUnitTest {
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Mock
    DeliveryRepo deliveryRepo;
    @Mock
    DeliveryDetailRepo deliveryDetailRepo;
    @Mock
    AdminRepo adminRepo;
    @Mock
    DriverRepo driverRepo;
    @Mock
    CustomerRepo customerRepo;
    @Mock
    StoreRepo storeRepo;

    UUID deliveryID;
    UUID orderID;
    Calendar time;
    Delivery delivery;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    double cost;

    @BeforeEach
    void setUp(){
        cost = 0.0;
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);

    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){
        AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(status, "detail", deliveryID, time);
        assertEquals(request.getDeliveryID(), deliveryID);
        assertEquals(request.getDetail(), "detail");
        assertEquals(request.getStatus(), status);
        assertEquals(request.getTimestamp(), time);
    }

    @Test
    @Description("Tests null request object, should throw an error.")
    @DisplayName("Null request object")
    void nullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.addDeliveryDetail(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests null parameters of request object.")
    @DisplayName("Null parameters")
    void nullParametersInRequestObject_UnitTest(){
        AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(status, null, deliveryID, time);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.addDeliveryDetail(request));
        assertEquals("Null parameters.", thrown.getMessage());
        AddDeliveryDetailRequest request2 = new AddDeliveryDetailRequest(status, "details", null, time);
        thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.addDeliveryDetail(request2));
        assertEquals("Null parameters.", thrown.getMessage());
        AddDeliveryDetailRequest request3 = new AddDeliveryDetailRequest(status, "details", deliveryID, null);
        thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.addDeliveryDetail(request3));
        assertEquals("Null parameters.", thrown.getMessage());
    }

    @Test
    @Description("Invalid deliveryID should throw an exception as nothing found in database.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryID_UnitTest(){
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(status, "detail", deliveryID, time);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.addDeliveryDetail(request));
        assertEquals("Delivery does not exist in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests the adding of a valid detail.")
    @DisplayName("Valid detail being added")
    void validDetailsBeingAdded_UnitTest() throws InvalidRequestException {
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        AddDeliveryDetailRequest request = new AddDeliveryDetailRequest(status, "detail", deliveryID, time);
        AddDeliveryDetailResponse response = deliveryService.addDeliveryDetail(request);
        assertEquals(response.getMessage(), "Delivery details added successfully.");
        assertEquals(response.getId(), 0);
    }
}
