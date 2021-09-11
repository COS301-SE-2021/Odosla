package cs.superleague.recommendation.rabbit;

import cs.superleague.recommendation.RecommendationService;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class AddRecommendationListener implements MessageListener {
    @Autowired
    RecommendationService recommendationService;
    @Override
    public void onMessage(Message message) {
        AddRecommendationRequest addRecommendationRequest = null;
        try{
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            addRecommendationRequest = (AddRecommendationRequest) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
