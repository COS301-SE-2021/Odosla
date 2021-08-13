/*
 * Library Service
 * The library service
 *
 * The version of the OpenAPI document: 0.0.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.UserAccountVerifyRequest;
import org.openapitools.client.model.UserAccountVerifyResponse;
import org.openapitools.client.model.UserClearShoppingCartRequest;
import org.openapitools.client.model.UserClearShoppingCartResponse;
import org.openapitools.client.model.UserGetCurrentUserRequest;
import org.openapitools.client.model.UserGetCurrentUserResponse;
import org.openapitools.client.model.UserGetShoppingCartRequest;
import org.openapitools.client.model.UserGetShoppingCartResponse;
import org.openapitools.client.model.UserLoginRequest;
import org.openapitools.client.model.UserLoginResponse;
import org.openapitools.client.model.UserMakeGroceryListRequest;
import org.openapitools.client.model.UserMakeGroceryListResponse;
import org.openapitools.client.model.UserRegisterAdminRequest;
import org.openapitools.client.model.UserRegisterAdminResponse;
import org.openapitools.client.model.UserRegisterCustomerRequest;
import org.openapitools.client.model.UserRegisterCustomerResponse;
import org.openapitools.client.model.UserRegisterDriverRequest;
import org.openapitools.client.model.UserRegisterDriverResponse;
import org.openapitools.client.model.UserRegisterShopperRequest;
import org.openapitools.client.model.UserRegisterShopperResponse;
import org.openapitools.client.model.UserResetPasswordRequest;
import org.openapitools.client.model.UserResetPasswordResponse;
import org.openapitools.client.model.UserSetCartRequest;
import org.openapitools.client.model.UserSetCartResponse;
import org.openapitools.client.model.UserSetCurrentLocationRequest;
import org.openapitools.client.model.UserSetCurrentLocationResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UserApi
 */
@Ignore
public class UserApiTest {

    private final UserApi api = new UserApi();

    
    /**
     * Endpoint for clearing a shopping cart
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void clearShoppingCartTest() throws ApiException {
        UserClearShoppingCartRequest userClearShoppingCartRequest = null;
        UserClearShoppingCartResponse response = api.clearShoppingCart(userClearShoppingCartRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for to get Current user
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getCurrentUserTest() throws ApiException {
        UserGetCurrentUserRequest userGetCurrentUserRequest = null;
        UserGetCurrentUserResponse response = api.getCurrentUser(userGetCurrentUserRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for getting a shopping cart
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getShoppingCartTest() throws ApiException {
        UserGetShoppingCartRequest userGetShoppingCartRequest = null;
        UserGetShoppingCartResponse response = api.getShoppingCart(userGetShoppingCartRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting a shopping cart
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void loginUserTest() throws ApiException {
        UserLoginRequest userLoginRequest = null;
        UserLoginResponse response = api.loginUser(userLoginRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for making a grocery list
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void makeGroceryListTest() throws ApiException {
        UserMakeGroceryListRequest userMakeGroceryListRequest = null;
        UserMakeGroceryListResponse response = api.makeGroceryList(userMakeGroceryListRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting a registering admin
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void registerAdminTest() throws ApiException {
        UserRegisterAdminRequest userRegisterAdminRequest = null;
        UserRegisterAdminResponse response = api.registerAdmin(userRegisterAdminRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting a registering customer
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void registerCustomerTest() throws ApiException {
        UserRegisterCustomerRequest userRegisterCustomerRequest = null;
        UserRegisterCustomerResponse response = api.registerCustomer(userRegisterCustomerRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting a registering driver
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void registerDriverTest() throws ApiException {
        UserRegisterDriverRequest userRegisterDriverRequest = null;
        UserRegisterDriverResponse response = api.registerDriver(userRegisterDriverRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting a registering shopper
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void registerShopperTest() throws ApiException {
        UserRegisterShopperRequest userRegisterShopperRequest = null;
        UserRegisterShopperResponse response = api.registerShopper(userRegisterShopperRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for reseting password
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void resetPasswordTest() throws ApiException {
        UserResetPasswordRequest userResetPasswordRequest = null;
        UserResetPasswordResponse response = api.resetPassword(userResetPasswordRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting a shopping cart
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void setCartTest() throws ApiException {
        UserSetCartRequest userSetCartRequest = null;
        UserSetCartResponse response = api.setCart(userSetCartRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for setting Current Location Driver
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void setCurrentLocationTest() throws ApiException {
        UserSetCurrentLocationRequest userSetCurrentLocationRequest = null;
        UserSetCurrentLocationResponse response = api.setCurrentLocation(userSetCurrentLocationRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for verifying a user account
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void verifyAccountTest() throws ApiException {
        UserAccountVerifyRequest userAccountVerifyRequest = null;
        UserAccountVerifyResponse response = api.verifyAccount(userAccountVerifyRequest);

        // TODO: test validations
    }
    
}
