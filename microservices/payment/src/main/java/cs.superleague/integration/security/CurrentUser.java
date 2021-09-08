package cs.superleague.integration.security;

import cs.superleague.integration.dataclass.UserType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class CurrentUser {

    private UserType userType;
    private String email;

    public CurrentUser(){
        try {
            Map<String, Object> i = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getDetails();
            String type = (String) i.get("userType");
            String e = (String) i.get("email");
            this.email = e;
            if (type.equals("SHOPPER")) {
                userType = UserType.SHOPPER;
            } else if (type.equals("DRIVER")) {
                userType = UserType.DRIVER;
            } else if (type.equals("CUSTOMER")) {
                userType = UserType.CUSTOMER;
            } else if (type.equals("ADMIN")) {
                userType = UserType.ADMIN;
            }
        } catch (NullPointerException e){
            userType = null;
            email = null;
        }

    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}