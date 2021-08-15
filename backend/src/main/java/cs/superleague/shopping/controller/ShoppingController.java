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
        UUID storeUUID3 = UUID.fromString("f29c3a2b-0f5e-45ef-b06b-7e564e70a7af");
        UUID storeUUID4 = UUID.fromString("ac0d5977-dca2-43b8-b5c7-d098cae44b1d");
        UUID storeUUID5 = UUID.fromString("701be3e1-c23c-4409-a851-9cae63861881");
        UUID storeUUID6 = UUID.fromString("5eb21285-f45c-43d9-b32d-8b979263d49d");
        UUID storeUUID7 = UUID.fromString("438a347b-e863-420a-b86f-69db6ab44ad5");

        Item item1, item2,item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13, item14, item15, item16, item17, item18, item19, item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30;
        item1=new Item("Tomato Sauce","p234058925","60019578",storeUUID1,31.99,1,"South Africa's firm favourite! It has a thick, smooth texture that can easily be poured and enjoyed on a variety of dishes.","item/tomatoSauce.png", "All Gold", "700ml", "Sauce");
        item2=new Item("Bar one","p123984123","6001068595808", storeUUID1,10.99,1,"Thick milk chocolate with nougat and caramel centre.","item/barOne.png", "Nestle", "55g", "Chocolate");
        item3=new Item("Milk","p423523144","6001007162474",storeUUID1,27.99,1,"Pasteurised, homogenised Full cream fresh milk","item/pnpMilk.png", "Pick n Pay", "2l", "Dairy");
        item4=new Item("Baked Beans","p623235254","6009522300586", storeUUID1,14.99,1,"Our iconic KOO Baked Beans in a rich tomato sauce, is extremely versatile and convenient. Delicious on its own, on toast and vetkoek, as a side to a breakfast, or as a quality ingredient in scrumptious weekday meals and curries.","item/kooBeans.png", "Koo", "410g", "Canned");
        item5=new Item("Bread", "p903932918", "6001205733520", storeUUID1, 15.49, 1, "Blue Ribbon has four delicious ranges of quality bread: Standard, Classic, Toaster and Lifestyle. Blue Ribbon Classic loaves are shorter and wider, with bigger slices for perfectly-filling sandwiches, while the Standard range of Sliced and Unsliced loaves are longer and thinner with smaller slices - perfect for lunchboxes.", "item/blueRibbon.png", "Blue Ribbon", "700g", "Bakery" );
        item6=new Item("Jolly Jammers", "p930458594", "6001056412360", storeUUID1, 28.99, 1, "Delicious buttery biscuits with choc cream sandiwched between, in a delightful smiley face to bring joy while you have your sweet treat.", "item/jammers.png", "Bakers", "200g", "Biscuit");
        item7=new Item("Ricoffy In Tin", "", "6001068323500", storeUUID1, 84.99, 1, "Ricoffy is South Africa's No. 1 coffee brand. Sharing memorable moments over a mug of hot coffee has been the tradition of many South African families. Today Ricoffy still brings you the true and original flavours and aroma that makes it the coffee of choice for most South Africans.", "item/ricoffy.png", "Nestle", "750g", "Beverages");
        item8=new Item("Creamy Tomato Soup", "", "6001007286309", storeUUID1, 39.99, 1, "Delicious creamy tomato soup prepared by the chefs at Pick n Pay.", "item/creamyTomato.png", "Pick n Pay", "600g", "Soup");
        item9=new Item("Creamy Butternut Soup", "", "6001007286323", storeUUID1, 29.99, 1, "Delicious butternut soup prepared by the chefs at Pick n Pay.", "item/creamyButternut.png", "Pick n Pay", "600g", "Soup");
        item10=new Item("Oros Original Orange Squash", "", "6001324011172", storeUUID1, 41.99, 1, "Mix this 2L tartrazine-free concentrate with cold water for a quick drink, the party table or your kids' school bags.", "item/originalOros.png", "Brooks", "2l", "Beverages");
        item11=new Item("Noodle's beef", "", "6001306002457", storeUUID1, 28.99, 1, "A convenient and easy meal to prepare, it can be used as a main meal ingredient or simply be enjoyed on its' own. Available in a variety of flavours, Kellogg's Instant Noodles can be consumed at any time of the day. Kellogg's Instant Noodles can help you feel energized and leave a sensational taste on the pallet.", "item/kellogNoodles.png", "Kelloggs", "70g", "Pantry");
        item12=new Item("Ultra Mel Vanilla Flavoured Custard", "", "6009708460257", storeUUID1, 20.99, 1, "Our creamy, thick and ultra-smooth blend of the finest vanilla makes it the perfect 100% treat to serve at every occasion. Whether you serve Ultra Mel hot or cold, over jelly or pudding or on its own, every spoonful is delicious and decadent.", "item/custard.png", "Danone", "1l", "Dairy");
        item13=new Item("Weet-Bix", "", "6001052001018", storeUUID1, 28.99, 1, "Wholegrain wheat biscuits.", "item/weetBix.png", "Bokomo", "450g", "Cereal");
        item14=new Item("Nescafe Classic Coffee", "", "7613033677724", storeUUID1, 85.99, 1, "Only the best coffee beans are selected. Blended. Roasted to perfection. So, dare to break the seal. Crack it open. Breathe it in. Let it set fire to your senses. Pure coffee. Nothing added. The rich full flavour, locked in. Just waiting to be released.", "item/nescaffeCoffee.png", "Nescafe", "500g", "Beverages");
        item15=new Item("Oreo original", "", "7622201779030", storeUUID1, 15.99, 1, "Oreo biscuits with vanilla flavoured filling, coated with milk chocolate.", "item/oreo.png", "OREO", "133g", "Biscuit");
        item16=new Item("Nescafe Classic Coffee", "", "7613033677724", storeUUID2, 85.99, 1, "Only the best coffee beans are selected. Blended. Roasted to perfection. So, dare to break the seal. Crack it open. Breathe it in. Let it set fire to your senses. Pure coffee. Nothing added. The rich full flavour, locked in. Just waiting to be released.", "item/nescaffeCoffee.png", "Nescafe", "500g", "Beverages");
        item17=new Item("Jolly Jammers", "p930458594", "6001056412360", storeUUID2, 28.99, 1, "Delicious buttery biscuits with choc cream sandiwched between, in a delightful smiley face to bring joy while you have your sweet treat.", "item/jammers.png", "Bakers", "200g", "Biscuit");
        item18=new Item("Ultra Mel Vanilla Flavoured Custard", "", "6009708460257", storeUUID2, 20.99, 1, "Our creamy, thick and ultra-smooth blend of the finest vanilla makes it the perfect 100% treat to serve at every occasion. Whether you serve Ultra Mel hot or cold, over jelly or pudding or on its own, every spoonful is delicious and decadent.", "item/custard.png", "Danone", "1l", "Dairy");
        item19=new Item("Weet-Bix", "", "6001052001018", storeUUID2, 28.99, 1, "Wholegrain wheat biscuits.", "item/weetBix.png", "Bokomo", "450g", "Cereal");
        item20=new Item("Oreo original", "", "7622201779030", storeUUID2, 15.99, 1, "Oreo biscuits with vanilla flavoured filling, coated with milk chocolate.", "item/oreo.png", "OREO", "133g", "Biscuit");
        item21=new Item("Strawberry yoghurt", "", "20048976", storeUUID2, 28.99, 1, "While no preservatives have been added, we have packed our delicious low fat strawberry yoghurt with an extra 16% fruit and added millions of live Bifidobacterium cultures. These cultures, as a regular part of a balanced diet, contribute to a balanced digestive system. So enjoy this thick, creamy and fruity yoghurt, and taste the quality you've always trusted us for.", "item/strawberryYoghurt.png", "Woolworths", "1kg", "Dairy");
        item22=new Item("White Thick Slice Bread", "", "20018702", storeUUID2, 16.69, 1, "A soft and fine textured white thick sliced bread.", "item/thickSlice.png", "Woolworths", "700g", "Bakery");
        item23=new Item("Fresh Full Cream Ayrshire Milk", "", "20026875", storeUUID2, 39.99, 1, "Our full cream Ayrshire milk promises a fresh, delicious taste. It may look like a glass of ordinary milk, but it isn't. It's our Ayrshire milk. Produced by Ayrshire cows, which receive no rBST hormones, it tastes richer, creamier and slightly sweeter than normal milk. It's delicious and exclusive to Woolworths.", "item/woolworthsMilk.png", "Woolworths", "2l", "Dairy");
        item24=new Item("Bulk Cheddar Cheese", "", "6009182131636", storeUUID2, 134.99, 1, "Enjoy our mild and slightly savoury cheddar cheese. Matured for a minimum of 2 months.", "item/woolworthsCheese.png", "Woolworths", "900g", "Dairy");
        item25=new Item("100% Apple Juice", "", "6009173967152", storeUUID2, 48.99, 1, "Our 100% Apple Juice contains no added preservatives and is a source of vitamin C.", "item/woolworthsAppleJuice.png", "Woolworths", "1.5l", "Beverages");
        item26=new Item("Ricoffy In Tin", "", "6001068323500", storeUUID2, 84.99, 1, "Ricoffy is South Africa's No. 1 coffee brand. Sharing memorable moments over a mug of hot coffee has been the tradition of many South African families. Today Ricoffy still brings you the true and original flavours and aroma that makes it the coffee of choice for most South Africans.", "item/ricoffy.png", "Nestle", "750g", "Beverages");
        item27=new Item("Tomato Sauce","p234058925","60019578",storeUUID2,31.99,1,"South Africa's firm favourite! It has a thick, smooth texture that can easily be poured and enjoyed on a variety of dishes.","item/sauce.png", "All Gold", "700ml", "Sauce");
        item28=new Item("Bar one","p123984123","6001068595808", storeUUID2,10.99,1,"Thick milk chocolate with nougat and caramel centre.","item/barOne.png", "Nestle", "55g", "Chocolate");
        item29=new Item("Baked Beans","p623235254","6009522300586", storeUUID2,14.99,1,"Our iconic KOO Baked Beans in a rich tomato sauce, is extremely versatile and convenient. Delicious on its own, on toast and vetkoek, as a side to a breakfast, or as a quality ingredient in scrumptious weekday meals and curries.","item/kooBeans.png", "Koo", "410g", "Canned");
        item30=new Item("Noodle's beef", "", "6001306002457", storeUUID2, 28.99, 1, "A convenient and easy meal to prepare, it can be used as a main meal ingredient or simply be enjoyed on its' own. Available in a variety of flavours, Kellogg's Instant Noodles can be consumed at any time of the day. Kellogg's Instant Noodles can help you feel energized and leave a sensational taste on the pallet.", "item/kellogNoodles.png", "Kelloggs", "70g", "Pantry");


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

        Store store1=new Store(storeUUID1, 7, 4, "Pick n Pay", 2, 5, true, "shop/pnp.png");
        Store store2=new Store(storeUUID2, 8, 10, "Woolworths", 2, 7, true, "shop/woolworths.png");
        Store store3=new Store(storeUUID3, 8, 9, "Checkers Hyper", 2, 7, true, "shop/checkers.png");
        Store store4=new Store(storeUUID4, 8, 10, "SuperSpar", 2, 7, true, "shop/superSpar.png");
        Store store5=new Store(storeUUID5, 8, 8, "Game", 2, 7, true, "shop/game.png");
        Store store6=new Store(storeUUID6, 7, 6, "Food Lover's Market", 2, 7, true, "shop/foodLoversMarket.png");
        Store store7=new Store(storeUUID7, 8, 8, "Pep", 2, 7, true, "shop/pep.png");
        GeoPoint store1Location = new GeoPoint(-25.762862391432126, 28.261305943073157, "Apple street");
        GeoPoint store2Location = new GeoPoint(-25.760319754713873, 28.278808593750004, "Banana Street");
        GeoPoint store3Location = new GeoPoint(-25.782541156164545, 28.261452959595255, "Grape street");
        GeoPoint store4Location = new GeoPoint(-25.705154853561545, 28.296656215156128, "Avo Street");
        GeoPoint store5Location = new GeoPoint(-25.725151681616511, 28.262516128952825, "Strawberry street");
        GeoPoint store6Location = new GeoPoint(-25.735282861315611, 28.225254252452454, "Blueberry Street");
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
