package cs.superleague.shopping.controller;

import cs.superleague.api.ShoppingApi;
import cs.superleague.models.*;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderItems;
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
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.shopping.requests.GetShoppersRequest;
import cs.superleague.shopping.responses.GetItemsResponse;
import cs.superleague.shopping.responses.RemoveQueuedOrderResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserException;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin
@RestController
public class ShoppingController implements ShoppingApi{

    ShoppingServiceImpl shoppingService;
    StoreRepo storeRepo;
    CatalogueRepo catalogueRepo;
    ItemRepo itemRepo;

    private RabbitTemplate rabbit;
    private RestTemplate restTemplate;
    private HttpServletRequest httpServletRequest;

    @Autowired
    public ShoppingController(ShoppingServiceImpl shoppingService, StoreRepo storeRepo,
                              CatalogueRepo catalogueRepo, ItemRepo itemRepo,
                              RabbitTemplate rabbit, RestTemplate restTemplate,
                              HttpServletRequest httpServletRequest){
        this.shoppingService = shoppingService;
        this.storeRepo = storeRepo;
        this.catalogueRepo = catalogueRepo;
        this.itemRepo = itemRepo;
        this.rabbit = rabbit;
        this.restTemplate = restTemplate;
        this.httpServletRequest = httpServletRequest;
    }


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

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            AddShopperRequest req = new AddShopperRequest(UUID.fromString(body.getShopperID()), UUID.fromString(body.getStoreID()));
            AddShopperResponse addShopperResponse = shoppingService.addShopper(req);

            try {
                response.setTimestamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(addShopperResponse.getTimestamp()));
                response.setMessage(addShopperResponse.getMessage());
                response.setSuccess(addShopperResponse.isSuccess());
            } catch (Exception e) {
                e.printStackTrace();
            }

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

