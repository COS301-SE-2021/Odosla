# DeliveryApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addDeliveryDetail**](DeliveryApi.md#addDeliveryDetail) | **POST** /delivery/addDeliveryDetail | Endpoint for add delivery detail
[**assignDriverToDelivery**](DeliveryApi.md#assignDriverToDelivery) | **POST** /delivery/assignDriverToDelivery | Endpoint for assign driver to delivery
[**createDelivery**](DeliveryApi.md#createDelivery) | **POST** /delivery/createDelivery | Endpoint for creating a delivery
[**getDeliveryDetail**](DeliveryApi.md#getDeliveryDetail) | **POST** /delivery/getDeliveryDetail | Endpoint for getting the detail of a delivery
[**getDeliveryStatus**](DeliveryApi.md#getDeliveryStatus) | **POST** /delivery/getDeliveryStatus | Endpoint for getting the status of a delivery
[**getNextOrderForDriver**](DeliveryApi.md#getNextOrderForDriver) | **POST** /delivery/getNextOrderForDriver | Endpoint for getting the next order for a driver
[**removeDriverFromDelivery**](DeliveryApi.md#removeDriverFromDelivery) | **POST** /delivery/removeDriverFromDelivery | Endpoint for removing a driver from a delivery
[**trackDelivery**](DeliveryApi.md#trackDelivery) | **POST** /delivery/trackDelivery | Endpoint for tracking a delivery
[**updateDeliveryStatus**](DeliveryApi.md#updateDeliveryStatus) | **POST** /delivery/updateDeliveryStatus | Endpoint for updating the status of a delivery


<a name="addDeliveryDetail"></a>
# **addDeliveryDetail**
> DeliveryAddDeliveryDetailResponse addDeliveryDetail(deliveryAddDeliveryDetailRequest)

Endpoint for add delivery detail

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryAddDeliveryDetailRequest deliveryAddDeliveryDetailRequest = new DeliveryAddDeliveryDetailRequest(); // DeliveryAddDeliveryDetailRequest | The input body required by this request
    try {
      DeliveryAddDeliveryDetailResponse result = apiInstance.addDeliveryDetail(deliveryAddDeliveryDetailRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#addDeliveryDetail");
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
 **deliveryAddDeliveryDetailRequest** | [**DeliveryAddDeliveryDetailRequest**](DeliveryAddDeliveryDetailRequest.md)| The input body required by this request |

### Return type

[**DeliveryAddDeliveryDetailResponse**](DeliveryAddDeliveryDetailResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the detail was added and the ID of that detail |  -  |

<a name="assignDriverToDelivery"></a>
# **assignDriverToDelivery**
> DeliveryAssignDriverToDeliveryResponse assignDriverToDelivery(deliveryAssignDriverToDeliveryRequest)

Endpoint for assign driver to delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryAssignDriverToDeliveryRequest deliveryAssignDriverToDeliveryRequest = new DeliveryAssignDriverToDeliveryRequest(); // DeliveryAssignDriverToDeliveryRequest | The input body required by this request
    try {
      DeliveryAssignDriverToDeliveryResponse result = apiInstance.assignDriverToDelivery(deliveryAssignDriverToDeliveryRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#assignDriverToDelivery");
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
 **deliveryAssignDriverToDeliveryRequest** | [**DeliveryAssignDriverToDeliveryRequest**](DeliveryAssignDriverToDeliveryRequest.md)| The input body required by this request |

### Return type

[**DeliveryAssignDriverToDeliveryResponse**](DeliveryAssignDriverToDeliveryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the driver was assigned to the delivery |  -  |

<a name="createDelivery"></a>
# **createDelivery**
> DeliveryCreateDeliveryResponse createDelivery(deliveryCreateDeliveryRequest)

Endpoint for creating a delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryCreateDeliveryRequest deliveryCreateDeliveryRequest = new DeliveryCreateDeliveryRequest(); // DeliveryCreateDeliveryRequest | The input body required by this request
    try {
      DeliveryCreateDeliveryResponse result = apiInstance.createDelivery(deliveryCreateDeliveryRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#createDelivery");
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
 **deliveryCreateDeliveryRequest** | [**DeliveryCreateDeliveryRequest**](DeliveryCreateDeliveryRequest.md)| The input body required by this request |

### Return type

[**DeliveryCreateDeliveryResponse**](DeliveryCreateDeliveryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the delivery was created or not |  -  |

<a name="getDeliveryDetail"></a>
# **getDeliveryDetail**
> DeliveryGetDeliveryDetailResponse getDeliveryDetail(deliveryGetDeliveryDetailRequest)

Endpoint for getting the detail of a delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryGetDeliveryDetailRequest deliveryGetDeliveryDetailRequest = new DeliveryGetDeliveryDetailRequest(); // DeliveryGetDeliveryDetailRequest | The input body required by this request
    try {
      DeliveryGetDeliveryDetailResponse result = apiInstance.getDeliveryDetail(deliveryGetDeliveryDetailRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#getDeliveryDetail");
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
 **deliveryGetDeliveryDetailRequest** | [**DeliveryGetDeliveryDetailRequest**](DeliveryGetDeliveryDetailRequest.md)| The input body required by this request |

### Return type

[**DeliveryGetDeliveryDetailResponse**](DeliveryGetDeliveryDetailResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns the delivery detail |  -  |

<a name="getDeliveryStatus"></a>
# **getDeliveryStatus**
> DeliveryGetDeliveryStatusResponse getDeliveryStatus(deliveryGetDeliveryStatusRequest)

Endpoint for getting the status of a delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryGetDeliveryStatusRequest deliveryGetDeliveryStatusRequest = new DeliveryGetDeliveryStatusRequest(); // DeliveryGetDeliveryStatusRequest | The input body required by this request
    try {
      DeliveryGetDeliveryStatusResponse result = apiInstance.getDeliveryStatus(deliveryGetDeliveryStatusRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#getDeliveryStatus");
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
 **deliveryGetDeliveryStatusRequest** | [**DeliveryGetDeliveryStatusRequest**](DeliveryGetDeliveryStatusRequest.md)| The input body required by this request |

### Return type

[**DeliveryGetDeliveryStatusResponse**](DeliveryGetDeliveryStatusResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns the delivery status |  -  |

<a name="getNextOrderForDriver"></a>
# **getNextOrderForDriver**
> DeliveryGetNextOrderForDriverResponse getNextOrderForDriver(deliveryGetNextOrderForDriverRequest)

Endpoint for getting the next order for a driver

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryGetNextOrderForDriverRequest deliveryGetNextOrderForDriverRequest = new DeliveryGetNextOrderForDriverRequest(); // DeliveryGetNextOrderForDriverRequest | The input body required by this request
    try {
      DeliveryGetNextOrderForDriverResponse result = apiInstance.getNextOrderForDriver(deliveryGetNextOrderForDriverRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#getNextOrderForDriver");
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
 **deliveryGetNextOrderForDriverRequest** | [**DeliveryGetNextOrderForDriverRequest**](DeliveryGetNextOrderForDriverRequest.md)| The input body required by this request |

### Return type

[**DeliveryGetNextOrderForDriverResponse**](DeliveryGetNextOrderForDriverResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns the next order for a driver |  -  |

<a name="removeDriverFromDelivery"></a>
# **removeDriverFromDelivery**
> DeliveryRemoveDriverFromDeliveryResponse removeDriverFromDelivery(deliveryRemoveDriverFromDeliveryRequest)

Endpoint for removing a driver from a delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryRemoveDriverFromDeliveryRequest deliveryRemoveDriverFromDeliveryRequest = new DeliveryRemoveDriverFromDeliveryRequest(); // DeliveryRemoveDriverFromDeliveryRequest | The input body required by this request
    try {
      DeliveryRemoveDriverFromDeliveryResponse result = apiInstance.removeDriverFromDelivery(deliveryRemoveDriverFromDeliveryRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#removeDriverFromDelivery");
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
 **deliveryRemoveDriverFromDeliveryRequest** | [**DeliveryRemoveDriverFromDeliveryRequest**](DeliveryRemoveDriverFromDeliveryRequest.md)| The input body required by this request |

### Return type

[**DeliveryRemoveDriverFromDeliveryResponse**](DeliveryRemoveDriverFromDeliveryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the driver was removed from the order |  -  |

<a name="trackDelivery"></a>
# **trackDelivery**
> DeliveryTrackDeliveryResponse trackDelivery(deliveryTrackDeliveryRequest)

Endpoint for tracking a delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryTrackDeliveryRequest deliveryTrackDeliveryRequest = new DeliveryTrackDeliveryRequest(); // DeliveryTrackDeliveryRequest | The input body required by this request
    try {
      DeliveryTrackDeliveryResponse result = apiInstance.trackDelivery(deliveryTrackDeliveryRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#trackDelivery");
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
 **deliveryTrackDeliveryRequest** | [**DeliveryTrackDeliveryRequest**](DeliveryTrackDeliveryRequest.md)| The input body required by this request |

### Return type

[**DeliveryTrackDeliveryResponse**](DeliveryTrackDeliveryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns the location of the delivery |  -  |

<a name="updateDeliveryStatus"></a>
# **updateDeliveryStatus**
> DeliveryUpdateDeliveryStatusResponse updateDeliveryStatus(deliveryUpdateDeliveryStatusRequest)

Endpoint for updating the status of a delivery

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DeliveryApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DeliveryApi apiInstance = new DeliveryApi(defaultClient);
    DeliveryUpdateDeliveryStatusRequest deliveryUpdateDeliveryStatusRequest = new DeliveryUpdateDeliveryStatusRequest(); // DeliveryUpdateDeliveryStatusRequest | The input body required by this request
    try {
      DeliveryUpdateDeliveryStatusResponse result = apiInstance.updateDeliveryStatus(deliveryUpdateDeliveryStatusRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DeliveryApi#updateDeliveryStatus");
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
 **deliveryUpdateDeliveryStatusRequest** | [**DeliveryUpdateDeliveryStatusRequest**](DeliveryUpdateDeliveryStatusRequest.md)| The input body required by this request |

### Return type

[**DeliveryUpdateDeliveryStatusResponse**](DeliveryUpdateDeliveryStatusResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the status of the delivery was updated |  -  |

