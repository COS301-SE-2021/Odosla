package code;

import cs.superleague.api.RbApi;
import cs.superleague.models.Rb1Request;
import cs.superleague.models.Rb1Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class controller implements RbApi {
    @Override
    public ResponseEntity<Rb1Response> message(Rb1Request body) {
        Rb1Response rr = new Rb1Response();
        rr.setMessage(body.getMessage());

        return new ResponseEntity<>(rr, HttpStatus.OK);
    }
}
