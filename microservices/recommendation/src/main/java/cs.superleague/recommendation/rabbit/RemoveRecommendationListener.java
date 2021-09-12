package cs.superleague.recommendation.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
public class RemoveRecommendationListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

    }
}
