package cs.superleague.shopping.controller;

import cs.superleague.api.LibraryApi;
import cs.superleague.api.ShoppingApi;
import cs.superleague.models.Book;
import cs.superleague.models.ShoppingGetItemsRequest;
import cs.superleague.models.ShoppingGetItemsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
public class ShoppingController implements ShoppingApi{

    private boolean mockMode = true;

    //getItems endpoint
    @Override
    public ResponseEntity<ShoppingGetItemsResponse> getItems(ShoppingGetItemsRequest body) {

        //creating response object and default return status:
        ShoppingGetItemsResponse response = null;
        HttpStatus httpStatus = HttpStatus.OK;

        if (mockMode){
            //response.setJwtToken("298hrf29rh2hr923hgrf2873gt2e4ht");
            response.setStoreID("ChooseN'Pay_22b");
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}
