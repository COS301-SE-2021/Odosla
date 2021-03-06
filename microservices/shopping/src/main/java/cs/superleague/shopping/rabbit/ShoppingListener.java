package cs.superleague.shopping.rabbit;

import cs.superleague.shopping.ShoppingService;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.*;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ShoppingListener implements MessageListener {
    private final ShoppingService shoppingService;

    public ShoppingListener(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @Override
    public void onMessage(Message message) {

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            Object o = in.readObject();
            if (o instanceof SaveItemToRepoRequest) {
                SaveItemToRepoRequest request = (SaveItemToRepoRequest) o;
                SaveItemToRepoRequest saveItemToRepoRequest = new SaveItemToRepoRequest(request.getItem());
                shoppingService.saveItemToRepo(saveItemToRepoRequest);
            } else if (o instanceof SaveStoreToRepoRequest) {
                SaveStoreToRepoRequest request = (SaveStoreToRepoRequest) o;
                SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(request.getStore());
                shoppingService.saveStoreToRepo(saveStoreToRepoRequest);
            } else if (o instanceof AddToQueueRequest) {
                AddToQueueRequest request = (AddToQueueRequest) o;
                AddToQueueRequest addToQueueRequest = new AddToQueueRequest(request.getOrder());
                shoppingService.addToQueue(addToQueueRequest);
            } else if (o instanceof AddToFrontOfQueueRequest) {
                AddToFrontOfQueueRequest request = (AddToFrontOfQueueRequest) o;
                AddToFrontOfQueueRequest addToFrontOfQueueRequest = new AddToFrontOfQueueRequest(request.getOrder());
                shoppingService.addToFrontOfQueue(addToFrontOfQueueRequest);
            } else if (o instanceof SaveCatalogueToRepoRequest) {
                SaveCatalogueToRepoRequest request = (SaveCatalogueToRepoRequest) o;
                SaveCatalogueToRepoRequest saveCatalogueToRepoRequest = new SaveCatalogueToRepoRequest(request.getCatalogue());
                shoppingService.saveCatalogueToRepo(saveCatalogueToRepoRequest);
            }

        } catch (InvalidRequestException | IOException | ClassNotFoundException | StoreDoesNotExistException e) {
            e.printStackTrace();
        }
    }
}
