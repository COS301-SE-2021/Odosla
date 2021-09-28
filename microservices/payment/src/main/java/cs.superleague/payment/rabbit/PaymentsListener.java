package cs.superleague.payment.rabbit;

import cs.superleague.payment.PaymentService;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class PaymentsListener implements MessageListener {
    private final PaymentService paymentService;

    public PaymentsListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void onMessage(Message message) {

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            Object o = in.readObject();
            if (o instanceof SaveOrderToRepoRequest) {
                System.out.println("i made it here 1");
                SaveOrderToRepoRequest request = (SaveOrderToRepoRequest) o;
                SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(request.getOrder());
                paymentService.saveOrderToRepo(saveOrderToRepoRequest);
                System.out.println("I made it here 3");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }
}
