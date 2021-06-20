package cs.superleague.shopping.controller;

import cs.superleague.api.LibraryApi;
import cs.superleague.api.ShoppingApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.Book;
import cs.superleague.models.ItemObject;
import cs.superleague.models.ShoppingGetItemsRequest;
import cs.superleague.models.ShoppingGetItemsResponse;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.requests.GetItemsRequest;
import cs.superleague.shopping.responses.GetItemsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class ShoppingController implements ShoppingApi{

    @Autowired
    ShoppingServiceImpl shoppingService;

    @Autowired
    ItemRepo itemRepo;





    private boolean mockMode = true;

    //getItems endpoint
    @Override
    public ResponseEntity<ShoppingGetItemsResponse> getItems(ShoppingGetItemsRequest body) {

        //add mock data to repo
        Item item1, item2;
        item1=new Item("Heinz Tomato Sauce","123456","123456",UUID.fromString("store1"),36.99,1,"description","img/");
        item2=new Item("Bar one","012345","012345", UUID.fromString("store1"),14.99,3,"description","img/");
        itemRepo.save(item1); itemRepo.save(item2);


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
                GetItemsResponse getItemsResponse = ServiceSelector.getShoppingService().getItems(new GetItemsRequest(UUID.fromString("123456")));
                //ItemObject
                //response.setItems();
            } catch (StoreDoesNotExistException e) {

            } catch (InvalidRequestException e) {

            }

        }



        return new ResponseEntity<>(response, httpStatus);
    }



}
