package code.rms1;

import code.rms2.requests.ObjMessage;
import cs.superleague.api.RbApi;
import cs.superleague.models.Rb1Request;
import cs.superleague.models.Rb1Response;
import cs.superleague.models.Rb1objRequest;
import cs.superleague.models.Rb1objResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class controller implements RbApi {

    @Autowired
    private RabbitTemplate rabbit;

    @Override
    public ResponseEntity<Rb1Response> message(Rb1Request body) {

        //return default message
        Rb1Response rr = new Rb1Response();
        rr.setMessage(body.getMessage());

        //publish message
        rabbit.convertAndSend("CatExchange", "KEY_CAT", "Meow from tom");

        return new ResponseEntity<>(rr, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Rb1objResponse> obj(Rb1objRequest body) {
        //return default message
        Rb1objResponse rr = new Rb1objResponse();
        rr.setMessage(body.getName());

        ObjMessage m = new ObjMessage(body.getName(), body.getType());
        rabbit.convertAndSend("CatExchange", "KEY_CAT", m);

        //publish message

        return new ResponseEntity<>(rr, HttpStatus.OK);
    }


}
