package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.model.EnvironmentDetailsDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EnvironmentApi {
    /**
     * Get environments for subscription
     *
     * @param subscriptionCode Subscription code (required)
     * @param status           The status by which to filter environments, possible values are \&quot;PROVISIONING\&quot;,\&quot;AVAILABLE\&quot;,\&quot;TERMINATING\&quot;,\&quot;TERMINATED\&quot; or \&quot;READY_FOR_DEPLOYMENT\&quot; (optional)
     * @param deploymentStatus The deployment status by which to filter environments, possible values are \&quot;SCHEDULED\&quot;,\&quot;DEPLOYING\&quot;,\&quot;DEPLOYED\&quot;,\&quot;UNDEPLOYED\&quot; or \&quot;FAIL\&quot; (optional)
     * @return Call&lt;EnvironmentDetailsDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/environments")
    Call<EnvironmentDetailsDTO> getEnvironments(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Query("status") String status, @retrofit2.http.Query("deploymentStatus") String deploymentStatus);

}
