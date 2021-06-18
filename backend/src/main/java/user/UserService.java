package user;

import user.exceptions.InvalidRequestException;
import user.exceptions.UserDoesNotExistException;
import user.requests.*;
import user.responses.*;

public interface UserService {
    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);
    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, UserDoesNotExistException;

}