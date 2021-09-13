package cs.superleague.delivery;

import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.requests.GetDeliveryCostRequest;
import cs.superleague.delivery.responses.GetDeliveryCostResponse;
import cs.superleague.delivery.stub.dataclass.GeoPoint;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    @Description("Tests for when invalid latitude drop off geoPoint, should throw an error.")
    @DisplayName("Invalid latitude drop off geoPoints")
    void invalidLatitudeDropOffGeoPointsBeingPassedIn_UnitTest(){
        dropOffLocation = new GeoPoint(91.0, 0.0, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(request1));
        assertEquals("Invalid Co-ordinates.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when invalid longitude drop off geoPoint, should throw an error.")
    @DisplayName("Invalid longitude drop off geoPoints")
    void invalidLongitudeDropOffGeoPointsBeingPassedIn_UnitTest(){
        dropOffLocation = new GeoPoint(89.0, -181.0, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(request1));
        assertEquals("Invalid Co-ordinates.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when invalid latitude pick up geoPoint, should throw an error.")
    @DisplayName("Invalid latitude pick up geoPoints")
    void invalidLatitudePickUpGeoPointsBeingPassedIn_UnitTest(){
        pickUpLocation = new GeoPoint(-91.0, 0.0, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(request1));
        assertEquals("Invalid Co-ordinates.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when invalid longitude pick up geoPoint, should throw an error.")
    @DisplayName("Invalid longitude pick up geoPoints")
    void invalidLongitudePickUpGeoPointsBeingPassedIn_UnitTest(){
        dropOffLocation = new GeoPoint(89.0, 181.0, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryCost(request1));
        assertEquals("Invalid Co-ordinates.", thrown1.getMessage());
    }

    @Test
    @Description("Tests for when valid geoPoints, distance below 20km.")
    @DisplayName("Valid geoPoints, distance below 20")
    void ValidGeoPointsBeingPassedInBelowTwentyKm_UnitTest() throws InvalidRequestException {
        dropOffLocation = new GeoPoint(0.1, 0.1, "");
        pickUpLocation = new GeoPoint(0.2, 0.2, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        GetDeliveryCostResponse response = deliveryService.getDeliveryCost(request1);
        assertEquals(response.getCost(), 20.0);
    }

    @Test
    @Description("Tests for when valid geoPoints, distance above 20km but below 40km.")
    @DisplayName("Valid geoPoints, distance above 20, below 40")
    void ValidGeoPointsBeingPassedInBelowFortyKmAboveTwenty_UnitTest() throws InvalidRequestException {
        dropOffLocation = new GeoPoint(0.1, 0.1, "");
        pickUpLocation = new GeoPoint(0.3, 0.3, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        GetDeliveryCostResponse response = deliveryService.getDeliveryCost(request1);
        assertEquals(response.getCost(), 35.0);
    }

    @Test
    @Description("Tests for when valid geoPoints, distance above 40km.")
    @DisplayName("Valid geoPoints, distance above 40")
    void ValidGeoPointsBeingPassedInAboveFortyKm_UnitTest() throws InvalidRequestException {
        dropOffLocation = new GeoPoint(0.1, 0.1, "");
        pickUpLocation = new GeoPoint(0.4, 0.4, "");
        GetDeliveryCostRequest request1 = new GetDeliveryCostRequest(dropOffLocation, pickUpLocation);
        GetDeliveryCostResponse response = deliveryService.getDeliveryCost(request1);
        assertEquals(response.getCost(), 50.0);
    }
}
