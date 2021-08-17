# AnalyticsApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**createUserReport**](AnalyticsApi.md#createUserReport) | **POST** /analytics/createUserReport | Endpoint for creating user report


<a name="createUserReport"></a>
# **createUserReport**
> AnalyticsCreateUserReportResponse createUserReport(analyticsCreateUserReportRequest)

Endpoint for creating user report

Refer to summary

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.AnalyticsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    AnalyticsApi apiInstance = new AnalyticsApi(defaultClient);
    AnalyticsCreateUserReportRequest analyticsCreateUserReportRequest = new AnalyticsCreateUserReportRequest(); // AnalyticsCreateUserReportRequest | The input body required by this request
    try {
      AnalyticsCreateUserReportResponse result = apiInstance.createUserReport(analyticsCreateUserReportRequest);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AnalyticsApi#createUserReport");
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
 **analyticsCreateUserReportRequest** | [**AnalyticsCreateUserReportRequest**](AnalyticsCreateUserReportRequest.md)| The input body required by this request |

### Return type

[**AnalyticsCreateUserReportResponse**](AnalyticsCreateUserReportResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | Returns whether Report was downloaded |  -  |

