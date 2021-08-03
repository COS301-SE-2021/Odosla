package cs.superleague.user.requests;

import cs.superleague.user.dataclass.UserType;

import java.util.Calendar;
import java.util.UUID;

public class RegisterUserRequest {

    /* Attributes */
    private String name;
    private String surname;
    private String username;
    private UUID id;
    private String email;
    private String phoneNumber;
    private String password;
    private Calendar activationDate;
    private UserType accountType;


}
