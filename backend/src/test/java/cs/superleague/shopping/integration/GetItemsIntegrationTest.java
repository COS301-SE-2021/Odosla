package cs.superleague.shopping.integration;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetItemsRequest;
import cs.superleague.shopping.responses.GetItemsResponse;
import cs.superleague.user.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.*;

import cs.superleague.integration.ServiceSelector;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GetItemsIntegrationTest {
    @Autowired
    ShoppingServiceImpl shoppingService;

    //OPTIONAL SERVICES
    @Autowired
    UserServiceImpl userService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    /* Requests */
    GetItemsRequest getItemsRequest;

    /* Store Ids */
    UUID storeID=UUID.randomUUID();

    /*Items */
    Item item1,item2;
    List<Item> listOfItems=new ArrayList<>();

    /*Store*/
    Store store;

    /*Catalogue*/
    Catalogue catalogue;

    @BeforeEach
    void setup(){
        item1=new Item("Heinz Tomato Sauce","123456","123456",storeID,36.99,1,"description","img/");
        item2=new Item("Bar one","012345","012345",storeID,14.99,3,"description","img/");
        listOfItems.add(item1);
        listOfItems.add(item2);
        catalogue=new Catalogue(storeID, listOfItems);
        store=new Store(storeID,"Woolworths",catalogue,2,null,null,4,false);
        store.setOpeningTime(7);
        store.setClosingTime(22);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();
    }

    @Test
    @Description("Tests if the given GetItems request when getItemsRequest is null - exception should be thrown")
    @DisplayName("When order request object null")
    void IntegrationTest_GetItemsNullRequest() throws InvalidRequestException {
        getItemsRequest = null;

        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            GetItemsResponse getItemsResponse = ServiceSelector.getShoppingService().getItems(getItemsRequest);
        });
    }

    @Test
    @Description("Tests for if the given order request on order creation has a null parameter - exception should be thrown")
    @DisplayName("When order parameter of request object null")
    void IntegrationTest_CreateOrderWithNullParamterRequest() throws InvalidRequestException {
        submitOrderRequest = new SubmitOrderRequest(null,itemList,3.0,storeID, OrderType.DELIVERY,geoPoint1,geoPoint2);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
        submitOrderRequest=new SubmitOrderRequest(userID,null,3.0,storeID,OrderType.DELIVERY,geoPoint1,geoPoint2);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,null,storeID,OrderType.DELIVERY,geoPoint1,geoPoint2);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,null,OrderType.DELIVERY,geoPoint1,geoPoint2);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,null,geoPoint1,geoPoint2);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,OrderType.DELIVERY,null,geoPoint2);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,OrderType.DELIVERY,geoPoint1,null);
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the store does not exist")
    @DisplayName("When order parameter gives id of a store that does not exist")
    void IntegrationTest_StoreDoesNotExist() throws InvalidRequestException {
        UUID storeID2=UUID.randomUUID();
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID2,OrderType.DELIVERY,geoPoint1,geoPoint2);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the store does is closed")
    @DisplayName("When store with store ID is closed")
    void IntegrationTest_StoreDoesisClosed() throws InvalidRequestException {
        storeRepo.deleteAll();
        store.setOpen(false);
        storeRepo.save(store);
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,OrderType.DELIVERY,geoPoint1,geoPoint2);
        assertThrows(StoreClosedException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
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
        assertEquals(geoPoint2,submitOrderRequest.getStoreAddress());
        assertEquals(geoPoint1,submitOrderRequest.getDeliveryAddress());
        assertEquals(OrderType.DELIVERY,submitOrderRequest.getOrderType());
    }

    @Test
    @Description("This test tests whether an order is created correctly - should return valid data stored in order entity")
    @DisplayName("When Order is created correctly")
    void IntegrationTest_CreatOrderConstruction() throws PaymentException, StoreClosedException, InvalidRequestException, StoreDoesNotExistException {
        Order order=null;
        submitOrderRequest=new SubmitOrderRequest(userID,itemList,3.0,storeID,OrderType.DELIVERY,geoPoint1,geoPoint2);
        SubmitOrderResponse submitOrderResponse= ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);

        if(orderRepo.findById(submitOrderResponse.getOrder().getOrderID()).isPresent()){
            order=orderRepo.findById(submitOrderResponse.getOrder().getOrderID()).orElse(null);
        }
        assertNotNull(order);
        assertEquals(userID, order.getUserID());
        assertEquals(itemList.size(),order.getItems().size());
        Iterator<Item> itemIterator_it = itemList.iterator();
        Iterator<Item> orderItemIterator_it = order.  getItems().iterator();
        while(itemIterator_it.hasNext() && orderItemIterator_it.hasNext()){
            Item item=itemIterator_it.next();
            Item orderItem=orderItemIterator_it.next();
            assertEquals(item.getPrice(),orderItem.getPrice());
            assertEquals(item.getQuantity(),orderItem.getQuantity());
            assertEquals(item.getBarcode(),orderItem.getBarcode());
            assertEquals(item.getStoreID(),orderItem.getStoreID());
            assertEquals(item.getDescription(),orderItem.getDescription());
            assertEquals(item.getProductID(),orderItem.getProductID());
            assertEquals(item.getImageUrl(),orderItem.getImageUrl());
        }

        assertEquals(3.0,order.getDiscount());
        assertEquals(storeID,order.getStoreID());
        //assertEquals(geoPoint2.getGeoID(),order.getStoreAddress().getGeoID());
        assertEquals(geoPoint2.getAddress(),order.getStoreAddress().getAddress());
        assertEquals(geoPoint2.getLatitude(),order.getStoreAddress().getLatitude());
        assertEquals(geoPoint2.getLongitude(),order.getStoreAddress().getLongitude());
        //assertEquals(geoPoint1.getGeoID(),order.getDeliveryAddress().getGeoID());
        assertEquals(geoPoint1.getAddress(),order.getDeliveryAddress().getAddress());
        assertEquals(geoPoint1.getLatitude(),order.getDeliveryAddress().getLatitude());
        assertEquals(geoPoint1.getLongitude(),order.getDeliveryAddress().getLongitude());
        assertEquals(OrderType.DELIVERY,order.getType());
        assertEquals(OrderStatus.AWAITING_PAYMENT,order.getStatus());
    }

}


