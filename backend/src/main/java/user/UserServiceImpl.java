package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payment.repos.OrderRepo;
import shopping.ShoppingService;
import user.repos.ShopperRepo;
import user.responses.*;
import user.requests.*;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    private final ShopperRepo shopperRepo;
    private final UserService userService;

    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo, UserService userService) {
        this.shopperRepo = shopperRepo;
        this.userService = userService;
    }

    @Override
    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) {



        return null;

    }

    @Override
    public ScanItemResponse scanItem(ScanItemRequest request) {
        return null;
    }


}