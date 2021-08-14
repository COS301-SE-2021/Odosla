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
import org.openapitools.client.model.PaymentUpdateOrderRequest;
import org.openapitools.client.model.PaymentUpdateOrderResponse;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for PaymentApi
 */
@Ignore
public class PaymentApiTest {

    private final PaymentApi api = new PaymentApi();

    
    /**
     * Endpoint for the Update Order use case
     *
     * Refer to summary
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updateOrderTest() throws ApiException {
        PaymentUpdateOrderRequest paymentUpdateOrderRequest = null;
        PaymentUpdateOrderResponse response = api.updateOrder(paymentUpdateOrderRequest);

        // TODO: test validations
    }
    
}
