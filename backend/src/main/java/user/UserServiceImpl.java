package user;

import org.springframework.beans.factory.annotation.Autowired;
import shopping.responses.ClearShoppersResponse;
import user.dataclass.Shopper;
import user.dataclass.User;
import user.exceptions.*;
import user.repos.UserRepo;
import user.responses.*;
import user.requests.*;

import java.util.Calendar;
import java.util.List;

public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public RegisterResponse registerUser(RegisterUserRequest registerRequest) {
        return null;
    }

    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest) {
        return null;
    }

}