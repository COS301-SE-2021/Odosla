# PaymentApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**updateOrder**](PaymentApi.md#updateOrder) | **POST** /payment/updateOrder | Endpoint for the Update Order use case


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

