package cs.superleague.recommendation.controller;

import cs.superleague.api.RecommendationApi;
import cs.superleague.models.ItemObject;
import cs.superleague.models.RecommendationGetCartRecommendationRequest;
import cs.superleague.models.RecommendationGetCartRecommendationResponse;
import cs.superleague.recommendation.RecommendationService;
import cs.superleague.recommendation.stubs.Order;
import cs.superleague.recommendation.stubs.Item;
import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
public class RecommendationController implements RecommendationApi {
    @Autowired
    RecommendationRepo recommendationRepo;
    @Autowired
    RecommendationService recommendationService;

    @Override
    public ResponseEntity<RecommendationGetCartRecommendationResponse> getCartRecommendation(RecommendationGetCartRecommendationRequest body) {
//        UUID orderID = UUID.randomUUID();
//        Order order = new Order();
//        UUID storeUUID1 = UUID.randomUUID();
//        Item item1=new Item("Tomato Sauce","p234058925","60019578",storeUUID1,31.99,1,"South Africa's firm favourite! It has a thick, smooth texture that can easily be poured and enjoyed on a variety of dishes.","item/tomatoSauce.png", "All Gold", "700ml", "Sauce");
//        Item item2=new Item("Bar one","p123984123","6001068595808", storeUUID1,10.99,1,"Thick milk chocolate with nougat and caramel centre.","item/barOne.png", "Nestle", "55g", "Chocolate");
//        Item item3=new Item("Milk","p423523144","6001007162474",storeUUID1,27.99,1,"Pasteurised, homogenised Full cream fresh milk","item/pnpMilk.png", "Pick n Pay", "2l", "Dairy");
//        itemRepo.save(item1);
//        itemRepo.save(item2);
//        itemRepo.save(item3);
//        List<Item> itemOrders = new ArrayList<>();
//        itemOrders.add(item1);
//        itemOrders.add(item2);
//        itemOrders.add(item3);
//        order.setItems(itemOrders);
//        orderRepo.save(order);
//        Recommendation recommendation1 = new Recommendation(UUID.randomUUID(), "p234058925", orderID);
//        Recommendation recommendation2 = new Recommendation(UUID.randomUUID(), "p123984123", orderID);
//        Recommendation recommendation3 = new Recommendation(UUID.randomUUID(), "p423523144", orderID);
//        recommendationRepo.save(recommendation1);
//        recommendationRepo.save(recommendation2);
//        recommendationRepo.save(recommendation3);
        RecommendationGetCartRecommendationResponse response = new RecommendationGetCartRecommendationResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{
            GetCartRecommendationRequest request = new GetCartRecommendationRequest(body.getItemIDs());
            GetCartRecommendationResponse getCartRecommendationResponse = recommendationService.getCartRecommendation(request);
            try {
                response.setMessage(getCartRecommendationResponse.getMessage());
                response.setIsSuccess(getCartRecommendationResponse.isSuccess());
                response.setRecommendations(populateItems(getCartRecommendationResponse.getRecommendations()));
            } catch (Exception e){
                e.printStackTrace();
                response.setMessage(e.getMessage());
                response.setIsSuccess(false);
                response.setRecommendations(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsSuccess(false);
            response.setRecommendations(null);
        }
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
            currentItem.setBrand(responseItems.get(i).getBrand());
            currentItem.setItemType(responseItems.get(i).getItemType());
            currentItem.setSize(responseItems.get(i).getSize());

            responseBody.add(currentItem);

        }

        return responseBody;
    }
}
