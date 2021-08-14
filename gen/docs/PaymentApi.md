# PaymentApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getItemsPayments**](PaymentApi.md#getItemsPayments) | **POST** /payment/getItems | Endpoint for Get Items use case
[**getStatus**](PaymentApi.md#getStatus) | **POST** /payment/getStatus | Endpoint for Get Status use case
[**submitOrder**](PaymentApi.md#submitOrder) | **POST** /payment/submitOrder | Endpoint for the Submit Order use case
[**updateOrder**](PaymentApi.md#updateOrder) | **POST** /payment/updateOrder | Endpoint for the Update Order use case


<a name="getItemsPayments"></a>
# **getItemsPayments**
> PaymentGetItemsResponse getItemsPayments(paymentGetItemsRequest)

Endpoint for Get Items use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    PaymentApi apiInstance = new PaymentApi(defaultClient);
    PaymentGetItemsRequest paymentGetItemsRequest = new PaymentGetItemsRequest(); // PaymentGetItemsRequest | The input body required by this request
    try {
      PaymentGetItemsResponse result = apiInstance.getItemsPayments(paymentGetItemsRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentApi#getItemsPayments");
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
 **paymentGetItemsRequest** | [**PaymentGetItemsRequest**](PaymentGetItemsRequest.md)| The input body required by this request |

### Return type

[**PaymentGetItemsResponse**](PaymentGetItemsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns status upon success |  -  |

<a name="getStatus"></a>
# **getStatus**
> PaymentGetStatusResponse getStatus(paymentGetStatusRequest)

Endpoint for Get Status use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    PaymentApi apiInstance = new PaymentApi(defaultClient);
    PaymentGetStatusRequest paymentGetStatusRequest = new PaymentGetStatusRequest(); // PaymentGetStatusRequest | The input body required by this request
    try {
      PaymentGetStatusResponse result = apiInstance.getStatus(paymentGetStatusRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentApi#getStatus");
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
 **paymentGetStatusRequest** | [**PaymentGetStatusRequest**](PaymentGetStatusRequest.md)| The input body required by this request |

### Return type

[**PaymentGetStatusResponse**](PaymentGetStatusResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns status upon success |  -  |

<a name="submitOrder"></a>
# **submitOrder**
> PaymentSubmitOrderResponse submitOrder(paymentSubmitOrderRequest)

Endpoint for the Submit Order use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    PaymentApi apiInstance = new PaymentApi(defaultClient);
    PaymentSubmitOrderRequest paymentSubmitOrderRequest = new PaymentSubmitOrderRequest(); // PaymentSubmitOrderRequest | The input body required by this request
    try {
      PaymentSubmitOrderResponse result = apiInstance.submitOrder(paymentSubmitOrderRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentApi#submitOrder");
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
 **paymentSubmitOrderRequest** | [**PaymentSubmitOrderRequest**](PaymentSubmitOrderRequest.md)| The input body required by this request |

### Return type

[**PaymentSubmitOrderResponse**](PaymentSubmitOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns submitted order upon success |  -  |

<a name="updateOrder"></a>
# **updateOrder**
> PaymentUpdateOrderResponse updateOrder(paymentUpdateOrderRequest)

Endpoint for the Update Order use case

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.PaymentApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    PaymentApi apiInstance = new PaymentApi(defaultClient);
    PaymentUpdateOrderRequest paymentUpdateOrderRequest = new PaymentUpdateOrderRequest(); // PaymentUpdateOrderRequest | The input body required by this request
    try {
      PaymentUpdateOrderResponse result = apiInstance.updateOrder(paymentUpdateOrderRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling PaymentApi#updateOrder");
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
 **paymentUpdateOrderRequest** | [**PaymentUpdateOrderRequest**](PaymentUpdateOrderRequest.md)| The input body required by this request |

### Return type

[**PaymentUpdateOrderResponse**](PaymentUpdateOrderResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns updated order upon success |  -  |

