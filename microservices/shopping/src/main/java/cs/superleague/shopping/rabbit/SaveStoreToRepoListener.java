package cs.superleague.shopping.rabbit;

import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.SaveStoreToRepoRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SaveStoreToRepoListener implements MessageListener {
    private final ShoppingService shoppingService;

    public SaveStoreToRepoListener(ShoppingService shoppingService)
    {
        this.shoppingService = shoppingService;
    }

    @Override
    public void onMessage(Message message) {
        SaveStoreToRepoRequest request = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            request = (SaveStoreToRepoRequest) in.readObject();
            SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(request.getStore());
            shoppingService.saveStoreToRepo(saveStoreToRepoRequest);
        } catch (InvalidRequestException | StoreDoesNotExistException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
