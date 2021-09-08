package code;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("r1service")
public class r1ServiceImpl implements r1Service {

    @Override
    public String message(String x) {

        return "temp";
    }

    @Override
    public String obj(ObjMessage messageObject) {

        return "temp";
    }
}
