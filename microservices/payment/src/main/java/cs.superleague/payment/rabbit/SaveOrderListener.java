package cs.superleague.payment.rabbit;

import cs.superleague.payment.PaymentService;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.requests.SaveOrderRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Component
public class SaveOrderListener implements MessageListener {
    private final PaymentService paymentService;

    public SaveOrderListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void onMessage(Message message) {
        SaveOrderRequest request = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            request = (SaveOrderRequest) in.readObject();
            SaveOrderRequest saveOrderRequest = new SaveOrderRequest(request.getOrder());
            paymentService.saveOrder(saveOrderRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
