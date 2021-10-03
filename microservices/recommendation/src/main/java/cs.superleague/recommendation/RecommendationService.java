package cs.superleague.recommendation;

import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.requests.*;
import cs.superleague.recommendation.responses.GenerateRecommendationTableResponse;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;
import cs.superleague.shopping.exceptions.ItemDoesNotExistException;

import java.net.URISyntaxException;

public interface RecommendationService {
    GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException, URISyntaxException, ItemDoesNotExistException;

    GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request);

    void addRecommendation(AddRecommendationRequest request) throws InvalidRequestException;

    void removeRecommendation(RemoveRecommendationRequest request) throws InvalidRequestException;

    GenerateRecommendationTableResponse generateRecommendationTable(GenerateRecommendationTableRequest request) throws InvalidRequestException, URISyntaxException;
}
