package cs.superleague.user.rabbit;

import cs.superleague.user.UserService;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.requests.SaveDriverRequest;
import cs.superleague.user.requests.SaveShopperRequest;
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
            if (o instanceof SaveDriverRequest){
                SaveDriverRequest saveDriverRequest = (SaveDriverRequest) o;
                userService.saveDriver(saveDriverRequest);
            } else if (o instanceof SaveShopperRequest){
                SaveShopperRequest saveShopperRequest = (SaveShopperRequest) o;
                userService.saveShopper(saveShopperRequest);
            }

        } catch (IOException | ClassNotFoundException | InvalidRequestException e) {
            e.printStackTrace();
        }


    }
}
