package cs.superleague.user.rabbit;

import cs.superleague.user.UserService;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.requests.SaveDriverToRepoRequest;
import cs.superleague.user.requests.SaveShopperToRepoRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class UserListener implements MessageListener {
    private final UserService userService;

    public UserListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onMessage(Message message) {

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
            Object o = in.readObject();
            if (o instanceof SaveDriverToRepoRequest) {
                SaveDriverToRepoRequest saveDriverToRepoRequest = (SaveDriverToRepoRequest) o;
                userService.saveDriver(saveDriverToRepoRequest);
            } else if (o instanceof SaveShopperToRepoRequest) {
                SaveShopperToRepoRequest saveShopperToRepoRequest = (SaveShopperToRepoRequest) o;
                userService.saveShopper(saveShopperToRepoRequest);
            }

        } catch (IOException | ClassNotFoundException | InvalidRequestException e) {
            e.printStackTrace();
        }


    }
}
