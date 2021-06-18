package cs.superleague.integration;

import cs.superleague.api.LibraryApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements LibraryApi {


    @Override
    public ResponseEntity<String> getAllBooksInLibrary() {
        return new ResponseEntity<String>(new String("test"), HttpStatus.OK);
    }
}
