package cs.superleague.recommendation.controller;

import cs.superleague.api.RecommendationApi;
import cs.superleague.models.*;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.recommendation.RecommendationService;
import cs.superleague.recommendation.requests.GenerateRecommendationTablePDFRequest;
import cs.superleague.recommendation.requests.GenerateRecommendationTableRequest;
import cs.superleague.recommendation.responses.GenerateRecommendationTablePDFResponse;
import cs.superleague.recommendation.responses.GenerateRecommendationTableResponse;
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
import java.util.UUID;

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
        try {

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            UUID storeOneID = null;
            UUID storeTwoID = null;
            UUID storeThreeID = null;
            if (body.getStoreOneID() != ""){
                storeOneID = UUID.fromString(body.getStoreOneID());
            }
            if (body.getStoreTwoID() != ""){
                storeTwoID = UUID.fromString(body.getStoreTwoID());
            }
            if (body.getStoreThreeID() != ""){
                storeThreeID = UUID.fromString(body.getStoreThreeID());
            }
            GetCartRecommendationRequest request = new GetCartRecommendationRequest(body.getItemIDs(), storeOneID, storeTwoID, storeThreeID);
            GetCartRecommendationResponse getCartRecommendationResponse = recommendationService.getCartRecommendation(request);
            try {
                response.setMessage(getCartRecommendationResponse.getMessage());
                response.setIsSuccess(getCartRecommendationResponse.isSuccess());
                response.setRecommendations(populateCartItems(getCartRecommendationResponse.getRecommendations()));
            } catch (Exception e) {
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
    private List<ItemObject> populateItems(List<Item> responseItems) throws NullPointerException {

        List<ItemObject> responseBody = new ArrayList<>();

        for (int i = 0; i < responseItems.size(); i++) {

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

    private List<CartItemObject> populateCartItems(List<CartItem> responseItems) throws NullPointerException {

        List<CartItemObject> responseBody = new ArrayList<>();

        for (CartItem i : responseItems) {

//            System.out.println("s id " + i.getStoreID().toString());

            CartItemObject item = new CartItemObject();
            if (i.getCartItemNo() != null) {
                item.setCartItemNo(i.getCartItemNo().toString());
            }
            item.setProductID(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            if (i.getStoreID() != null)
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

    @Override
    public ResponseEntity<RecommendationGenerateRecommendationTableResponse> generateRecommendationTable(RecommendationGenerateRecommendationTableRequest body) {
        RecommendationGenerateRecommendationTableResponse response = new RecommendationGenerateRecommendationTableResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GenerateRecommendationTableRequest request = new GenerateRecommendationTableRequest();
            GenerateRecommendationTableResponse generateRecommendationTableResponse = recommendationService.generateRecommendationTable(request);
            try {
                response.setMessage(generateRecommendationTableResponse.getMessage());
                response.setIsSuccess(generateRecommendationTableResponse.isSuccess());
                response.setRecommendationTable(generateRecommendationTableResponse.getRecommendationTable());
            } catch (Exception e) {
                e.printStackTrace();
                response.setMessage(e.getMessage());
                response.setIsSuccess(false);
                response.setRecommendationTable(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsSuccess(false);
            response.setRecommendationTable(null);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<RecommendationGenerateRecommendationTablePDFResponse> generateRecommendationTablePDF(
            RecommendationGenerateRecommendationTablePDFRequest body) {

        RecommendationGenerateRecommendationTablePDFResponse response = new
                RecommendationGenerateRecommendationTablePDFResponse();

        HttpStatus httpStatus = HttpStatus.OK;
        try {

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GenerateRecommendationTablePDFRequest request = new GenerateRecommendationTablePDFRequest(
                    body.getEmail());
            GenerateRecommendationTablePDFResponse generateRecommendationTableResponse =
                    recommendationService.generateRecommendationTablePDF(request);
            try {
                response.setMessage(generateRecommendationTableResponse.getMessage());
                response.setIsSuccess(generateRecommendationTableResponse.isSuccess());
            } catch (Exception e) {
                e.printStackTrace();
                response.setMessage(e.getMessage());
                response.setIsSuccess(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setIsSuccess(false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }
}
