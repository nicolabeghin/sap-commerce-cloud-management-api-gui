package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.model.EndpointDetailsDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EndpointApi {
    /**
     * List endpoints for environment
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param environmentCode  Environment in the subscription (required)
     * @param webProxy         The web proxy by which to filter endpoints, possible values are \&quot;public\&quot;, \&quot;private\&quot; or \&quot;nat\&quot; (optional)
     * @param service          The service by which to filter endpoints (optional)
     * @return Call&lt;EndpointDetailsDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/endpoints")
    Call<EndpointDetailsDTO> getEndpoints(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Query("webProxy") String webProxy, @retrofit2.http.Query("service") String service);

}
