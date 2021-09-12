package cs.superleague.shopping.rabbit;

import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import cs.superleague.shopping.requests.SaveStoreToRepoRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ShoppingListener implements MessageListener {
    private final ShoppingService shoppingService;

    public ShoppingListener(ShoppingService shoppingService)
    {
        this.shoppingService = shoppingService;
    }

    @Override
    public void onMessage(Message message) {

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            if(in.readObject() instanceof SaveItemToRepoRequest)
            {
                SaveItemToRepoRequest request = (SaveItemToRepoRequest) in.readObject();
                SaveItemToRepoRequest saveItemToRepoRequest = new SaveItemToRepoRequest(request.getItem());
                shoppingService.saveItemToRepo(saveItemToRepoRequest);
            }
            else if(in.readObject() instanceof SaveStoreToRepoRequest)
            {
                SaveStoreToRepoRequest request = (SaveStoreToRepoRequest) in.readObject();
                SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(request.getStore());
                shoppingService.saveStoreToRepo(saveStoreToRepoRequest);
            }

        } catch (InvalidRequestException | IOException | ClassNotFoundException | StoreDoesNotExistException e) {
            e.printStackTrace();
        }
    }
}