//        Item item=new Item("Jelly Tots","p924585674","6001068453408", UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f5627f"),7.99,1,"Delicious Sweet.","item/jellyTots.png", "Beacon", "55g", "Sweets");
//        List<Item> itemList = new ArrayList<>();
//        itemList.add(item);
//
//        Order order = new Order();
//        order.setOrderID(UUID.randomUUID());
//        order.setStoreID(UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f5627f"));
//        order.setUserID(UUID.fromString("0163d933-561f-4253-9ea9-174c15a7fe99"));
//        order.setTotalCost(10.99);
//        order.setItems(itemList);
//
//        AddToQueueRequest addToQueueRequest = new AddToQueueRequest(order);
//
//        rabbit.convertAndSend("ShoppingEXCHANGE", "RK_AddToQueue", addToQueueRequest);


        //creating response object and default return status:
        ShoppingGetItemsResponse response = new ShoppingGetItemsResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            GetItemsResponse getItemsResponse = shoppingService.getItems(new GetItemsRequest(UUID.fromString(body.getStoreID())));
            try {

                response.setItems(populateItems(getItemsResponse.getItems()));

            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (StoreDoesNotExistException e) {

        } catch (InvalidRequestException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingRemoveQueuedOrderResponse> removeQueuedOrder(ShoppingRemoveQueuedOrderRequest body) {

        //creating response object and default return status:
        ShoppingRemoveQueuedOrderResponse response = new ShoppingRemoveQueuedOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            RemoveQueuedOrderRequest req = new RemoveQueuedOrderRequest(UUID.fromString(body.getOrderID()),UUID.fromString(body.getStoreID()));
            RemoveQueuedOrderResponse removeQueuedOrderResponse = shoppingService.removeQueuedOrder(req);

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


        try {
            GetShoppersRequest req = new GetShoppersRequest(UUID.fromString(body.getStoreID()));
            GetShoppersResponse getShoppersResponse = shoppingService.getShoppers(req);

            try {
                response.setListOfShoppers(populateShoppers(getShoppersResponse.getListOfShoppers()));
                response.setMessage(getShoppersResponse.getMessage());
                response.setSuccess(getShoppersResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(getShoppersResponse.getTimeStamp()));

            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (StoreDoesNotExistException e) {

        } catch (InvalidRequestException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }


    public ResponseEntity<ShoppingGetStoresResponse> getStores(ShoppingGetStoresRequest body) {

        //creating response object and default return status:
        ShoppingGetStoresResponse response = new ShoppingGetStoresResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            GetStoresResponse getStoresResponse = shoppingService.getStores(new GetStoresRequest());
            try {
                response.setStores(populateStores(getStoresResponse.getStores()));
                response.setResponse(true);
                response.setMessage(getStoresResponse.getMessage());

            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    public ResponseEntity<ShoppingGetNextQueuedResponse> getNextQueued(ShoppingGetNextQueuedRequest body) {

        ShoppingGetNextQueuedResponse response = new ShoppingGetNextQueuedResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            System.out.println("getNextqueued controller store id: "+ body.getStoreID());
            GetNextQueuedRequest getNextQueuedRequest = new GetNextQueuedRequest(UUID.fromString(body.getStoreID()));
            GetNextQueuedResponse getNextQueuedResponse = shoppingService.getNextQueued(getNextQueuedRequest);
            try {
                response.setTimeStamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(getNextQueuedResponse.getTimeStamp()));
                response.setMessage(getNextQueuedResponse.getMessage());
                response.setResponse(getNextQueuedResponse.isResponse());
                OrderObject orderObject = new OrderObject();
                Order order = getNextQueuedResponse.getNewCurrentOrder();

                orderObject.setStoreAddress(populateGeoPointObject(order.getStoreAddress()));
                orderObject.setOrderID(order.getOrderID().toString());
                orderObject.setCartItems(populateCartItems(order.getCartItems()));
                //orderObject.setOrderItems(populateOrderItems(order.getOrderItems()));
                orderObject.setCreateDate(order.getCreateDate().toString());
                orderObject.setStatus(order.getStatus().toString());
                orderObject.setDeliveryAddress(populateGeoPointObject(order.getDeliveryAddress()));
                orderObject.setDiscount(BigDecimal.valueOf(order.getDiscount()));
                orderObject.setProcessDate(order.getProcessDate().toString());
                orderObject.setRequiresPharmacy(order.isRequiresPharmacy());
                orderObject.setUserID(order.getUserID().toString());
                orderObject.setStoreID(order.getStoreID().toString());

                if(order.getShopperID() != null)
                orderObject.setShopperID(order.getShopperID().toString());
                orderObject.setTotalCost(BigDecimal.valueOf(order.getTotalCost()));

                response.setNewCurrentOrder(orderObject);
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
    public ResponseEntity<ShoppingGetProductByBarcodeResponse> getProductByBarcode(ShoppingGetProductByBarcodeRequest body) {

        ShoppingGetProductByBarcodeResponse response = new ShoppingGetProductByBarcodeResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetProductByBarcodeRequest request = new GetProductByBarcodeRequest(body.getProductBarcode(), UUID.fromString(body.getStoreID()));
            GetProductByBarcodeResponse getProductByBarcodeResponse = shoppingService.getProductByBarcode(request);
            try {
                response.setMessage(getProductByBarcodeResponse.getMessage());
                response.setSuccess(getProductByBarcodeResponse.isSuccess());
                ItemObject currentItem = new ItemObject();

                currentItem.setName(getProductByBarcodeResponse.getProduct().getName());
                currentItem.setDescription(getProductByBarcodeResponse.getProduct().getDescription());
                currentItem.setBarcode(getProductByBarcodeResponse.getProduct().getBarcode());
                currentItem.setProductID(getProductByBarcodeResponse.getProduct().getProductID());
                currentItem.setStoreID(getProductByBarcodeResponse.getProduct().getStoreID().toString());
                currentItem.setPrice(BigDecimal.valueOf(getProductByBarcodeResponse.getProduct().getPrice()));
                currentItem.setQuantity(getProductByBarcodeResponse.getProduct().getQuantity());
                currentItem.setImageUrl(getProductByBarcodeResponse.getProduct().getImageUrl());
                currentItem.setBrand(getProductByBarcodeResponse.getProduct().getBrand());
                currentItem.setItemType(getProductByBarcodeResponse.getProduct().getItemType());
                currentItem.setSize(getProductByBarcodeResponse.getProduct().getSize());
                response.setProduct(currentItem);
            }catch (Exception e){
                e.printStackTrace();
                response.setSuccess(false);
                response.setMessage(e.getMessage());
                response.setProduct(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setProduct(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingGetQueueResponse> getQueue(ShoppingGetQueueRequest body) {
        GetQueueRequest getQueueRequest;

        ShoppingGetQueueResponse response = new ShoppingGetQueueResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        System.out.println("getQueue body getStore ID: "+ body.getStoreID());
        try{
            GetQueueRequest request = new GetQueueRequest(UUID.fromString(body.getStoreID()));
            GetQueueResponse getQueueResponse = shoppingService.getQueue(request);
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
        UUID storeUUID3 = UUID.fromString("f29c3a2b-0f5e-45ef-b06b-7e564e70a7af");
        UUID storeUUID4 = UUID.fromString("ac0d5977-dca2-43b8-b5c7-d098cae44b1d");
        UUID storeUUID5 = UUID.fromString("701be3e1-c23c-4409-a851-9cae63861881");
        UUID storeUUID6 = UUID.fromString("5eb21285-f45c-43d9-b32d-8b979263d49d");
        UUID storeUUID7 = UUID.fromString("438a347b-e863-420a-b86f-69db6ab44ad5");

        Item item1, item2,item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13, item14, item15, item16, item17, item18, item19, item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30;
        item1=new Item("Tomato Sauce","p234058925","60019578",storeUUID1,31.99,1,"South Africa's firm favourite! It has a thick, smooth texture that can easily be poured and enjoyed on a variety of dishes.","item/tomatoSauce.png", "All Gold", "700ml", "Sauce");
        item2=new Item("Bar one","p123984123","6001068595808", storeUUID1,10.99,1,"Thick milk chocolate with nougat and caramel centre.","item/barOne.png", "Nestle", "55g", "Chocolate");
        item3=new Item("Milk","p423523144","6001007162474",storeUUID1,27.99,1,"Pasteurised, homogenised Full cream fresh milk","item/pnpMilk.png", "Pick n Pay", "2l", "Dairy");
        item4=new Item("Baked Beans","p623235254","6009522300586", storeUUID1,14.99,1,"Our iconic KOO Baked Beans in a rich tomato sauce, is extremely versatile and convenient.","item/kooBeans.png", "Koo", "410g", "Canned");
        item5=new Item("Bread", "p903932918", "6001205733520", storeUUID1, 15.49, 1, "Blue Ribbon has four delicious ranges of quality bread: Standard, Classic, Toaster and Lifestyle.", "item/blueRibbon.png", "Blue Ribbon", "700g", "Bakery" );
        item6=new Item("Jolly Jammers", "p930458594", "6001056412360", storeUUID1, 28.99, 1, "Delicious buttery biscuits with choc cream sandwiched between, in a delightful smiley face to bring joy while you have your sweet treat.", "item/jammers.png", "Bakers", "200g", "Biscuit");
        item7=new Item("Ricoffy In Tin", "p434565787", "6001068323500", storeUUID1, 84.99, 1, "Ricoffy is South Africa's No. 1 coffee brand. Sharing memorable moments over a mug of hot coffee has been the tradition of many South African families.", "item/ricoffy.png", "Nestle", "750g", "Beverages");
        item8=new Item("Creamy Tomato Soup", "p4352546788", "6001007286309", storeUUID1, 39.99, 1, "Delicious creamy tomato soup prepared by the chefs at Pick n Pay.", "item/creamyTomato.png", "Pick n Pay", "600g", "Soup");
        item9=new Item("Creamy Butternut Soup", "p543523442", "6001007286323", storeUUID1, 29.99, 1, "Delicious butternut soup prepared by the chefs at Pick n Pay.", "item/creamyButternut.png", "Pick n Pay", "600g", "Soup");
        item10=new Item("Oros Original Orange Squash", "p6980402934", "6001324011172", storeUUID1, 41.99, 1, "Mix this 2L tartrazine-free concentrate with cold water for a quick drink, the party table or your kids' school bags.", "item/originalOros.png", "Brooks", "2l", "Beverages");
        item11=new Item("Noodle's beef", "p3872948374", "6001306002457", storeUUID1, 28.99, 1, "A convenient and easy meal to prepare, it can be used as a main meal ingredient or simply be enjoyed on its' own.", "item/kellogNoodles.png", "Kelloggs", "70g", "Pantry");
        item12=new Item("Ultra Mel Vanilla Flavoured Custard", "p493082605986", "6009708460257", storeUUID1, 20.99, 1, "Our creamy, thick and ultra-smooth blend of the finest vanilla makes it the perfect 100% treat to serve at every occasion.", "item/custard.png", "Danone", "1l", "Dairy");
        item13=new Item("Weet-Bix", "p473203872049", "6001052001018", storeUUID1, 28.99, 1, "Wholegrain wheat biscuits.", "item/weetBix.png", "Bokomo", "450g", "Cereal");
        item14=new Item("Nescafe Classic Coffee", "p048375923", "7613033677724", storeUUID1, 85.99, 1, "Only the best coffee beans are selected. Blended. Roasted to perfection. So, dare to break the seal.", "item/nescaffeCoffee.png", "Nescafe", "500g", "Beverages");
        item15=new Item("Oreo original", "p392874287", "7622201779030", storeUUID1, 15.99, 1, "Oreo biscuits with vanilla flavoured filling, coated with milk chocolate.", "item/oreo.png", "OREO", "133g", "Biscuit");
        item16=new Item("Nescafe Classic Coffee", "w435435345", "7613033677724", storeUUID2, 85.99, 1, "Only the best coffee beans are selected. Blended. Roasted to perfection. So, dare to break the seal.", "item/nescaffeCoffee.png", "Nescafe", "500g", "Beverages");
        item17=new Item("Jolly Jammers", "w423546657", "6001056412360", storeUUID2, 28.99, 1, "Delicious buttery biscuits with choc cream sandiwched between, in a delightful smiley face to bring joy while you have your sweet treat.", "item/jammers.png", "Bakers", "200g", "Biscuit");
        item18=new Item("Ultra Mel Vanilla Flavoured Custard", "w786453324", "6009708460257", storeUUID2, 20.99, 1, "Our creamy, thick and ultra-smooth blend of the finest vanilla makes it the perfect 100% treat to serve at every occasion.", "item/custard.png", "Danone", "1l", "Dairy");
        item19=new Item("Weet-Bix", "w5438768098", "6001052001018", storeUUID2, 28.99, 1, "Wholegrain wheat biscuits.", "item/weetBix.png", "Bokomo", "450g", "Cereal");
        item20=new Item("Oreo original", "w546789058", "7622201779030", storeUUID2, 15.99, 1, "Oreo biscuits with vanilla flavoured filling, coated with milk chocolate.", "item/oreo.png", "OREO", "133g", "Biscuit");
        item21=new Item("Strawberry yoghurt", "w213564876", "20048976", storeUUID2, 28.99, 1, "While no preservatives have been added, we have packed our delicious low fat strawberry yoghurt with an extra 16% fruit and added millions of live Bifidobacterium cultures.", "item/strawberryYoghurt.png", "Woolworths", "1kg", "Dairy");
        item22=new Item("White Thick Slice Bread", "w342678678", "20018702", storeUUID2, 16.69, 1, "A soft and fine textured white thick sliced bread.", "item/thickSlice.png", "Woolworths", "700g", "Bakery");
        item23=new Item("Fresh Full Cream Ayrshire Milk", "w657577567", "20026875", storeUUID2, 39.99, 1, "Our full cream Ayrshire milk promises a fresh, delicious taste. It may look like a glass of ordinary milk, but it isn't. It's our Ayrshire milk.", "item/woolworthsMilk.png", "Woolworths", "2l", "Dairy");
        item24=new Item("Bulk Cheddar Cheese", "w3247657765", "6009182131636", storeUUID2, 134.99, 1, "Enjoy our mild and slightly savoury cheddar cheese. Matured for a minimum of 2 months.", "item/woolworthsCheese.png", "Woolworths", "900g", "Dairy");
        item25=new Item("100% Apple Juice", "w097567878", "6009173967152", storeUUID2, 48.99, 1, "Our 100% Apple Juice contains no added preservatives and is a source of vitamin C.", "item/woolworthsAppleJuice.png", "Woolworths", "1.5l", "Beverages");
        item26=new Item("Ricoffy In Tin", "w97546564", "6001068323500", storeUUID2, 84.99, 1, "Ricoffy is South Africa's No. 1 coffee brand. Sharing memorable moments over a mug of hot coffee has been the tradition of many South African families.", "item/ricoffy.png", "Nestle", "750g", "Beverages");
        item27=new Item("Tomato Sauce","w786456345","60019578",storeUUID2,31.99,1,"South Africa's firm favourite! It has a thick, smooth texture that can easily be poured and enjoyed on a variety of dishes.","item/sauce.png", "All Gold", "700ml", "Sauce");
        item28=new Item("Bar one","w3456757896","6001068595808", storeUUID2,10.99,1,"Thick milk chocolate with nougat and caramel centre.","item/barOne.png", "Nestle", "55g", "Chocolate");
        item29=new Item("Baked Beans","w897321657","6009522300586", storeUUID2,14.99,1,"Our iconic KOO Baked Beans in a rich tomato sauce, is extremely versatile and convenient.","item/kooBeans.png", "Koo", "410g", "Canned");
        item30=new Item("Noodle's beef", "w45387609564", "6001306002457", storeUUID2, 28.99, 1, "A convenient and easy meal to prepare, it can be used as a main meal ingredient or simply be enjoyed on its' own.", "item/kellogNoodles.png", "Kelloggs", "70g", "Pantry");


        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        itemRepo.save(item4);
        itemRepo.save(item5);
        itemRepo.save(item6);
        itemRepo.save(item7);
        itemRepo.save(item8);
        itemRepo.save(item9);
        itemRepo.save(item10);
        itemRepo.save(item11);
        itemRepo.save(item12);
        itemRepo.save(item13);
        itemRepo.save(item14);
        itemRepo.save(item15);
        itemRepo.save(item16);
        itemRepo.save(item17);
        itemRepo.save(item18);
        itemRepo.save(item19);
        itemRepo.save(item20);
        itemRepo.save(item21);
        itemRepo.save(item22);
        itemRepo.save(item23);
        itemRepo.save(item24);
        itemRepo.save(item25);
        itemRepo.save(item26);
        itemRepo.save(item27);
        itemRepo.save(item28);
        itemRepo.save(item29);
        itemRepo.save(item30);


        //Catalogues
        List<Item> store1Cat = new ArrayList<>();
        store1Cat.add(item1);
        store1Cat.add(item2);
        store1Cat.add(item3);
        store1Cat.add(item4);
        store1Cat.add(item5);
        store1Cat.add(item6);
        store1Cat.add(item7);
        store1Cat.add(item8);
        store1Cat.add(item9);
        store1Cat.add(item10);
        store1Cat.add(item11);
        store1Cat.add(item12);
        store1Cat.add(item13);
        store1Cat.add(item14);
        store1Cat.add(item15);

        List<Item> store2Cat = new ArrayList<>();
        store2Cat.add(item16);
        store2Cat.add(item17);
        store2Cat.add(item18);
        store2Cat.add(item19);
        store2Cat.add(item20);
        store2Cat.add(item21);
        store2Cat.add(item22);
        store2Cat.add(item23);
        store2Cat.add(item24);
        store2Cat.add(item25);
        store2Cat.add(item26);
        store2Cat.add(item27);
        store2Cat.add(item28);
        store2Cat.add(item29);
        store2Cat.add(item30);

        Catalogue c1 = new Catalogue(storeUUID1, store1Cat);
        catalogueRepo.save(c1);

        Catalogue c2 = new Catalogue(storeUUID2, store2Cat);
        catalogueRepo.save(c2);

        Store store1=new Store(storeUUID1, -1, 24, "Pick n Pay", 2, 5, true, "shop/pnp.png");
        Store store2=new Store(storeUUID2, -1, 24, "Woolworths", 2, 7, true, "shop/woolworths.png");
        Store store3=new Store(storeUUID3, 8, 21, "Checkers Hyper", 2, 7, true, "shop/checkers.png");
        Store store4=new Store(storeUUID4, 8, 22, "SuperSpar", 2, 7, true, "shop/superSpar.png");
        Store store5=new Store(storeUUID5, 8, 20, "Game", 2, 7, true, "shop/game.png");
        Store store6=new Store(storeUUID6, 7, 18, "Food Lover's Market", 2, 7, true, "shop/foodLoversMarket.png");
        Store store7=new Store(storeUUID7, 8, 20, "Pep", 2, 7, true, "shop/pep.png");
        GeoPoint store1Location = new GeoPoint(-25.770344, 28.234631, "Pick n Pay Brooklyn");
        GeoPoint store2Location = new GeoPoint(-25.7588746 , 28.2429369, "Hillcrest Boulevard");
        GeoPoint store3Location = new GeoPoint(-25.7825411, 28.26145295, "Grape street");
        GeoPoint store4Location = new GeoPoint(-25.7051548, 28.29665621, "Avo Street");
        GeoPoint store5Location = new GeoPoint(-25.72515168, 28.2625161, "Strawberry street");
        GeoPoint store6Location = new GeoPoint(-25.73528286, 28.225254252452454, "Blueberry Street");
        GeoPoint store7Location = new GeoPoint(-25.715615161561516, 28.265748988985454, "Raspberry street");

        store1.setStoreLocation(store1Location);
        store1.setStock(c1);

        store2.setStoreLocation(store2Location);
        store2.setStock(c2);

        store3.setStoreLocation(store3Location);
        store4.setStoreLocation(store4Location);
        store5.setStoreLocation(store5Location);
        store6.setStoreLocation(store6Location);
        store7.setStoreLocation(store7Location);

        storeRepo.save(store1);
        storeRepo.save(store2);
        storeRepo.save(store3);
        storeRepo.save(store4);
        storeRepo.save(store5);
        storeRepo.save(store6);
        storeRepo.save(store7);

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
            currentItem.setProductID(responseItems.get(i).getProductID());
            currentItem.setStoreID(responseItems.get(i).getStoreID().toString());
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
            if(responseShoppers.get(i).getShopperID()!=null)
            {
                currentShopper.setShopperID(responseShoppers.get(i).getShopperID().toString());
            }
            if(responseShoppers.get(i).getStoreID()!=null)
            {
                currentShopper.setStoreID(responseShoppers.get(i).getStoreID().toString());
            }

            currentShopper.setOrdersCompleted(responseShoppers.get(i).getOrdersCompleted());
            currentShopper.setOnShift(responseShoppers.get(i).getOnShift());
            currentShopper.setIsActive(responseShoppers.get(i).isActive());
            responseBody.add(currentShopper);

        }

        return responseBody;
    }

    private List<StoreObject> populateStores(List<Store> responseStores) throws NullPointerException{

        List<StoreObject> responseBody = new ArrayList<>();

        if(responseStores != null)
        for(int i = 0; i < responseStores.size(); i++){

            StoreObject currentStore = new StoreObject();

            if(responseStores.get(i).getStoreID()!=null)
            {
                currentStore.setStoreID(responseStores.get(i).getStoreID().toString());
            }

            currentStore.setStoreBrand(responseStores.get(i).getStoreBrand());
            currentStore.setOpeningTime(responseStores.get(i).getOpeningTime());
            currentStore.setClosingTime(responseStores.get(i).getClosingTime());
            currentStore.setMaxOrders(responseStores.get(i).getMaxOrders());
            currentStore.setMaxShoppers(responseStores.get(i).getMaxShoppers());
            currentStore.setIsOpen(responseStores.get(i).getOpen());
            currentStore.setImgUrl(responseStores.get(i).getImgUrl());

            if(responseStores.get(i).getStoreLocation()!=null)
            {
                currentStore.setStoreLocation(populateGeoPointObject(responseStores.get(i).getStoreLocation()));
            }

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
                currentOrder.setOrderID(responseOrders.get(i).getOrderID().toString());
                currentOrder.setUserID(responseOrders.get(i).getUserID().toString());
                currentOrder.setStoreID(responseOrders.get(i).getStoreID().toString());

                if(responseOrders.get(i).getShopperID()!=null)
                {
                    currentOrder.setShopperID(responseOrders.get(i).getShopperID().toString());
                }

                currentOrder.setCreateDate(responseOrders.get(i).getCreateDate().toString());
                currentOrder.setProcessDate(responseOrders.get(i).getProcessDate().toString());
                currentOrder.setTotalCost(new BigDecimal(responseOrders.get(i).getTotalCost()));
                currentOrder.setStatus(responseOrders.get(i).getStatus().name());
                currentOrder.setCartItems(populateCartItems(responseOrders.get(i).getCartItems()));
                currentOrder.setDiscount(new BigDecimal(responseOrders.get(i).getDiscount()));
                currentOrder.setDeliveryAddress(populateGeoPointObject(responseOrders.get(i).getDeliveryAddress()));
                currentOrder.setStoreAddress(populateGeoPointObject(responseOrders.get(i).getStoreAddress()));
                currentOrder.setRequiresPharmacy(responseOrders.get(i).isRequiresPharmacy());


            }catch(Exception e)
            {
                e.printStackTrace();
            }


            responseBody.add(currentOrder);
        }

        return responseBody;
    }

    private List<OrderItemsObject> populateOrderItems(List<OrderItems> responseItems) throws NullPointerException{

        List<OrderItemsObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseItems.size(); i++){

            OrderItemsObject currentItem = new OrderItemsObject();

            currentItem.setName(responseItems.get(i).getName());
            currentItem.setDescription(responseItems.get(i).getDescription());
            currentItem.setBarcode(responseItems.get(i).getBarcode());
            currentItem.setProductId(responseItems.get(i).getProductID());
            currentItem.setOrderId(responseItems.get(i).getOrderID().toString());
            currentItem.setPrice(BigDecimal.valueOf(responseItems.get(i).getPrice()));
            currentItem.setQuantity(responseItems.get(i).getQuantity());
            currentItem.setImageUrl(responseItems.get(i).getImageUrl());
            currentItem.setBrand(responseItems.get(i).getBrand());
            currentItem.setItemType(responseItems.get(i).getItemType());
            currentItem.setSize(responseItems.get(i).getSize());
            currentItem.setTotalCost(BigDecimal.valueOf(responseItems.get(i).getTotalCost()));

            responseBody.add(currentItem);

        }

        return responseBody;
    }

    public GeoPointObject populateGeoPointObject(GeoPoint location){
        GeoPointObject locationObject = new GeoPointObject();
        locationObject.setAddress(location.getAddress());
        locationObject.setLongitude(BigDecimal.valueOf(location.getLongitude()));
        locationObject.setLatitude(BigDecimal.valueOf(location.getLatitude()));
        return locationObject;
    }

    @Override
    public ResponseEntity<ShoppingGetAllItemsResponse> getAllItems(ShoppingGetAllItemsRequest body) {

        //creating response object and default return status:
        ShoppingGetAllItemsResponse response = new ShoppingGetAllItemsResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            GetAllItemsResponse getAllItemsResponse = shoppingService.getAllItems(new GetAllItemsRequest());
            try {

                response.setItems(populateItems(getAllItemsResponse.getItems()));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingGetItemsByIDResponse> getItemsByID(ShoppingGetItemsByIDRequest body) {

        //creating response object and default return status:
        ShoppingGetItemsByIDResponse response = new ShoppingGetItemsByIDResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            GetItemsByIDResponse getItemsByIDResponse = shoppingService.getItemsByID(new GetItemsByIDRequest(body.getItemIDs()));
            try {

                response.setItems(populateItems(getItemsByIDResponse.getItems()));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ShoppingGetStoreByUUIDResponse> getStoreByUUID(ShoppingGetStoreByUUIDRequest body) {

        //creating response object and default return status:
        ShoppingGetStoreByUUIDResponse response = new ShoppingGetStoreByUUIDResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            System.out.println("controller getStoreyByUUID : "+ body.getStoreID());
            GetStoreByUUIDResponse getStoreByUUIDResponse = shoppingService.getStoreByUUID(new GetStoreByUUIDRequest(UUID.fromString(body.getStoreID())));
            try {

                response.setStore(populateStore(getStoreByUUIDResponse.getStore()));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException | StoreDoesNotExistException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    private StoreObject populateStore(Store responseStore) throws NullPointerException{

        StoreObject responseBody = new StoreObject();

        if(responseStore.getStoreID()!=null)
        {
            responseBody.setStoreID(responseStore.getStoreID().toString());
        }

        responseBody.setStoreBrand(responseStore.getStoreBrand());
        responseBody.setOpeningTime(responseStore.getOpeningTime());
        responseBody.setClosingTime(responseStore.getClosingTime());
        responseBody.setMaxOrders(responseStore.getMaxOrders());
        responseBody.setMaxShoppers(responseStore.getMaxShoppers());
        responseBody.setIsOpen(responseStore.getOpen());
        responseBody.setImgUrl(responseStore.getImgUrl());

        if(responseStore.getStoreLocation()!=null)
        {
            responseBody.setStoreLocation(populateGeoPointObject(responseStore.getStoreLocation()));
        }

        return responseBody;
    }

    private List<CartItemObject> populateCartItems(List<CartItem> responseItems) throws NullPointerException{

        List<CartItemObject> responseBody = new ArrayList<>();

        for (CartItem i: responseItems){

            CartItemObject item = new CartItemObject();
            if(i.getCartItemNo()!=null)
            {
                item.setCartItemNo(i.getCartItemNo().toString());
            }
            item.setProductId(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(i.getStoreID().toString());
            item.setPrice(BigDecimal.valueOf(i.getPrice()));
            item.setImageUrl(i.getImageUrl());
            item.setBrand(i.getBrand());
            item.setSize(i.getSize());
            item.setItemType(i.getItemType());
            item.setDescription(i.getDescription());

            responseBody.add(item);

        }

        return responseBody;
    }
}
