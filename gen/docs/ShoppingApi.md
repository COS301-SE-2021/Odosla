# ShoppingApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addShopper**](ShoppingApi.md#addShopper) | **POST** /shopping/addShopper | Endpoint for adding shopper to shopper list
[**getItems**](ShoppingApi.md#getItems) | **POST** /shopping/getItems | Endpoint for Get Items use case
[**getQueue**](ShoppingApi.md#getQueue) | **POST** /shopping/getQueue | Endpoint for getting the order queue of a shop
[**getShoppers**](ShoppingApi.md#getShoppers) | **POST** /shopping/getShoppers | Endpoint for Get Shoppers use case
[**getStores**](ShoppingApi.md#getStores) | **POST** /shopping/getStores | Endpoint for Get Stores use case
[**populateTables**](ShoppingApi.md#populateTables) | **POST** /shopping/populateTables | Endpoint populating tables
[**removeQueuedOrder**](ShoppingApi.md#removeQueuedOrder) | **POST** /shopping/removeQueuedOrder | Endpoint for removing a queued order.


<a name="addShopper"></a>
# **addShopper**
> ShoppingAddShopperResponse addShopper(shoppingAddShopperRequest)

Endpoint for adding shopper to shopper list

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    ShoppingAddShopperRequest shoppingAddShopperRequest = new ShoppingAddShopperRequest(); // ShoppingAddShopperRequest | The input body required by this request
    try {
      ShoppingAddShopperResponse result = apiInstance.addShopper(shoppingAddShopperRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#addShopper");
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
 **shoppingAddShopperRequest** | [**ShoppingAddShopperRequest**](ShoppingAddShopperRequest.md)| The input body required by this request |

### Return type

[**ShoppingAddShopperResponse**](ShoppingAddShopperResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="getItems"></a>
# **getItems**
> ShoppingGetItemsResponse getItems(shoppingGetItemsRequest)

Endpoint for Get Items use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    ShoppingGetItemsRequest shoppingGetItemsRequest = new ShoppingGetItemsRequest(); // ShoppingGetItemsRequest | The input body required by this request
    try {
      ShoppingGetItemsResponse result = apiInstance.getItems(shoppingGetItemsRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#getItems");
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
 **shoppingGetItemsRequest** | [**ShoppingGetItemsRequest**](ShoppingGetItemsRequest.md)| The input body required by this request |

### Return type

[**ShoppingGetItemsResponse**](ShoppingGetItemsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns items upon success |  -  |

<a name="getQueue"></a>
# **getQueue**
> ShoppingGetQueueResponse getQueue(shoppingGetQueueRequest)

Endpoint for getting the order queue of a shop

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    ShoppingGetQueueRequest shoppingGetQueueRequest = new ShoppingGetQueueRequest(); // ShoppingGetQueueRequest | The input body required by this request
    try {
      ShoppingGetQueueResponse result = apiInstance.getQueue(shoppingGetQueueRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#getQueue");
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
 **shoppingGetQueueRequest** | [**ShoppingGetQueueRequest**](ShoppingGetQueueRequest.md)| The input body required by this request |

### Return type

[**ShoppingGetQueueResponse**](ShoppingGetQueueResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="getShoppers"></a>
# **getShoppers**
> ShoppingGetShoppersResponse getShoppers(shoppingGetShoppersRequest)

Endpoint for Get Shoppers use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    ShoppingGetShoppersRequest shoppingGetShoppersRequest = new ShoppingGetShoppersRequest(); // ShoppingGetShoppersRequest | The input body required by this request
    try {
      ShoppingGetShoppersResponse result = apiInstance.getShoppers(shoppingGetShoppersRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#getShoppers");
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
 **shoppingGetShoppersRequest** | [**ShoppingGetShoppersRequest**](ShoppingGetShoppersRequest.md)| The input body required by this request |

### Return type

[**ShoppingGetShoppersResponse**](ShoppingGetShoppersResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="getStores"></a>
# **getStores**
> ShoppingGetStoresResponse getStores(body)

Endpoint for Get Stores use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    Object body = null; // Object | The input body required by this request
    try {
      ShoppingGetStoresResponse result = apiInstance.getStores(body);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#getStores");
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
 **body** | **Object**| The input body required by this request |

### Return type

[**ShoppingGetStoresResponse**](ShoppingGetStoresResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

<a name="populateTables"></a>
# **populateTables**
> Object populateTables(body)

Endpoint populating tables

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    Object body = null; // Object | The input body required by this request
    try {
      Object result = apiInstance.populateTables(body);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#populateTables");
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
 **body** | **Object**| The input body required by this request |

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns success |  -  |

<a name="removeQueuedOrder"></a>
# **removeQueuedOrder**
> ShoppingRemoveQueuedOrderResponse removeQueuedOrder(shoppingRemoveQueuedOrderRequest)

Endpoint for removing a queued order.

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ShoppingApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    ShoppingApi apiInstance = new ShoppingApi(defaultClient);
    ShoppingRemoveQueuedOrderRequest shoppingRemoveQueuedOrderRequest = new ShoppingRemoveQueuedOrderRequest(); // ShoppingRemoveQueuedOrderRequest | The input body required by this request
    try {
      ShoppingRemoveQueuedOrderResponse result = apiInstance.removeQueuedOrder(shoppingRemoveQueuedOrderRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ShoppingApi#removeQueuedOrder");
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
 **shoppingRemoveQueuedOrderRequest** | [**ShoppingRemoveQueuedOrderRequest**](ShoppingRemoveQueuedOrderRequest.md)| The input body required by this request |

### Return type

[**ShoppingRemoveQueuedOrderResponse**](ShoppingRemoveQueuedOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns upon success |  -  |

