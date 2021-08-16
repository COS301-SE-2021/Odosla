/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.18).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package cs.superleague.api;

import cs.superleague.models.UserAccountVerifyRequest;
import cs.superleague.models.UserAccountVerifyResponse;
import cs.superleague.models.UserClearShoppingCartRequest;
import cs.superleague.models.UserClearShoppingCartResponse;
import cs.superleague.models.UserCompletePackagingOrderRequest;
import cs.superleague.models.UserCompletePackagingOrderResponse;
import cs.superleague.models.UserGetCurrentUserRequest;
import cs.superleague.models.UserGetCurrentUserResponse;
import cs.superleague.models.UserGetGroceryListRequest;
import cs.superleague.models.UserGetGroceryListResponse;
import cs.superleague.models.UserGetShoppingCartRequest;
import cs.superleague.models.UserGetShoppingCartResponse;
import cs.superleague.models.UserLoginRequest;
import cs.superleague.models.UserLoginResponse;
import cs.superleague.models.UserMakeGroceryListRequest;
import cs.superleague.models.UserMakeGroceryListResponse;
import cs.superleague.models.UserRegisterAdminRequest;
import cs.superleague.models.UserRegisterAdminResponse;
import cs.superleague.models.UserRegisterCustomerRequest;
import cs.superleague.models.UserRegisterCustomerResponse;
import cs.superleague.models.UserRegisterDriverRequest;
import cs.superleague.models.UserRegisterDriverResponse;
import cs.superleague.models.UserRegisterShopperRequest;
import cs.superleague.models.UserRegisterShopperResponse;
import cs.superleague.models.UserResetPasswordRequest;
import cs.superleague.models.UserResetPasswordResponse;
import cs.superleague.models.UserScanItemRequest;
import cs.superleague.models.UserScanItemResponse;
import cs.superleague.models.UserSetCartRequest;
import cs.superleague.models.UserSetCartResponse;
import cs.superleague.models.UserSetCurrentLocationRequest;
import cs.superleague.models.UserSetCurrentLocationResponse;
import cs.superleague.models.UserUpdateDriverShiftRequest;
import cs.superleague.models.UserUpdateDriverShiftResponse;
import cs.superleague.models.UserUpdateShopperShiftRequest;
import cs.superleague.models.UserUpdateShopperShiftResponse;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@Api(value = "user", description = "the user API")
public interface UserApi {

