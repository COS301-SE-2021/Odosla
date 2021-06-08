package cs.superleague.shopping.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingController {

    @RequestMapping("/test")
    public String test(){
        return "Test complete";
    }
}
