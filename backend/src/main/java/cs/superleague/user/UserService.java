package cs.superleague.user;

import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException;
    public ScanItemResponse scanItem(ScanItemRequest request);


}