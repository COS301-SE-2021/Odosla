package cs.superleague.shopping.controller;

import cs.superleague.api.ShoppingApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
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
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.shopping.requests.GetShoppersRequest;
import cs.superleague.shopping.responses.GetItemsResponse;
import cs.superleague.shopping.responses.RemoveQueuedOrderResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.repos.ShopperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@CrossOrigin
@RestController
public class ShoppingController implements ShoppingApi{

    @Autowired
    ShoppingServiceImpl shoppingService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    CustomerRepo customerRepo;


    UUID storeID = UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0");


    UUID userID = UUID.fromString("55534567-9CBC-FEF0-1254-56789ABCDEF0");


    UUID orderID = UUID.fromString("99134567-9CBC-FEF0-1254-56789ABCDEF0");


    private boolean mockMode = false;


    @Override
    public ResponseEntity<ShoppingAddShopperResponse> addShopper(ShoppingAddShopperRequest body) {

        //creating response object  and default return status
        ShoppingAddShopperResponse response = new ShoppingAddShopperResponse();
        HttpStatus status = HttpStatus.OK;

        try{

            AddShopperRequest req = new AddShopperRequest(UUID.fromString(body.getShopperID()), UUID.fromString(body.getStoreID()));
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
        }catch(UserException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, status);

    }

    //getItems endpoint
    @Override
    public ResponseEntity<ShoppingGetItemsResponse> getItems(ShoppingGetItemsRequest body) {

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
                GetItemsResponse getItemsResponse = ServiceSelector.getShoppingService().getItems(new GetItemsRequest(UUID.fromString(body.getStoreID())));
                try {

                    response.setItems(populateItems(getItemsResponse.getItems()));

                } catch (Exception e){
                    e.printStackTrace();
                }

            } catch (StoreDoesNotExistException e) {

            } catch (InvalidRequestException e) {

            }

        }

