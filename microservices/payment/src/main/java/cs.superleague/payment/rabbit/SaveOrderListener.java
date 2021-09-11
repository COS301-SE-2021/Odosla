package cs.superleague.payment.rabbit;

import cs.superleague.payment.PaymentService;
import cs.superleague.payment.requests.SaveOrderRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SaveOrderListener implements MessageListener {
    @Autowired
    PaymentService paymentService;
    @Override
    public void onMessage(Message message) {
        SaveOrderRequest request = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            request = (SaveOrderRequest) in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
