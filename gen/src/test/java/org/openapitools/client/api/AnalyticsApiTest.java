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
import org.openapitools.client.model.AnalyticsCreateUserReportRequest;
import org.openapitools.client.model.AnalyticsCreateUserReportResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for AnalyticsApi
 */
@Ignore
public class AnalyticsApiTest {

    private final AnalyticsApi api = new AnalyticsApi();

    
    /**
     * Endpoint for creating user report
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createUserReportTest() throws ApiException {
        AnalyticsCreateUserReportRequest analyticsCreateUserReportRequest = null;
        AnalyticsCreateUserReportResponse response = api.createUserReport(analyticsCreateUserReportRequest);

        // TODO: test validations
    }
    
}
