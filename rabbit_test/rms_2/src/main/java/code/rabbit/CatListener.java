package code.rabbit;

import code.other.ObjMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static code.utility.DeserializationUtility.deSerializeObjectFromString;

public class CatListener implements MessageListener {
    @Override
    public void onMessage(Message message) {

        ObjMessage obj = null;

        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));

            obj = (ObjMessage) in.readObject();

            System.out.println("message received: " + obj.toString());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
