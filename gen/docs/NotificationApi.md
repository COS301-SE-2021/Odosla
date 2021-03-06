# NotificationApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**sendDirectEmailNotification**](NotificationApi.md#sendDirectEmailNotification) | **POST** /notification/sendDirectEmailNotification | Endpoint for sending a direct email notification
[**sendEmailNotification**](NotificationApi.md#sendEmailNotification) | **POST** /notification/sendEmailNotification | Endpoint for sending an email notification


<a name="sendDirectEmailNotification"></a>
# **sendDirectEmailNotification**
> NotificationSendDirectEmailNotificationResponse sendDirectEmailNotification(notificationSendDirectEmailNotificationRequest)

Endpoint for sending a direct email notification

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.NotificationApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    NotificationApi apiInstance = new NotificationApi(defaultClient);
    NotificationSendDirectEmailNotificationRequest notificationSendDirectEmailNotificationRequest = new NotificationSendDirectEmailNotificationRequest(); // NotificationSendDirectEmailNotificationRequest | The input body required by this request
    try {
      NotificationSendDirectEmailNotificationResponse result = apiInstance.sendDirectEmailNotification(notificationSendDirectEmailNotificationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling NotificationApi#sendDirectEmailNotification");
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
 **notificationSendDirectEmailNotificationRequest** | [**NotificationSendDirectEmailNotificationRequest**](NotificationSendDirectEmailNotificationRequest.md)| The input body required by this request |

### Return type

[**NotificationSendDirectEmailNotificationResponse**](NotificationSendDirectEmailNotificationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the email was sent |  -  |

<a name="sendEmailNotification"></a>
# **sendEmailNotification**
> NotificationSendEmailNotificationResponse sendEmailNotification(notificationSendEmailNotificationRequest)

Endpoint for sending an email notification

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.NotificationApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    NotificationApi apiInstance = new NotificationApi(defaultClient);
    NotificationSendEmailNotificationRequest notificationSendEmailNotificationRequest = new NotificationSendEmailNotificationRequest(); // NotificationSendEmailNotificationRequest | The input body required by this request
    try {
      NotificationSendEmailNotificationResponse result = apiInstance.sendEmailNotification(notificationSendEmailNotificationRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling NotificationApi#sendEmailNotification");
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
 **notificationSendEmailNotificationRequest** | [**NotificationSendEmailNotificationRequest**](NotificationSendEmailNotificationRequest.md)| The input body required by this request |

### Return type

[**NotificationSendEmailNotificationResponse**](NotificationSendEmailNotificationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether the email was sent |  -  |

