package cs.superleague.shopping.controller;

import cs.superleague.api.ShoppingApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.AddShopperRequest;
import cs.superleague.shopping.requests.GetItemsRequest;
import cs.superleague.shopping.requests.RemoveQueuedOrderRequest;
import cs.superleague.shopping.responses.AddShopperResponse;
import cs.superleague.shopping.responses.GetItemsResponse;
import cs.superleague.shopping.responses.RemoveQueuedOrderResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.shopping.requests.GetShoppersRequest;
import cs.superleague.shopping.responses.GetItemsResponse;
import cs.superleague.shopping.responses.GetShoppersResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.ShopperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class ShoppingController implements ShoppingApi{

    @Autowired
    ShoppingServiceImpl shoppingService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    OrderRepo orderRepo;

    UUID storeID = UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0");


    UUID userID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");


    UUID orderID = UUID.fromString("99134567-9CBC-FEF0-1254-56789ABCDEF0");


    private boolean mockMode = false;


    @Override
    public ResponseEntity<ShoppingAddShopperResponse> addShopper(ShoppingAddShopperRequest body) {

        //mock mem:db
        Store store1 = new Store();
        store1.setStoreID(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0"));
        store1.setShoppers(new ArrayList<>());
        storeRepo.save(store1);

        Shopper sh1 = new Shopper();
        sh1.setShopperID(userID);
        shopperRepo.save(sh1);

        //creating response object  and default return status
        ShoppingAddShopperResponse response = new ShoppingAddShopperResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            AddShopperRequest req = new AddShopperRequest(userID, storeID);
            AddShopperResponse addShopperResponse = ServiceSelector.getShoppingService().addShopper(req);

            try {
                response.setDate(addShopperResponse.getTimestamp().toString());
                response.setMessage(addShopperResponse.getMessage());
                response.setSuccess(addShopperResponse.isSuccess());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (cs.superleague.user.exceptions.InvalidRequestException e) {
            e.printStackTrace();
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
        } catch (StoreDoesNotExistException e) {
            e.printStackTrace();
        }
        catch (UserException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, status);

    }

    //getItems endpoint
    @Override
    public ResponseEntity<ShoppingGetItemsResponse> getItems(ShoppingGetItemsRequest body) {

        //add mock data to repo
        List<Item> mockItemList = new ArrayList<>();
        Item item1, item2;
        item1=new Item("Heinz Tomato Sauce","p234058925","91234567-9ABC-DEF0-1234-56789ABCDEFF",storeID,36.99,1,"description","img/");
        item2=new Item("Bar one","p123984123","62234567-9ABC-DEF0-1234-56789ABCDEFA", storeID,14.99,3,"description","img/");
        itemRepo.save(item1); itemRepo.save(item2);
        mockItemList.add(item1); mockItemList.add(item2);

        Catalogue c = new Catalogue();
        c.setStoreID(storeID);
        c.setItems(mockItemList);
        catalogueRepo.save(c);

        Store store1 = new Store();
        store1.setStock(c);

        store1.setStoreID(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0"));
        store1.setStock(c);
        storeRepo.save(store1);
        ///

        //creating response object and default return status:
        ShoppingGetItemsResponse response = new ShoppingGetItemsResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        if (mockMode){
            List<ItemObject> mockItems = new ArrayList<>();
            ItemObject a = new ItemObject();
            a.setName("mockA");
            ItemObject b = new ItemObject();
            b.setName("mockB");
            mockItems.add(a);
            mockItems.add(b);

            response.setItems(mockItems);
        } else {

            try {
                GetItemsResponse getItemsResponse = ServiceSelector.getShoppingService().getItems(new GetItemsRequest(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0")));
                try {

                    response.setItems(populateItems(getItemsResponse.getItems()));

                } catch (Exception e){
                    e.printStackTrace();
                }

            } catch (StoreDoesNotExistException e) {

            } catch (InvalidRequestException e) {

            }

        }

        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingRemoveQueuedOrderResponse> removeQueuedOrder(ShoppingRemoveQueuedOrderRequest body) {
        //mock mem:db

        List<Order> oq = new ArrayList<>();
        Order o1 = new Order(); o1.setOrderID(orderID); o1.setType(OrderType.DELIVERY);
        Order o2 = new Order(); o2.setOrderID(UUID.randomUUID()); o2.setType(OrderType.DELIVERY);
        oq.add(o1); oq.add(o2);
        orderRepo.save(o1);
        orderRepo.save(o2);

        Store store1 = new Store();
        store1.setStoreID(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0"));
        store1.setOrderQueue(oq);
        storeRepo.save(store1);

        //creating response object and default return status:
        ShoppingRemoveQueuedOrderResponse response = new ShoppingRemoveQueuedOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            RemoveQueuedOrderRequest req = new RemoveQueuedOrderRequest(orderID, storeID);
            RemoveQueuedOrderResponse removeQueuedOrderResponse = ServiceSelector.getShoppingService().removeQueuedOrder(req);

            try {
                response.setOrderID(removeQueuedOrderResponse.getOrderID().toString());
                response.setIsRemoved(removeQueuedOrderResponse.isRemoved());
                response.setMessage(removeQueuedOrderResponse.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (StoreDoesNotExistException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);

    }

    public ResponseEntity<ShoppingGetShoppersResponse> getShoppers(ShoppingGetShoppersRequest body) {
        //add mock data to repo
        List<Shopper> mockShopperList = new ArrayList<>();
        Shopper shopper1, shopper2;
        shopper1=new Shopper();
        shopper2=new Shopper();

        shopper1.setShopperID(UUID.randomUUID());
        shopper1.setName("Peter");
        shopper1.setSurname("Parker");
        shopper1.setEmail("PeterParker2021!");
        shopper1.setPassword("DontTellMaryJane2021!");
        shopper1.setOrdersCompleted(5);
        shopper1.setAccountType(UserType.SHOPPER);
        shopper1.setStoreID(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0"));

        shopper2.setShopperID(UUID.randomUUID());
        shopper2.setName("Mary");
        shopper2.setSurname("Jane");
        shopper2.setEmail("MaryJane2021!");
        shopper2.setPassword("IKnowWhoPeterIs2021!");
        shopper2.setOrdersCompleted(4);
        shopper2.setAccountType(UserType.SHOPPER);
        shopper2.setStoreID(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0"));

        shopperRepo.save(shopper1); shopperRepo.save(shopper2);
        mockShopperList.add(shopper1); mockShopperList.add(shopper2);

        Store store1 = new Store();
        store1.setShoppers(mockShopperList);
        store1.setStoreID(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0"));
        storeRepo.save(store1);

        //creating response object and default return status:
        ShoppingGetShoppersResponse response = new ShoppingGetShoppersResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        if (mockMode){
            List<ShopperObject> mockShoppers = new ArrayList<>();
            ShopperObject a = new ShopperObject();
            a.setName("mockA");
            ShopperObject b = new ShopperObject();
            b.setName("mockB");
            mockShoppers.add(a);
            mockShoppers.add(b);

            response.setShoppers(mockShoppers);
        } else {

            try {
                GetShoppersResponse getShoppersResponse = ServiceSelector.getShoppingService().getShoppers(new GetShoppersRequest(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0")));
                try {
                    response.setShoppers(populateShoppers(getShoppersResponse.getListOfShoppers()));

                } catch (Exception e){
                    e.printStackTrace();
                }

            } catch (StoreDoesNotExistException e) {

            } catch (InvalidRequestException e) {

            }

        }

        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();
        shopperRepo.deleteAll();

        return new ResponseEntity<>(response, httpStatus);
    }


    //////////////////////
    // helper functions //
    //////////////////////

    //Populate ItemObject list from items returned by use case
    private List<ItemObject> populateItems(List<Item> responseItems) throws NullPointerException{

        List<ItemObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseItems.size(); i++){

            ItemObject currentItem = new ItemObject();

            currentItem.setName(responseItems.get(i).getName());
            currentItem.setDescription(responseItems.get(i).getDescription());
            currentItem.setBarcode(responseItems.get(i).getBarcode());
            currentItem.setProductId(responseItems.get(i).getProductID());
            currentItem.setStoreId(responseItems.get(i).getStoreID().toString());
            currentItem.setPrice(BigDecimal.valueOf(responseItems.get(i).getPrice()));
            currentItem.setQuantity(responseItems.get(i).getQuantity());
            currentItem.setImageUrl(responseItems.get(i).getImageUrl());

            responseBody.add(currentItem);

        }

        return responseBody;
    }

    //Populate ItemObject list from items returned by use case
    private List<ShopperObject> populateShoppers(List<Shopper> responseShoppers) throws NullPointerException{

        List<ShopperObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseShoppers.size(); i++){

            ShopperObject currentShopper = new ShopperObject();

            currentShopper.setName(responseShoppers.get(i).getName());
            currentShopper.setId(responseShoppers.get(i).getShopperID().toString());
            currentShopper.setSurname(responseShoppers.get(i).getSurname());
            currentShopper.setUsername(responseShoppers.get(i).getEmail());
            currentShopper.setPassword(responseShoppers.get(i).getPassword());
            currentShopper.setOrdersCompleted(responseShoppers.get(i).getOrdersCompleted());
            currentShopper.setStoreID(responseShoppers.get(i).getStoreID().toString());

            responseBody.add(currentShopper);

        }

        return responseBody;
    }

}