    @ApiOperation(value = "Endpoint for clearing a shopping cart", nickname = "clearShoppingCart", notes = "Refer to summary", response = UserClearShoppingCartResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserClearShoppingCartResponse.class) })
    @RequestMapping(value = "/user/clearShoppingCart",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserClearShoppingCartResponse> clearShoppingCart(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserClearShoppingCartRequest body
);


    @ApiOperation(value = "Endpoint for complete packaging order use case", nickname = "completePackagingOrder", notes = "Refer to summary", response = UserCompletePackagingOrderResponse.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns whether the email was sent", response = UserCompletePackagingOrderResponse.class) })
    @RequestMapping(value = "/user/completePackagingOrder",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserCompletePackagingOrderResponse> completePackagingOrder(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserCompletePackagingOrderRequest body
);


    @ApiOperation(value = "Endpoint for to get Current user", nickname = "getCurrentUser", notes = "Refer to summary", response = UserGetCurrentUserResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserGetCurrentUserResponse.class) })
    @RequestMapping(value = "/user/getCurrentUser",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserGetCurrentUserResponse> getCurrentUser(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserGetCurrentUserRequest body
);


    @ApiOperation(value = "Endpoint for get grocery lists use case", nickname = "getGroceryLists", notes = "Refer to summary", response = UserGetGroceryListResponse.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns the groceryList", response = UserGetGroceryListResponse.class) })
    @RequestMapping(value = "/user/getGroceryLists",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserGetGroceryListResponse> getGroceryLists(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserGetGroceryListRequest body
);


    @ApiOperation(value = "Endpoint for getting a shopping cart", nickname = "getShoppingCart", notes = "Refer to summary", response = UserGetShoppingCartResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserGetShoppingCartResponse.class) })
    @RequestMapping(value = "/user/getShoppingCart",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserGetShoppingCartResponse> getShoppingCart(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserGetShoppingCartRequest body
);


    @ApiOperation(value = "Endpoint for setting a shopping cart", nickname = "loginUser", notes = "Refer to summary", response = UserLoginResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserLoginResponse.class) })
    @RequestMapping(value = "/user/loginUser",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserLoginResponse> loginUser(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserLoginRequest body
);


    @ApiOperation(value = "Endpoint for making a grocery list", nickname = "makeGroceryList", notes = "Refer to summary", response = UserMakeGroceryListResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserMakeGroceryListResponse.class) })
    @RequestMapping(value = "/user/makeGroceryList",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserMakeGroceryListResponse> makeGroceryList(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserMakeGroceryListRequest body
);


    @ApiOperation(value = "Endpoint for setting a registering admin", nickname = "registerAdmin", notes = "Refer to summary", response = UserRegisterAdminResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserRegisterAdminResponse.class) })
    @RequestMapping(value = "/user/registerAdmin",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserRegisterAdminResponse> registerAdmin(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserRegisterAdminRequest body
);


    @ApiOperation(value = "Endpoint for setting a registering customer", nickname = "registerCustomer", notes = "Refer to summary", response = UserRegisterCustomerResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserRegisterCustomerResponse.class) })
    @RequestMapping(value = "/user/registerCustomer",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserRegisterCustomerResponse> registerCustomer(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserRegisterCustomerRequest body
);


    @ApiOperation(value = "Endpoint for setting a registering driver", nickname = "registerDriver", notes = "Refer to summary", response = UserRegisterDriverResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserRegisterDriverResponse.class) })
    @RequestMapping(value = "/user/registerDriver",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserRegisterDriverResponse> registerDriver(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserRegisterDriverRequest body
);


    @ApiOperation(value = "Endpoint for setting a registering shopper", nickname = "registerShopper", notes = "Refer to summary", response = UserRegisterShopperResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserRegisterShopperResponse.class) })
    @RequestMapping(value = "/user/registerShopper",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserRegisterShopperResponse> registerShopper(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserRegisterShopperRequest body
);


    @ApiOperation(value = "Endpoint for reseting password", nickname = "resetPassword", notes = "Refer to summary", response = UserResetPasswordResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserResetPasswordResponse.class) })
    @RequestMapping(value = "/user/resetPassword",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserResetPasswordResponse> resetPassword(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserResetPasswordRequest body
);


    @ApiOperation(value = "Endpoint for scanning an item", nickname = "scanItem", notes = "Refer to summary", response = UserScanItemResponse.class, tags={ "user", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns whether the email was sent", response = UserScanItemResponse.class) })
    @RequestMapping(value = "/user/scanItem",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserScanItemResponse> scanItem(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserScanItemRequest body
);


    @ApiOperation(value = "Endpoint for setting a shopping cart", nickname = "setCart", notes = "Refer to summary", response = UserSetCartResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserSetCartResponse.class) })
    @RequestMapping(value = "/user/setCart",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserSetCartResponse> setCart(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserSetCartRequest body
);


    @ApiOperation(value = "Endpoint for setting Current Location Driver", nickname = "setCurrentLocation", notes = "Refer to summary", response = UserSetCurrentLocationResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserSetCurrentLocationResponse.class) })
    @RequestMapping(value = "/user/setCurrentLocation",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserSetCurrentLocationResponse> setCurrentLocation(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserSetCurrentLocationRequest body
);


    @ApiOperation(value = "Endpoint for updating driver shift", nickname = "updateDriverShift", notes = "Refer to summary", response = UserUpdateDriverShiftResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserUpdateDriverShiftResponse.class) })
    @RequestMapping(value = "/user/updateDriverShift",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserUpdateDriverShiftResponse> updateDriverShift(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserUpdateDriverShiftRequest body
);


    @ApiOperation(value = "Endpoint for updating shopper shift", nickname = "updateShopperShift", notes = "Refer to summary", response = UserUpdateShopperShiftResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserUpdateShopperShiftResponse.class) })
    @RequestMapping(value = "/user/updateShopperShift",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserUpdateShopperShiftResponse> updateShopperShift(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserUpdateShopperShiftRequest body
);


    @ApiOperation(value = "Endpoint for verifying a user account", nickname = "verifyAccount", notes = "Refer to summary", response = UserAccountVerifyResponse.class, tags={ "User", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns upon success", response = UserAccountVerifyResponse.class) })
    @RequestMapping(value = "/user/verifyAccount",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<UserAccountVerifyResponse> verifyAccount(@ApiParam(value = "The input body required by this request" ,required=true )  @Valid @RequestBody UserAccountVerifyRequest body
);

}
