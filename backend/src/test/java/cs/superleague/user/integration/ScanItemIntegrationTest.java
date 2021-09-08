package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.requests.ScanItemRequest;
import cs.superleague.user.responses.ScanItemResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ScanItemIntegrationTest {

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
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
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

    ScanItemRequest request;
    ScanItemResponse response;

    @BeforeEach
    void setUp()
    {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        itemRepo.save(i1);
        itemRepo.save(i2);
        listOfItems.add(i1);
        listOfItems.add(i2);

        totalC=133.99;
        expectedDiscount=0.0;
        Calendar c1=Calendar.getInstance();
        Date d1=new Date(2021,06,1,14,30);
        c1.setTime(d1);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, Calendar.getInstance(), c1, totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, listOfItems, expectedDiscount, deliveryAddress, storeAddress, false);
        orderRepo.save(o);

        shopper = new Shopper();
        shopper.setShopperID(expectedShopper1);
        shopper.setName("JJ");

        shopperRepo.save(shopper);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when ScanItem is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_ScanItemNullRequestObject(){
        request = null;
        assertThrows(InvalidRequestException.class, ()-> {
            response = userService.scanItem(request);
        });
    }

    @Test
    @Description("Tests for when scanItem is submitted with a null request parameter- exception should be thrown")
    @DisplayName("When orderID parameter is not specified")
    void IntegrationTest_testingNullRequestOrderIDParameter(){
        request = new ScanItemRequest("123456", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Order ID is null in ScanItemRequest request - could not retrieve order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when scanItem is submitted with a null request parameter- exception should be thrown")
    @DisplayName("When barcode parameter is not specified")
    void IntegrationTest_testingNullRequestBarcodeParameter(){
        request = new ScanItemRequest(null, o1UUID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Barcode is null in ScanItemRequest request - could not scan item", thrown.getMessage());
    }

    @Test
    @Description("Tests for when scanItem orderID doesn't exist - exception should be thrown")
    @DisplayName("When orderID does not exist")
    void IntegrationTest_testingInvalidOrder(){
        request = new ScanItemRequest("123456", UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.scanItem(request));
        assertEquals("Order with ID does not exist in repository - could not get Order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when barcode doesn't match any items in the order - exception should be thrown")
    @DisplayName("When barcode does not exist")
    void IntegrationTest_testingInvalidItem(){
        request = new ScanItemRequest("123459", o1UUID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Item barcode doesn't match any of the items in the order", thrown.getMessage());
    }

    @Test
    @Description("Test for when Item exists in database - should return correct response")
    @DisplayName("When Item exists and response is returned")
    void IntegrationTest_Item_exist_successful_scanItem() throws InvalidRequestException, OrderDoesNotExist {
        request = new ScanItemRequest("123456", o1UUID);
        response= userService.scanItem(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Item successfully scanned",response.getMessage());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the ScanItem request object was created correctly")
    @DisplayName("ScanItem correctly constructed")
    void IntegrationTest_ScanItemRequestConstruction() {
        request = new ScanItemRequest("123456", o1UUID);
        assertNotNull(request);
    }
}
