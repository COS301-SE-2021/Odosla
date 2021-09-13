package code.rms2;

import cs.superleague.api.Rb2Api;
import cs.superleague.models.Rb2Request;
import cs.superleague.models.Rb2Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class controller implements Rb2Api {

    @Override
    public ResponseEntity<Rb2Response> message(Rb2Request body) {
        Rb2Response rr = new Rb2Response();
        rr.setMessage(body.getMessage());

        return new ResponseEntity<>(rr, HttpStatus.OK);
    }
}
