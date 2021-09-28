package cs.superleague.recommendation.rabbit;

import cs.superleague.recommendation.RecommendationService;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class RecommendationListener implements MessageListener {

    private final RecommendationService recommendationService;

    public RecommendationListener(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Override
    public void onMessage(Message message) {
        AddRecommendationRequest addRecommendationRequest = null;
        RemoveRecommendationRequest removeRecommendationRequest = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            Object o = in.readObject();
            if (o instanceof AddRecommendationRequest) {
                addRecommendationRequest = (AddRecommendationRequest) o;
                recommendationService.addRecommendation(addRecommendationRequest);
            } else if (o instanceof RemoveRecommendationRequest) {
                removeRecommendationRequest = (RemoveRecommendationRequest) o;
                recommendationService.removeRecommendation(removeRecommendationRequest);
            }
        } catch (IOException | ClassNotFoundException | InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
