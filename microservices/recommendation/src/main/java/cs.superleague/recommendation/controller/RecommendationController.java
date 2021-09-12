package cs.superleague.recommendation.controller;

import cs.superleague.api.RecommendationApi;
import cs.superleague.models.ItemObject;
import cs.superleague.models.RecommendationGetCartRecommendationRequest;
import cs.superleague.models.RecommendationGetCartRecommendationResponse;
import cs.superleague.recommendation.RecommendationService;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import cs.superleague.recommendation.stubs.shopping.Item;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class RecommendationController implements RecommendationApi {
    @Autowired
    RecommendationRepo recommendationRepo;
    @Autowired
    RecommendationService recommendationService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<RecommendationGetCartRecommendationResponse> getCartRecommendation(RecommendationGetCartRecommendationRequest body) {
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
