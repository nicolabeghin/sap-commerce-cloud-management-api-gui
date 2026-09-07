package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.EndpointDTO;
import com.sap.cx.commercecloud.management.openapi.model.EndpointDetailsDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EndpointApi {
  /**
   * 
   * 
   * @param body  (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @return Call&lt;EndpointDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/environments/{environmentCode}/endpoints")
  Call<EndpointDTO> createEndpoint(
    @retrofit2.http.Body EndpointDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * 
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param endpointCode Endpoint code (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("subscriptions/{subscriptionCode}/environments/{environmentCode}/endpoints/{endpointCode}")
  Call<Void> deleteEndpoint(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("endpointCode") String endpointCode
  );

  /**
   * 
   * Get a single endpoint
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param endpointCode Endpoint code (required)
   * @return Call&lt;EndpointDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/endpoints/{endpointCode}")
  Call<EndpointDTO> getEndpoint(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("endpointCode") String endpointCode
  );

  /**
   * 
   * List endpoints for environment
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param webProxy The web proxy by which to filter endpoints, possible values are \&quot;public\&quot;, \&quot;private\&quot; or \&quot;nat\&quot; (optional)
   * @param service The service by which to filter endpoints (optional)
   * @return Call&lt;EndpointDetailsDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/endpoints")
  Call<EndpointDetailsDTO> getEndpoints(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Query("webProxy") String webProxy, @retrofit2.http.Query("service") String service
  );

  /**
   * 
   * Update an endpoint
   * @param body  (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param endpointCode Endpoint code (required)
   * @return Call&lt;EndpointDTO&gt;
   */
  @Headers({
    "Content-Type:application/merge-patch+json"
  })
  @PATCH("subscriptions/{subscriptionCode}/environments/{environmentCode}/endpoints/{endpointCode}")
  Call<EndpointDTO> updateEndpoint(
    @retrofit2.http.Body Map<String, Object> body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("endpointCode") String endpointCode
  );

}
