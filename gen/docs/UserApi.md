# UserApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**clearShoppingCart**](UserApi.md#clearShoppingCart) | **POST** /user/clearShoppingCart | Endpoint for clearing a shopping cart
[**completeDelivery**](UserApi.md#completeDelivery) | **POST** /user/completeDelivery | Endpoint for complete delivery use case
[**completePackagingOrder**](UserApi.md#completePackagingOrder) | **POST** /user/completePackagingOrder | Endpoint for complete packaging order use case
[**driverSetRating**](UserApi.md#driverSetRating) | **POST** /user/driverSetRating | Endpoint for setting rating use case
[**getCurrentUser**](UserApi.md#getCurrentUser) | **POST** /user/getCurrentUser | Endpoint for to get Current user
[**getCustomerByUUID**](UserApi.md#getCustomerByUUID) | **POST** /user/getCustomerByUUID | Endpoint for get customer by UUID use case
[**getDeliveryDriverByOrderId**](UserApi.md#getDeliveryDriverByOrderId) | **POST** /delivery/getDeliveryDriverByOrderId | Endpoint for get delivery driver by order id use case
[**getGroceryLists**](UserApi.md#getGroceryLists) | **POST** /user/getGroceryLists | Endpoint for get grocery lists use case
[**getNextQueued**](UserApi.md#getNextQueued) | **POST** /shopping/getNextQueued | Endpoint for getting next queue
[**getShoppingCart**](UserApi.md#getShoppingCart) | **POST** /user/getShoppingCart | Endpoint for getting a shopping cart
[**loginUser**](UserApi.md#loginUser) | **POST** /user/loginUser | Endpoint for setting a shopping cart
[**makeGroceryList**](UserApi.md#makeGroceryList) | **POST** /user/makeGroceryList | Endpoint for making a grocery list
[**registerAdmin**](UserApi.md#registerAdmin) | **POST** /user/registerAdmin | Endpoint for setting a registering admin
[**registerCustomer**](UserApi.md#registerCustomer) | **POST** /user/registerCustomer | Endpoint for setting a registering customer
[**registerDriver**](UserApi.md#registerDriver) | **POST** /user/registerDriver | Endpoint for setting a registering driver
[**registerShopper**](UserApi.md#registerShopper) | **POST** /user/registerShopper | Endpoint for setting a registering shopper
[**resetPassword**](UserApi.md#resetPassword) | **POST** /user/resetPassword | Endpoint for reseting password
[**scanItem**](UserApi.md#scanItem) | **POST** /user/scanItem | Endpoint for scanning an item
[**setCart**](UserApi.md#setCart) | **POST** /user/setCart | Endpoint for setting a shopping cart
[**setCurrentLocation**](UserApi.md#setCurrentLocation) | **POST** /user/setCurrentLocation | Endpoint for setting Current Location Driver
[**updateDriverShift**](UserApi.md#updateDriverShift) | **POST** /user/updateDriverShift | Endpoint for updating driver shift
[**updateShopperDetails**](UserApi.md#updateShopperDetails) | **POST** /user/updateShopperDetails | Endpoint for updating shopper details
[**updateShopperShift**](UserApi.md#updateShopperShift) | **POST** /user/updateShopperShift | Endpoint for updating shopper shift
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

<a name="completeDelivery"></a>
# **completeDelivery**
> UserCompleteDeliveryResponse completeDelivery(userCompleteDeliveryRequest)

Endpoint for complete delivery use case

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
    UserCompleteDeliveryRequest userCompleteDeliveryRequest = new UserCompleteDeliveryRequest(); // UserCompleteDeliveryRequest | The input body required by this request
    try {
      UserCompleteDeliveryResponse result = apiInstance.completeDelivery(userCompleteDeliveryRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#completeDelivery");
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
 **userCompleteDeliveryRequest** | [**UserCompleteDeliveryRequest**](UserCompleteDeliveryRequest.md)| The input body required by this request |

### Return type

[**UserCompleteDeliveryResponse**](UserCompleteDeliveryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns delivery is completed |  -  |

<a name="completePackagingOrder"></a>
# **completePackagingOrder**
> UserCompletePackagingOrderResponse completePackagingOrder(userCompletePackagingOrderRequest)

Endpoint for complete packaging order use case

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
    UserCompletePackagingOrderRequest userCompletePackagingOrderRequest = new UserCompletePackagingOrderRequest(); // UserCompletePackagingOrderRequest | The input body required by this request
    try {
      UserCompletePackagingOrderResponse result = apiInstance.completePackagingOrder(userCompletePackagingOrderRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#completePackagingOrder");
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
 **userCompletePackagingOrderRequest** | [**UserCompletePackagingOrderRequest**](UserCompletePackagingOrderRequest.md)| The input body required by this request |

### Return type

[**UserCompletePackagingOrderResponse**](UserCompletePackagingOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the email was sent |  -  |

<a name="driverSetRating"></a>
# **driverSetRating**
> UserDriverSetRatingResponse driverSetRating(userDriverSetRatingRequest)

Endpoint for setting rating use case

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
    UserDriverSetRatingRequest userDriverSetRatingRequest = new UserDriverSetRatingRequest(); // UserDriverSetRatingRequest | The input body required by this request
    try {
      UserDriverSetRatingResponse result = apiInstance.driverSetRating(userDriverSetRatingRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#driverSetRating");
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
 **userDriverSetRatingRequest** | [**UserDriverSetRatingRequest**](UserDriverSetRatingRequest.md)| The input body required by this request |

### Return type

[**UserDriverSetRatingResponse**](UserDriverSetRatingResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns response |  -  |

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

<a name="getCustomerByUUID"></a>
# **getCustomerByUUID**
> UserGetCustomerByUUIDResponse getCustomerByUUID(userGetCustomerByUUIDRequest)

Endpoint for get customer by UUID use case

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
    UserGetCustomerByUUIDRequest userGetCustomerByUUIDRequest = new UserGetCustomerByUUIDRequest(); // UserGetCustomerByUUIDRequest | The input body required by this request
    try {
      UserGetCustomerByUUIDResponse result = apiInstance.getCustomerByUUID(userGetCustomerByUUIDRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getCustomerByUUID");
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
 **userGetCustomerByUUIDRequest** | [**UserGetCustomerByUUIDRequest**](UserGetCustomerByUUIDRequest.md)| The input body required by this request |

### Return type

[**UserGetCustomerByUUIDResponse**](UserGetCustomerByUUIDResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns customer |  -  |

<a name="getDeliveryDriverByOrderId"></a>
# **getDeliveryDriverByOrderId**
> DeliveryGetDeliveryDriverByOrderIdResponse getDeliveryDriverByOrderId(deliveryGetDeliveryDriverByOrderIdRequest)

Endpoint for get delivery driver by order id use case

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
    DeliveryGetDeliveryDriverByOrderIdRequest deliveryGetDeliveryDriverByOrderIdRequest = new DeliveryGetDeliveryDriverByOrderIdRequest(); // DeliveryGetDeliveryDriverByOrderIdRequest | The input body required by this request
    try {
      DeliveryGetDeliveryDriverByOrderIdResponse result = apiInstance.getDeliveryDriverByOrderId(deliveryGetDeliveryDriverByOrderIdRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getDeliveryDriverByOrderId");
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
 **deliveryGetDeliveryDriverByOrderIdRequest** | [**DeliveryGetDeliveryDriverByOrderIdRequest**](DeliveryGetDeliveryDriverByOrderIdRequest.md)| The input body required by this request |

### Return type

[**DeliveryGetDeliveryDriverByOrderIdResponse**](DeliveryGetDeliveryDriverByOrderIdResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns delivery and driver |  -  |

<a name="getGroceryLists"></a>
# **getGroceryLists**
> UserGetGroceryListResponse getGroceryLists(userGetGroceryListRequest)

Endpoint for get grocery lists use case

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
    UserGetGroceryListRequest userGetGroceryListRequest = new UserGetGroceryListRequest(); // UserGetGroceryListRequest | The input body required by this request
    try {
      UserGetGroceryListResponse result = apiInstance.getGroceryLists(userGetGroceryListRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getGroceryLists");
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
 **userGetGroceryListRequest** | [**UserGetGroceryListRequest**](UserGetGroceryListRequest.md)| The input body required by this request |

### Return type

[**UserGetGroceryListResponse**](UserGetGroceryListResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns the groceryList |  -  |

<a name="getNextQueued"></a>
# **getNextQueued**
> ShoppingGetNextQueuedResponse getNextQueued(shoppingGetNextQueuedRequest)

Endpoint for getting next queue

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
    ShoppingGetNextQueuedRequest shoppingGetNextQueuedRequest = new ShoppingGetNextQueuedRequest(); // ShoppingGetNextQueuedRequest | The input body required by this request
    try {
      ShoppingGetNextQueuedResponse result = apiInstance.getNextQueued(shoppingGetNextQueuedRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#getNextQueued");
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
 **shoppingGetNextQueuedRequest** | [**ShoppingGetNextQueuedRequest**](ShoppingGetNextQueuedRequest.md)| The input body required by this request |

### Return type

[**ShoppingGetNextQueuedResponse**](ShoppingGetNextQueuedResponse.md)

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

<a name="scanItem"></a>
# **scanItem**
> UserScanItemResponse scanItem(userScanItemRequest)

Endpoint for scanning an item

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
    UserScanItemRequest userScanItemRequest = new UserScanItemRequest(); // UserScanItemRequest | The input body required by this request
    try {
      UserScanItemResponse result = apiInstance.scanItem(userScanItemRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#scanItem");
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
 **userScanItemRequest** | [**UserScanItemRequest**](UserScanItemRequest.md)| The input body required by this request |

### Return type

[**UserScanItemResponse**](UserScanItemResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the email was sent |  -  |

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

<a name="updateDriverShift"></a>
# **updateDriverShift**
> UserUpdateDriverShiftResponse updateDriverShift(userUpdateDriverShiftRequest)

Endpoint for updating driver shift

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
    UserUpdateDriverShiftRequest userUpdateDriverShiftRequest = new UserUpdateDriverShiftRequest(); // UserUpdateDriverShiftRequest | The input body required by this request
    try {
      UserUpdateDriverShiftResponse result = apiInstance.updateDriverShift(userUpdateDriverShiftRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#updateDriverShift");
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
 **userUpdateDriverShiftRequest** | [**UserUpdateDriverShiftRequest**](UserUpdateDriverShiftRequest.md)| The input body required by this request |

### Return type

[**UserUpdateDriverShiftResponse**](UserUpdateDriverShiftResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="updateShopperDetails"></a>
# **updateShopperDetails**
> UserUpdateShopperDetailsResponse updateShopperDetails(userUpdateShopperDetailsRequest)

Endpoint for updating shopper details

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
    UserUpdateShopperDetailsRequest userUpdateShopperDetailsRequest = new UserUpdateShopperDetailsRequest(); // UserUpdateShopperDetailsRequest | The input body require by this request
    try {
      UserUpdateShopperDetailsResponse result = apiInstance.updateShopperDetails(userUpdateShopperDetailsRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#updateShopperDetails");
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
 **userUpdateShopperDetailsRequest** | [**UserUpdateShopperDetailsRequest**](UserUpdateShopperDetailsRequest.md)| The input body require by this request |

### Return type

[**UserUpdateShopperDetailsResponse**](UserUpdateShopperDetailsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns if updating details was successful |  -  |

<a name="updateShopperShift"></a>
# **updateShopperShift**
> UserUpdateShopperShiftResponse updateShopperShift(userUpdateShopperShiftRequest)

Endpoint for updating shopper shift

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
    UserUpdateShopperShiftRequest userUpdateShopperShiftRequest = new UserUpdateShopperShiftRequest(); // UserUpdateShopperShiftRequest | The input body required by this request
    try {
      UserUpdateShopperShiftResponse result = apiInstance.updateShopperShift(userUpdateShopperShiftRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling UserApi#updateShopperShift");
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
 **userUpdateShopperShiftRequest** | [**UserUpdateShopperShiftRequest**](UserUpdateShopperShiftRequest.md)| The input body required by this request |

### Return type

[**UserUpdateShopperShiftResponse**](UserUpdateShopperShiftResponse.md)

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

