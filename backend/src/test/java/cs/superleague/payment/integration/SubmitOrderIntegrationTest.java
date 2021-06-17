package cs.superleague.payment.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SubmitOrderIntegrationTest {
    @Autowired
    PaymentServiceImpl paymentService;

    //OPTIONAL SERVICES
    @Autowired
    UserServiceImpl userService;

    @Autowired
    OrderRepo orderRepo;


    /* Requests */
    SubmitOrderRequest submitOrderRequest;

    /* User Ids */
    UUID userID=UUID.randomUUID();

    /* Store Ids */
    UUID storeID=UUID.randomUUID();

    /*Items */
    Item item1,item2,item3,item4,item5;
    List<Item> itemList;

    /*GeoPoints*/
    GeoPoint geoPoint1;
    GeoPoint geoPoint2;

    @BeforeEach
    void setup(){
        item1=new Item("name1","productID1","barcode1",storeID,10.0,2,"Description1","imageURL1");
        item2=new Item("name2","productID2","barcode2",storeID,30.0,1,"Description2","imageURL2");
        item3=new Item("name3","productID3","barcode3",storeID,27.0,1,"Description3","imageURL3");
        item4=new Item("name4","productID4","barcode4",storeID,22.0,1,"Description4","imageURL4");
        item5=new Item("name5","productID5","barcode5",storeID,22.0,1,"Description5","imageURL5");
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        geoPoint1=new GeoPoint();
        geoPoint2=new GeoPoint();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests whether the SubmitOrderRequest object is constructed correctly")
    @DisplayName("SubmitOrderRequest correct construction")
    void IntegrationTest_SubmitOrderRequestConstruction() {

        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,OrderType.DELIVERY,geoPoint1,geoPoint2);

        assertNotNull(submitOrderRequest);
        assertEquals(userID, submitOrderRequest.getUserID());
        assertEquals(itemList,submitOrderRequest.getListOfItems());
        assertEquals(3.0,submitOrderRequest.getDiscount());
        assertEquals(storeID,submitOrderRequest.getStoreID());
        assertEquals(geoPoint1,submitOrderRequest.getStoreAddress());
        assertEquals(geoPoint2,submitOrderRequest.getDeliveryAddress());
        assertEquals(OrderType.DELIVERY,submitOrderRequest.getOrderType());
    }

    @Test
    @Description("This test tests whether an order is created correctly - should return valid data stored in order entity")
    @DisplayName("When Order is created correctly")
    void IntegrationTest_CreatOrderConstruction(){
        Order order;
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,OrderType.DELIVERY,geoPoint1,geoPoint2);
        //submitOrderRequest= ServiceSelector.Get
    }
}
