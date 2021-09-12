package cs.superleague.shopping.rabbit;

import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SaveItemToRepoListener implements MessageListener {
    private final ShoppingService shoppingService;

    public SaveItemToRepoListener(ShoppingService shoppingService)
    {
        this.shoppingService = shoppingService;
    }

    @Override
    public void onMessage(Message message) {
        SaveItemToRepoRequest request = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            request = (SaveItemToRepoRequest) in.readObject();
            SaveItemToRepoRequest saveItemToRepoRequest = new SaveItemToRepoRequest(request.getItem());
            shoppingService.saveItemToRepo(saveItemToRepoRequest);
        } catch (InvalidRequestException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