//        storeRepo.deleteAll();
//        catalogueRepo.deleteAll();
//        itemRepo.deleteAll();

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingRemoveQueuedOrderResponse> removeQueuedOrder(ShoppingRemoveQueuedOrderRequest body) {

        //creating response object and default return status:
        ShoppingRemoveQueuedOrderResponse response = new ShoppingRemoveQueuedOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            RemoveQueuedOrderRequest req = new RemoveQueuedOrderRequest(UUID.fromString(body.getOrderID()),UUID.fromString(body.getStoreID()));
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
                GetShoppersRequest req = new GetShoppersRequest(UUID.fromString(body.getStoreID()));
                GetShoppersResponse getShoppersResponse = ServiceSelector.getShoppingService().getShoppers(req);

                try {
                    response.setShoppers(populateShoppers(getShoppersResponse.getListOfShoppers()));

                } catch (Exception e){
                    e.printStackTrace();
                }

            } catch (StoreDoesNotExistException e) {

            } catch (InvalidRequestException e) {

            }

        }

        return new ResponseEntity<>(response, httpStatus);
    }


    public ResponseEntity<ShoppingGetStoresResponse> getStores(ShoppingGetStoresRequest body) {

        //creating response object and default return status:
        ShoppingGetStoresResponse response = new ShoppingGetStoresResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        if (mockMode){
            List<StoreObject> mockStores = new ArrayList<>();
            StoreObject a = new StoreObject();
            a.setStoreBrand("mockA");
            StoreObject b = new StoreObject();
            b.setStoreBrand("mockB");
            mockStores.add(a);
            mockStores.add(b);

            response.setStores(mockStores);
        } else {

            try {
                GetStoresResponse getStoresResponse = ServiceSelector.getShoppingService().getStores(new GetStoresRequest());
                try {
                    response.setStores(populateStores(getStoresResponse.getStores()));
                    response.setResponse(true);
                    response.setMessage(getStoresResponse.getMessage());

                } catch (Exception e){
                    e.printStackTrace();
                }

            } catch (InvalidRequestException e) {

            }

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    public ResponseEntity<ShoppingGetNextQueuedResponse> getNextQueued(ShoppingGetNextQueuedRequest body) {

        ShoppingGetNextQueuedResponse response = new ShoppingGetNextQueuedResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            GetNextQueuedRequest getNextQueuedRequest = new GetNextQueuedRequest(UUID.fromString(body.getStoreID()), body.getJwtToken());
            GetNextQueuedResponse getNextQueuedResponse = ServiceSelector.getShoppingService().getNextQueued(getNextQueuedRequest);
            try {

                response.setDate(getNextQueuedResponse.getTimeStamp().toString());
                response.setMessage(getNextQueuedResponse.getMessage());
                response.setSuccess(getNextQueuedResponse.isResponse());
                response.setNewCurrentOrder(getNextQueuedResponse.getNewCurrentOrder());
                response.setQueueOfOrders(populateOrders(getNextQueuedResponse.getQueueOfOrders()));

            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingGetQueueResponse> getQueue(ShoppingGetQueueRequest body) {
        GetQueueRequest getQueueRequest;

        ShoppingGetQueueResponse response = new ShoppingGetQueueResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetQueueRequest request = new GetQueueRequest(UUID.fromString(body.getStoreID()));
            GetQueueResponse getQueueResponse = ServiceSelector.getShoppingService().getQueue(request);
            try {
                response.setResponse(getQueueResponse.getResponse());
                response.setMessage(getQueueResponse.getMessage());
                response.setQueueOfOrders(populateOrders(getQueueResponse.getQueueOfOrders()));
            }catch (Exception e){
                e.printStackTrace();
                response.setResponse(false);
                response.setMessage(e.getMessage());
                response.setQueueOfOrders(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setResponse(false);
            response.setMessage(e.getMessage());
            response.setQueueOfOrders(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    public ResponseEntity<ShoppingPopulateTablesResponse> populateTables(ShoppingPopulateTablesRequest body) {
        //Items

        UUID storeUUID1 = UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f7a27f");
        UUID storeUUID2 = UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f5627f");

        Item item1, item2,item3, item4, item5;
        item1=new Item("Tomato Sauce","p234058925","91234567-9ABC-DEF0-1234-56789ABCDEFF",storeUUID1,36.99,1,"description","item/", "Heinz", "250g", "Sauce");
        item2=new Item("Bar one","p123984123","62234567-9ABC-DEF0-1234-56789ABCDEFA", storeUUID1,14.99,3,"description","item/", "Nestle", "90g", "Chocolate");
        item3=new Item("Milk","p423523144","69767699-9ABF-HJDS-1234-56789ABCDEFF",storeUUID1,36.99,1,"description","item/", "Pick n Pay", "2l", "Dairy");
        item4=new Item("Baked Beans","p623235254","65363563-9JBC-DEF0-1234-56789ABCDEFA", storeUUID2,14.99,3,"description","item/", "Koo", "410g", "Canned");
        item5=new Item("Bread", "p903932918", "6001710010253", storeUUID2, 16.99, 9, "description", "item/", "Sasko", "700g", "Bakery" );

        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        itemRepo.save(item4);
        itemRepo.save(item5);

        //Catalogues
        List<Item> store1Cat = new ArrayList<>();
        store1Cat.add(item1);
        store1Cat.add(item2);
        store1Cat.add(item3);

        List<Item> store2Cat = new ArrayList<>();
        store2Cat.add(item4);
        store2Cat.add(item5);

        Catalogue c1 = new Catalogue(storeUUID1, store1Cat);
        catalogueRepo.save(c1);

        Catalogue c2 = new Catalogue(storeUUID2, store2Cat);
        catalogueRepo.save(c2);

        Store store1=new Store(storeUUID1, 7, 4, "PnP", 2, 5, true, "shop/pnp.png");
        Store store2=new Store(storeUUID2, 8, 10, "Woolworths", 2, 7, true, "shop/woolworths.png");
        GeoPoint store1Location = new GeoPoint(-25.762862391432126, 28.261305943073157, "Apple street");
        GeoPoint store2Location = new GeoPoint(-25.760319754713873, 28.278808593750004, "Banana Street");

        store1.setStoreLocation(store1Location);
        store1.setStock(c1);

        store2.setStoreLocation(store2Location);
        store2.setStock(c2);


        storeRepo.save(store1);
        storeRepo.save(store2);

       // HttpStatus httpStatus = HttpStatus.OK;

        //Shoppers
//        Shopper shopper1, shopper2;
//        shopper1=new Shopper();
//        shopper2=new Shopper();
//
//        UUID shopper1ID= UUID.randomUUID();
//        UUID shopper2ID= UUID.randomUUID();
//
//        shopper1.setShopperID(shopper1ID);
//        shopper1.setName("Peter");
//        shopper1.setSurname("Parker");
//        shopper1.setEmail("PeterParker2021!@gmail.com");
//        shopper1.setPassword("DontTellMaryJane2021!");
//        shopper1.setOrdersCompleted(5);
//        shopper1.setAccountType(UserType.SHOPPER);
//        shopper1.setStoreID(storeUUID1);
//
//        shopper2.setShopperID(shopper2ID);
//        shopper2.setName("Mary");
//        shopper2.setSurname("Jane");
//        shopper2.setEmail("MaryJane2021!@gmail.com");
//        shopper2.setPassword("IKnowWhoPeterIs2021!");
//        shopper2.setOrdersCompleted(4);
//        shopper2.setAccountType(UserType.SHOPPER);
//        shopper2.setStoreID(storeUUID2);
//
//        shopperRepo.save(shopper1); shopperRepo.save(shopper2);
//        List<Shopper> mockShopperList = new ArrayList<>();
//        mockShopperList.add(shopper1);
//        store1.setShoppers(mockShopperList);
//        storeRepo.save(store1);
//        mockShopperList = new ArrayList<>();
//        mockShopperList.add(shopper2);
//        store2.setShoppers(mockShopperList);
//        storeRepo.save(store2);
//
//
//        //Grocery Lists
//        GroceryList groceryList = new GroceryList(UUID.randomUUID(), "Shopping List", store2Cat);
//        List<GroceryList> groceryLists = new ArrayList<>();
//        groceryLists.add(groceryList);
//        groceryListRepo.save(groceryList);
//        //Customers
//        GeoPoint customerLocation = new GeoPoint(-25.700937877819005, 28.223876953125004, "customer address");
//        Customer customer = new Customer("Dan", "Smith", "ds@smallClub.com", "0721234567", "Hello$$123",null, "activate", "reset", null, true,UserType.CUSTOMER, UUID.randomUUID(), customerLocation, groceryLists, null, null, null);
//        customerRepo.save(customer);

        return null;
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
            currentItem.setBrand(responseItems.get(i).getBrand());
            currentItem.setItemType(responseItems.get(i).getItemType());
            currentItem.setSize(responseItems.get(i).getSize());

            responseBody.add(currentItem);

        }

        return responseBody;
    }

    //Populate ShopperObject list from items returned by use case
    private List<ShopperObject> populateShoppers(List<Shopper> responseShoppers) throws NullPointerException{

        List<ShopperObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseShoppers.size(); i++){

            ShopperObject currentShopper = new ShopperObject();

            currentShopper.setName(responseShoppers.get(i).getName());
            currentShopper.setSurname(responseShoppers.get(i).getSurname());
            currentShopper.setEmail(responseShoppers.get(i).getEmail());
            currentShopper.setPhoneNumber(responseShoppers.get(i).getPhoneNumber());
            currentShopper.setPassword(responseShoppers.get(i).getPassword());
            currentShopper.setActivationDate(String.valueOf(responseShoppers.get(i).getActivationDate()));
            currentShopper.setActivationCode(responseShoppers.get(i).getActivationCode());
            currentShopper.setResetCode(responseShoppers.get(i).getResetCode());
            currentShopper.setResetExpiration(responseShoppers.get(i).getResetExpiration());
            currentShopper.setAccountType(String.valueOf(responseShoppers.get(i).getAccountType()));
            currentShopper.setShopperID(responseShoppers.get(i).getShopperID().toString());
            currentShopper.setStoreID(responseShoppers.get(i).getStoreID().toString());
            currentShopper.setOrdersCompleted(responseShoppers.get(i).getOrdersCompleted());
            currentShopper.setOnShift(responseShoppers.get(i).getOnShift());
            currentShopper.setIsActive(responseShoppers.get(i).isActive());
            responseBody.add(currentShopper);

        }

        return responseBody;
    }

    private List<StoreObject> populateStores(List<Store> responseStores) throws NullPointerException{

        List<StoreObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseStores.size(); i++){

            StoreObject currentStore = new StoreObject();

            currentStore.setStoreID(responseStores.get(i).getStoreID().toString());
            currentStore.setStoreBrand(responseStores.get(i).getStoreBrand());
            currentStore.setOpeningTime(responseStores.get(i).getOpeningTime());
            currentStore.setClosingTime(responseStores.get(i).getClosingTime());
            currentStore.setMaxOrders(responseStores.get(i).getMaxOrders());
            currentStore.setMaxShoppers(responseStores.get(i).getMaxShoppers());
            currentStore.setIsOpen(responseStores.get(i).getOpen());
            currentStore.setImageUrl(responseStores.get(i).getImgUrl());

            responseBody.add(currentStore);

        }

        return responseBody;
    }

    private List<OrderObject> populateOrders(List<Order> responseOrders) throws NullPointerException{

        List<OrderObject> responseBody = new ArrayList<>();
        if (responseOrders == null){
            return null;
        }
        for(int i = 0; i < responseOrders.size(); i++){

            OrderObject currentOrder = new OrderObject();

            try{
                currentOrder.setOrderId(responseOrders.get(i).getOrderID().toString());
                currentOrder.setUserId(responseOrders.get(i).getUserID().toString());
                currentOrder.setStoreId(responseOrders.get(i).getStoreID().toString());

                try{
                    currentOrder.setShopperId(responseOrders.get(i).getShopperID().toString());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                currentOrder.setCreateDate(responseOrders.get(i).getCreateDate().getTime().toString());
                currentOrder.setProcessDate(responseOrders.get(i).getProcessDate().getTime().toString());
                currentOrder.setTotalPrice(new BigDecimal(responseOrders.get(i).getTotalCost()));
                currentOrder.setStatus(responseOrders.get(i).getStatus().name());
                currentOrder.setItems(populateItems(responseOrders.get(i).getItems()));
                currentOrder.setDiscount(new BigDecimal(responseOrders.get(i).getDiscount()));
                currentOrder.setDeliveryAddress(responseOrders.get(i).getDeliveryAddress().getAddress());
                currentOrder.setStoreAddress(responseOrders.get(i).getStoreAddress().getAddress());
                currentOrder.setRequiresPharmacy(responseOrders.get(i).isRequiresPharmacy());


            }catch(Exception e)
            {
                e.printStackTrace();
            }


            responseBody.add(currentOrder);
        }

        return responseBody;
    }

}
