package cs.superleague.payment.rabbit;

import cs.superleague.payment.PaymentService;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Component
public class PaymentsListener implements MessageListener {
    private final PaymentService paymentService;

    public PaymentsListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void onMessage(Message message) {
        SaveOrderToRepoRequest request = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            request = (SaveOrderToRepoRequest) in.readObject();
            SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(request.getOrder());
            paymentService.saveOrderToRepo(saveOrderToRepoRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
