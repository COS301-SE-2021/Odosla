package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetItemsRequest;
import cs.superleague.shopping.responses.GetItemsResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GetItemsIntegrationTest {
    @Autowired
    ShoppingServiceImpl shoppingService;

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

        itemRepo.save(item1);
        itemRepo.save(item2);
        catalogueRepo.save(catalogue);
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests if the given GetItems request when getItemsRequest is null - exception should be thrown")
    @DisplayName("When order request object null")
    void IntegrationTest_GetItemsNullRequest() throws InvalidRequestException {
        getItemsRequest = null;

        assertThrows(InvalidRequestException.class, ()-> {
            GetItemsResponse getItemsResponse = shoppingService.getItems(getItemsRequest);
        });
    }

    @Test
    @Description("Tests if the given getItems request on creation has a null parameter - exception should be thrown")
    @DisplayName("When getItems parameter of request object null")
    void IntegrationTest_GetItemsWithNullParameterRequest() throws InvalidRequestException {
        getItemsRequest = new GetItemsRequest(null);
        assertThrows(InvalidRequestException.class, ()-> {
            GetItemsResponse getItemsResponse = shoppingService.getItems(getItemsRequest);
        });
    }

    @Test
    @Description("Tests for if the store does not exist")
    @DisplayName("When getItems parameter gives id of a store that does not exist")
    void IntegrationTest_StoreDoesNotExist() throws StoreDoesNotExistException {
        UUID storeID2=UUID.randomUUID();
        getItemsRequest=new GetItemsRequest(storeID2);
        assertThrows(StoreDoesNotExistException.class, ()-> {
            GetItemsResponse getItemsResponse = shoppingService.getItems(getItemsRequest);
        });
    }

    @Test
    @Description("Tests whether the getItemsRequest object is constructed correctly")
    @DisplayName("getItemsRequest correct construction")
    void IntegrationTest_GetItemsRequestConstruction() {

        getItemsRequest=new GetItemsRequest(storeID);

        assertNotNull(getItemsRequest);
        assertEquals(storeID,getItemsRequest.getStoreID());
    }

    @Test
    @Description("This test checks whether getItems correctly returns valid data stored in catalogue entity")
    @DisplayName("When getItems returns the correct data")
    void IntegrationTest_GetItemsConstruction() throws InvalidRequestException, StoreDoesNotExistException {
        List<Item> storeItems = null;
        getItemsRequest=new GetItemsRequest(storeID);
        GetItemsResponse getItemsResponse= shoppingService.getItems(getItemsRequest);

        if(catalogueRepo.findById(getItemsResponse.getStoreID()).isPresent()){
            storeItems= catalogueRepo.findById(getItemsResponse.getStoreID()).get().getItems();
        }

        assertNotNull(storeItems);
        assertEquals(listOfItems.size(), storeItems.size());

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


