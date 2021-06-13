package cs.superleague.shopping.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class ShoppingController { //implements shopping api

    @RequestMapping("/test")
    public String test(){
        return "Test complete";
    }

}
