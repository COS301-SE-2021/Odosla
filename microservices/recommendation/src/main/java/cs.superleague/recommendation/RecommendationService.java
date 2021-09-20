package cs.superleague.recommendation;

import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;

import java.net.URISyntaxException;

public interface RecommendationService {
    GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException, URISyntaxException;

    GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request);

    void addRecommendation(AddRecommendationRequest request) throws InvalidRequestException;

    void removeRecommendation(RemoveRecommendationRequest request) throws InvalidRequestException;
}
