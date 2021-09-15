package cs.superleague.recommendation.controller;

import cs.superleague.api.RecommendationApi;
import cs.superleague.models.ItemObject;
import cs.superleague.models.RecommendationGetCartRecommendationRequest;
import cs.superleague.models.RecommendationGetCartRecommendationResponse;
import cs.superleague.recommendation.RecommendationService;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class RecommendationController implements RecommendationApi {

    RecommendationRepo recommendationRepo;
    RecommendationService recommendationService;
    RestTemplate restTemplate;
    HttpServletRequest httpServletRequest;

    @Autowired
    public RecommendationController(RecommendationRepo recommendationRepo, RecommendationService recommendationService, RestTemplate restTemplate, HttpServletRequest httpServletRequest) {
        this.recommendationRepo = recommendationRepo;
        this.recommendationService = recommendationService;
        this.restTemplate = restTemplate;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ResponseEntity<RecommendationGetCartRecommendationResponse> getCartRecommendation(RecommendationGetCartRecommendationRequest body) {
        RecommendationGetCartRecommendationResponse response = new RecommendationGetCartRecommendationResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

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
