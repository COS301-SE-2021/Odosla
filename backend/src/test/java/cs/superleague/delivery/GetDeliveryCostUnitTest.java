package cs.superleague.delivery;

import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryCostRequest;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GetDeliveryCostUnitTest {
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    GeoPoint dropOffLocation;
    GeoPoint pickUpLocation;

    @BeforeEach
    void setUp(){
        dropOffLocation = new GeoPoint(0.0,0.0,"address");
        pickUpLocation = new GeoPoint(1.0,1.0,"address");
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){
        GetDeliveryCostRequest request = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        assertEquals(request.getDropOffLocation(), dropOffLocation);
        assertEquals(request.getPickUpLocation(), pickUpLocation);
    }

    @Test
    @Description("Tests null request object, should throw an error.")
    @DisplayName("Null request object")
    void nullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests null parameters in request object, should throw an error.")
    @DisplayName("Null parameters")
    void nullParametersInRequestObject_UnitTest(){
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(null, pickUpLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(request1));
        assertEquals("Null parameters.", thrown1.getMessage());

        GetDeliveryCostRequest request2 = new GetDeliveryCostRequest(dropOffLocation, null);
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(request2));
        assertEquals("Null parameters.", thrown2.getMessage());
    }

    @Test
    @Description("Tests for when invalid geoPoints, should throw an error.")
    @DisplayName("Invalid geoPoints")
    void invalidGeoPointsBeingPassedIn_UnitTest(){

    }


}
