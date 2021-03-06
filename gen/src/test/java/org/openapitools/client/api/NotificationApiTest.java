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
import org.openapitools.client.model.NotificationSendDirectEmailNotificationRequest;
import org.openapitools.client.model.NotificationSendDirectEmailNotificationResponse;
import org.openapitools.client.model.NotificationSendEmailNotificationRequest;
import org.openapitools.client.model.NotificationSendEmailNotificationResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for NotificationApi
 */
@Ignore
public class NotificationApiTest {

    private final NotificationApi api = new NotificationApi();

    
    /**
     * Endpoint for sending a direct email notification
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sendDirectEmailNotificationTest() throws ApiException {
        NotificationSendDirectEmailNotificationRequest notificationSendDirectEmailNotificationRequest = null;
        NotificationSendDirectEmailNotificationResponse response = api.sendDirectEmailNotification(notificationSendDirectEmailNotificationRequest);

        // TODO: test validations
    }
    
    /**
     * Endpoint for sending an email notification
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void sendEmailNotificationTest() throws ApiException {
        NotificationSendEmailNotificationRequest notificationSendEmailNotificationRequest = null;
        NotificationSendEmailNotificationResponse response = api.sendEmailNotification(notificationSendEmailNotificationRequest);

        // TODO: test validations
    }
    
}
