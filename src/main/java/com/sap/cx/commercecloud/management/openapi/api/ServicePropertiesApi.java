package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.model.ServicePropertyDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface ServicePropertiesApi {
    /**
     * Get specific property from a service
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param environmentCode  Environment created into the subscription (required)
     * @param serviceCode      Service code for an environment. (required)
     * @param propertyCode     Property code to get the property in a service (required)
     * @return Call&lt;ServicePropertyDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/services/{serviceCode}/properties/{propertyCode}")
    Call<ServicePropertyDTO> getProperty(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("serviceCode") String serviceCode, @retrofit2.http.Path("propertyCode") String propertyCode);

    /**
     * Update or create a property into a service
     *
     * @param body             Property info (required)
     * @param subscriptionCode Customer subscription code (required)
     * @param environmentCode  Environment created into the subscription (required)
     * @param serviceCode      Service code for an environment. (required)
     * @param propertyCode     Property code to get the property in a service (required)
     * @return Call&lt;ServicePropertyDTO&gt;
     */
    @Headers({"Content-Type:application/json"})
    @PUT("subscriptions/{subscriptionCode}/environments/{environmentCode}/services/{serviceCode}/properties/{propertyCode}")
    Call<ServicePropertyDTO> putProperty(@retrofit2.http.Body ServicePropertyDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("serviceCode") String serviceCode, @retrofit2.http.Path("propertyCode") String propertyCode);

}
