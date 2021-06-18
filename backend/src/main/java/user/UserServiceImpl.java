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

    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, UserDoesNotExistException {
        GetShopperByUUIDResponse response=null;

        if(request!=null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("User ID in request object for get can't be null");
            }

            User userEntity=null;

            userEntity = userRepo.findById(request.getUserID()).orElse(null);

            if(userEntity==null){
                throw new UserDoesNotExistException("User with id given doesn't exist in repository");
            }


            response=new GetShopperByUUIDResponse(userEntity,Calendar.getInstance().getTime(), "User was returned succesfully");

        }
        else{
            throw new InvalidRequestException("Request object can't be null for getShopperByUUID");
        }

        return response;
    }


}