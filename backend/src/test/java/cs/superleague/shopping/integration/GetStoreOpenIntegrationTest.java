package cs.superleague.shopping.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.payment.dataclass.OrderType;
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
import cs.superleague.shopping.requests.GetCatalogueRequest;
import cs.superleague.shopping.requests.GetStoreOpenRequest;
import cs.superleague.shopping.responses.GetCatalogueResponse;
import cs.superleague.shopping.responses.GetStoreOpenResponse;
import cs.superleague.user.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetStoreOpenIntegrationTest {
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
    GetStoreOpenRequest getStoreOpenRequest;

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

        itemRepo.save(item1);
        itemRepo.save(item2);
        catalogueRepo.save(catalogue);
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();
    }

    @Test
    @Description("Tests if the given getStore request when getStoreOpen is null - exception should be thrown")
    @DisplayName("When getStoreOpen request object null")
    void IntegrationTest_GetStoreOpenNullRequest() throws InvalidRequestException {
        getStoreOpenRequest = null;

        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            GetStoreOpenResponse getStoreOpenResponse = ServiceSelector.getShoppingService().getStoreOpen(getStoreOpenRequest);
        });
    }

    @Test
    @Description("Tests if the given getStoreOpen request on creation has a null parameter - exception should be thrown")
    @DisplayName("When getStoreOpen parameter of request object null")
    void IntegrationTest_GetStoreOpenWithNullParameterRequest() throws InvalidRequestException {
        getStoreOpenRequest = new GetStoreOpenRequest(null);
        assertThrows(cs.superleague.shopping.exceptions.InvalidRequestException.class, ()-> {
            GetStoreOpenResponse getStoreOpenResponse = ServiceSelector.getShoppingService().getStoreOpen(getStoreOpenRequest);
        });
    }

    @Test
    @Description("Tests for if the store does not exist")
    @DisplayName("When getCatalogue parameter gives id of a store that does not exist")
    void IntegrationTest_StoreDoesNotExist() throws StoreDoesNotExistException {
        UUID storeID2=UUID.randomUUID();
        getStoreOpenRequest=new GetStoreOpenRequest(storeID2);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            GetStoreOpenResponse getStoreOpenResponse = ServiceSelector.getShoppingService().getStoreOpen(getStoreOpenRequest);
        });
    }

    @Test
    @Description("Tests whether the getStoreOpen request object is constructed correctly")
    @DisplayName("getStoreOpenRequest correct construction")
    void IntegrationTest_GetStoreOpenRequestConstruction() {

        getStoreOpenRequest=new GetStoreOpenRequest(storeID);

        assertNotNull(getStoreOpenRequest);
        assertEquals(storeID,getStoreOpenRequest.getStoreID());
    }

    @Test
    @Description("This test checks whether getStoreOpen correctly returns valid data stored in catalogue entity")
    @DisplayName("When getStoreOpen returns the correct data")
    void IntegrationTest_GetStoreOpenConstruction() throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException {
        Store getStore=null;
        List<Item> storeItems = null;
        Catalogue storeCatalogue= null;
        getStoreOpenRequest=new GetStoreOpenRequest(storeID);
        GetStoreOpenResponse getStoreOpenResponse= ServiceSelector.getShoppingService().getStoreOpen(getStoreOpenRequest);

        if(storeRepo.findById(getStoreOpenResponse.getStoreID()).isPresent()){
            getStore= storeRepo.findById(getStoreOpenResponse.getStoreID()).get();
        }

        assertNotNull(getStore);
        assertEquals(listOfItems.size(), getStore.getStock().getItems().size());
        assertEquals(store.getStoreID(), getStore.getStoreID());
        Calendar calendar= Calendar.getInstance();
        if(calendar.get(Calendar.HOUR_OF_DAY) >= getStore.getOpeningTime() && calendar.get(Calendar.HOUR_OF_DAY) < getStore.getClosingTime())
        {
            assertEquals(true, getStore.getOpen());
        }
        else
        {
            assertEquals(false, getStore.getOpen());
        }

        assertEquals(store.getOpeningTime(), getStore.getOpeningTime());
        assertEquals(store.getClosingTime(), getStore.getClosingTime());
        assertEquals(store.getMaxOrders(), getStore.getMaxOrders());
        assertEquals(store.getStoreBrand(), getStore.getStoreBrand());
        assertEquals(store.getStoreID(), getStore.getStoreID());


        storeItems= getStore.getStock().getItems();
        Iterator<Item> itemIterator_it = listOfItems.iterator();
        Iterator<Item> storeItemIterator_it = storeItems.iterator();
        while(itemIterator_it.hasNext() && storeItemIterator_it.hasNext()){

            Item item=itemIterator_it.next();
            Item storeItem=storeItemIterator_it.next();

            assertEquals(item.getPrice(),storeItem.getPrice());
            assertEquals(item.getQuantity(),storeItem.getQuantity());
            assertEquals(item.getBarcode(),storeItem.getBarcode());
            assertEquals(item.getStoreID(),storeItem.getStoreID());
            assertEquals(item.getDescription(),storeItem.getDescription());
            assertEquals(item.getProductID(),storeItem.getProductID());
            assertEquals(item.getImageUrl(),storeItem.getImageUrl());
        }
    }
}
