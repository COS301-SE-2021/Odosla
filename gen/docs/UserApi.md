# UserApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**clearShoppingCart**](UserApi.md#clearShoppingCart) | **POST** /user/clearShoppingCart | Endpoint for clearing a shopping cart
[**getCurrentUser**](UserApi.md#getCurrentUser) | **POST** /user/getCurrentUser | Endpoint for to get Current user
[**getShoppingCart**](UserApi.md#getShoppingCart) | **POST** /user/getShoppingCart | Endpoint for getting a shopping cart
[**loginUser**](UserApi.md#loginUser) | **POST** /user/loginUser | Endpoint for setting a shopping cart
[**makeGroceryList**](UserApi.md#makeGroceryList) | **POST** /user/makeGroceryList | Endpoint for making a grocery list
[**registerAdmin**](UserApi.md#registerAdmin) | **POST** /user/registerAdmin | Endpoint for setting a registering admin
[**registerCustomer**](UserApi.md#registerCustomer) | **POST** /user/registerCustomer | Endpoint for setting a registering customer
[**registerDriver**](UserApi.md#registerDriver) | **POST** /user/registerDriver | Endpoint for setting a registering driver
[**registerShopper**](UserApi.md#registerShopper) | **POST** /user/registerShopper | Endpoint for setting a registering shopper
[**resetPassword**](UserApi.md#resetPassword) | **POST** /user/resetPassword | Endpoint for reseting password
[**setCart**](UserApi.md#setCart) | **POST** /user/setCart | Endpoint for setting a shopping cart
[**setCurrentLocation**](UserApi.md#setCurrentLocation) | **POST** /user/setCurrentLocation | Endpoint for setting Current Location Driver
[**verifyAccount**](UserApi.md#verifyAccount) | **POST** /user/verifyAccount | Endpoint for verifying a user account


<a name="clearShoppingCart"></a>
# **clearShoppingCart**
> UserClearShoppingCartResponse clearShoppingCart(userClearShoppingCartRequest)

Endpoint for clearing a shopping cart

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserClearShoppingCartRequest userClearShoppingCartRequest = new UserClearShoppingCartRequest(); // UserClearShoppingCartRequest | The input body required by this request
    try {
      UserClearShoppingCartResponse result = apiInstance.clearShoppingCart(userClearShoppingCartRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#clearShoppingCart");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userClearShoppingCartRequest** | [**UserClearShoppingCartRequest**](UserClearShoppingCartRequest.md)| The input body required by this request |

### Return type

[**UserClearShoppingCartResponse**](UserClearShoppingCartResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="getCurrentUser"></a>
# **getCurrentUser**
> UserGetCurrentUserResponse getCurrentUser(userGetCurrentUserRequest)

Endpoint for to get Current user

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserGetCurrentUserRequest userGetCurrentUserRequest = new UserGetCurrentUserRequest(); // UserGetCurrentUserRequest | The input body required by this request
    try {
      UserGetCurrentUserResponse result = apiInstance.getCurrentUser(userGetCurrentUserRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getCurrentUser");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userGetCurrentUserRequest** | [**UserGetCurrentUserRequest**](UserGetCurrentUserRequest.md)| The input body required by this request |

### Return type

[**UserGetCurrentUserResponse**](UserGetCurrentUserResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="getShoppingCart"></a>
# **getShoppingCart**
> UserGetShoppingCartResponse getShoppingCart(userGetShoppingCartRequest)

Endpoint for getting a shopping cart

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserGetShoppingCartRequest userGetShoppingCartRequest = new UserGetShoppingCartRequest(); // UserGetShoppingCartRequest | The input body required by this request
    try {
      UserGetShoppingCartResponse result = apiInstance.getShoppingCart(userGetShoppingCartRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getShoppingCart");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userGetShoppingCartRequest** | [**UserGetShoppingCartRequest**](UserGetShoppingCartRequest.md)| The input body required by this request |

### Return type

[**UserGetShoppingCartResponse**](UserGetShoppingCartResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="loginUser"></a>
# **loginUser**
> UserLoginResponse loginUser(userLoginRequest)

Endpoint for setting a shopping cart

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserLoginRequest userLoginRequest = new UserLoginRequest(); // UserLoginRequest | The input body required by this request
    try {
      UserLoginResponse result = apiInstance.loginUser(userLoginRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#loginUser");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userLoginRequest** | [**UserLoginRequest**](UserLoginRequest.md)| The input body required by this request |

### Return type

[**UserLoginResponse**](UserLoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="makeGroceryList"></a>
# **makeGroceryList**
> UserMakeGroceryListResponse makeGroceryList(userMakeGroceryListRequest)

Endpoint for making a grocery list

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserMakeGroceryListRequest userMakeGroceryListRequest = new UserMakeGroceryListRequest(); // UserMakeGroceryListRequest | The input body required by this request
    try {
      UserMakeGroceryListResponse result = apiInstance.makeGroceryList(userMakeGroceryListRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#makeGroceryList");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userMakeGroceryListRequest** | [**UserMakeGroceryListRequest**](UserMakeGroceryListRequest.md)| The input body required by this request |

### Return type

[**UserMakeGroceryListResponse**](UserMakeGroceryListResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="registerAdmin"></a>
# **registerAdmin**
> UserRegisterAdminResponse registerAdmin(userRegisterAdminRequest)

Endpoint for setting a registering admin

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserRegisterAdminRequest userRegisterAdminRequest = new UserRegisterAdminRequest(); // UserRegisterAdminRequest | The input body required by this request
    try {
      UserRegisterAdminResponse result = apiInstance.registerAdmin(userRegisterAdminRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#registerAdmin");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userRegisterAdminRequest** | [**UserRegisterAdminRequest**](UserRegisterAdminRequest.md)| The input body required by this request |

### Return type

[**UserRegisterAdminResponse**](UserRegisterAdminResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="registerCustomer"></a>
# **registerCustomer**
> UserRegisterCustomerResponse registerCustomer(userRegisterCustomerRequest)

Endpoint for setting a registering customer

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserRegisterCustomerRequest userRegisterCustomerRequest = new UserRegisterCustomerRequest(); // UserRegisterCustomerRequest | The input body required by this request
    try {
      UserRegisterCustomerResponse result = apiInstance.registerCustomer(userRegisterCustomerRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#registerCustomer");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userRegisterCustomerRequest** | [**UserRegisterCustomerRequest**](UserRegisterCustomerRequest.md)| The input body required by this request |

### Return type

[**UserRegisterCustomerResponse**](UserRegisterCustomerResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="registerDriver"></a>
# **registerDriver**
> UserRegisterDriverResponse registerDriver(userRegisterDriverRequest)

Endpoint for setting a registering driver

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserRegisterDriverRequest userRegisterDriverRequest = new UserRegisterDriverRequest(); // UserRegisterDriverRequest | The input body required by this request
    try {
      UserRegisterDriverResponse result = apiInstance.registerDriver(userRegisterDriverRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#registerDriver");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userRegisterDriverRequest** | [**UserRegisterDriverRequest**](UserRegisterDriverRequest.md)| The input body required by this request |

### Return type

[**UserRegisterDriverResponse**](UserRegisterDriverResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="registerShopper"></a>
# **registerShopper**
> UserRegisterShopperResponse registerShopper(userRegisterShopperRequest)

Endpoint for setting a registering shopper

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserRegisterShopperRequest userRegisterShopperRequest = new UserRegisterShopperRequest(); // UserRegisterShopperRequest | The input body required by this request
    try {
      UserRegisterShopperResponse result = apiInstance.registerShopper(userRegisterShopperRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#registerShopper");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userRegisterShopperRequest** | [**UserRegisterShopperRequest**](UserRegisterShopperRequest.md)| The input body required by this request |

### Return type

[**UserRegisterShopperResponse**](UserRegisterShopperResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="resetPassword"></a>
# **resetPassword**
> UserResetPasswordResponse resetPassword(userResetPasswordRequest)

Endpoint for reseting password

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserResetPasswordRequest userResetPasswordRequest = new UserResetPasswordRequest(); // UserResetPasswordRequest | The input body required by this request
    try {
      UserResetPasswordResponse result = apiInstance.resetPassword(userResetPasswordRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#resetPassword");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userResetPasswordRequest** | [**UserResetPasswordRequest**](UserResetPasswordRequest.md)| The input body required by this request |

### Return type

[**UserResetPasswordResponse**](UserResetPasswordResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="setCart"></a>
# **setCart**
> UserSetCartResponse setCart(userSetCartRequest)

Endpoint for setting a shopping cart

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserSetCartRequest userSetCartRequest = new UserSetCartRequest(); // UserSetCartRequest | The input body required by this request
    try {
      UserSetCartResponse result = apiInstance.setCart(userSetCartRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#setCart");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userSetCartRequest** | [**UserSetCartRequest**](UserSetCartRequest.md)| The input body required by this request |

### Return type

[**UserSetCartResponse**](UserSetCartResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="setCurrentLocation"></a>
# **setCurrentLocation**
> UserSetCurrentLocationResponse setCurrentLocation(userSetCurrentLocationRequest)

Endpoint for setting Current Location Driver

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserSetCurrentLocationRequest userSetCurrentLocationRequest = new UserSetCurrentLocationRequest(); // UserSetCurrentLocationRequest | The input body required by this request
    try {
      UserSetCurrentLocationResponse result = apiInstance.setCurrentLocation(userSetCurrentLocationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#setCurrentLocation");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userSetCurrentLocationRequest** | [**UserSetCurrentLocationRequest**](UserSetCurrentLocationRequest.md)| The input body required by this request |

### Return type

[**UserSetCurrentLocationResponse**](UserSetCurrentLocationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="verifyAccount"></a>
# **verifyAccount**
> UserAccountVerifyResponse verifyAccount(userAccountVerifyRequest)

Endpoint for verifying a user account

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.UserApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    UserApi apiInstance = new UserApi(defaultClient);
    UserAccountVerifyRequest userAccountVerifyRequest = new UserAccountVerifyRequest(); // UserAccountVerifyRequest | The input body required by this request
    try {
      UserAccountVerifyResponse result = apiInstance.verifyAccount(userAccountVerifyRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#verifyAccount");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userAccountVerifyRequest** | [**UserAccountVerifyRequest**](UserAccountVerifyRequest.md)| The input body required by this request |

### Return type

[**UserAccountVerifyResponse**](UserAccountVerifyResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

