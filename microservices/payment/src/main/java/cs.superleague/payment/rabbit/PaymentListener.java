package cs.superleague.payment.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PaymentListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

    }
}
