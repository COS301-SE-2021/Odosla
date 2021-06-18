package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.requests.GetItemsRequest;
import cs.superleague.user.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GetItemsIntegrationTest {
        @Autowired
        ShoppingServiceImpl shoppingService;

        //OPTIONAL SERVICES
        @Autowired
        UserServiceImpl userService;

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
            catalogue=new Catalogue(listOfItems);
            store=new Store(storeID,"Woolworths",catalogue,2,null,null,4,false);
            store.setOpeningTime(7);
            store.setClosingTime(22);
        }

        @AfterEach
        void tearDown() {
        }

        @Test
        @Description("Tests whether the GetItemsRequest object is constructed correctly")
        @DisplayName("GetItemsRequest correct construction")
        void IntegrationTest_GetItemsRequestConstruction() {

            getItemsRequest=new GetItemsRequest(storeID);
            assertNotNull(getItemsRequest);
            assertEquals(storeID, getItemsRequest.getStoreID());
        }

}


