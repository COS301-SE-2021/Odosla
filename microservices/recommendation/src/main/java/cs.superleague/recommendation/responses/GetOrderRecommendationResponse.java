package cs.superleague.recommendation.responses;

import cs.superleague.recommendation.stubs.Item;

import java.util.List;

public class GetOrderRecommendationResponse {
    private List<Item> recommendations;
    private boolean isSuccess;
    private String message;
}
