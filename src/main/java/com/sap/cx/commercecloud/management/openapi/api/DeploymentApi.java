package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import java.util.List;

public interface DeploymentApi {
    /**
     * Start a new deployment
     *
     * @param body             Data for a new deployment (required)
     * @param subscriptionCode Customer subscription code (required)
     * @return Call&lt;CreateDeploymentResponseDTO&gt;
     */
    @Headers({"Content-Type:application/json"})
    @POST("subscriptions/{subscriptionCode}/deployments")
    Call<CreateDeploymentResponseDTO> createDeployment(@retrofit2.http.Body CreateDeploymentRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode);

    /**
     * Create a deployment cancelation request
     *
     * @param body             Properties and flags for deployment cancelation request (required)
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;Void&gt;
     */
    @Headers({"Content-Type:application/json"})
    @POST("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/cancellation")
    Call<Void> createDeploymentCancellation(@retrofit2.http.Body DeploymentCancellationRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * deployment decision request
     *
     * @param body             Data to make a deployment decision a deployment (required)
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;CreateDeploymentDecisionResponseDTO&gt;
     */
    @Headers({"Content-Type:application/json"})
    @POST("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/decisions")
    Call<CreateDeploymentDecisionResponseDTO> createDeploymentDecision(@retrofit2.http.Body CreateDeploymentDecisionRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * Get deployment details
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;DeploymentDetailDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments/{deploymentCode}")
    Call<DeploymentDetailDTO> getDeployment(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * Get deployment cancellation options
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;DeploymentCancellationOptionsDetailDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/cancellationoptions")
    Call<DeploymentCancellationOptionsDetailDTO> getDeploymentCancellationOptions(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * Get deployment decisions
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;DeploymentDecisionsDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/decisions")
    Call<DeploymentDecisionsDTO> getDeploymentDecisions(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * Get list of environments and respective allowed deployment modes for a subscription
     *
     * @param subscriptionCode Customer subscription code (required)
     * @return Call&lt;List&lt;DeploymentModesPerEnvironmentDTO&gt;&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deploymentmodes")
    Call<List<DeploymentModesPerEnvironmentDTO>> getDeploymentModes(@retrofit2.http.Path("subscriptionCode") String subscriptionCode);

    /**
     * Get deployment progress
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;DeploymentProgressDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/progress")
    Call<DeploymentProgressDTO> getDeploymentProgress(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * Get deployments
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param buildCode        Finds all the deployments associated a build (optional)
     * @param environmentCode  Finds all the deployments associated an environment (optional)
     * @param status           The status by which to filter deployments, possible values are \&quot;SCHEDULED\&quot;,\&quot;DEPLOYING\&quot;,\&quot;DEPLOYED\&quot;,\&quot;UNDEPLOYED\&quot; or \&quot;FAIL\&quot; (optional)
     * @param $top             Number of items to be returned (page size) (optional)
     * @param $skip            Number of items to be skipped (offset) (optional)
     * @param $orderby         Comma separated list of attribute names, attributed with asc or desc (optional)
     * @param $count           Flag to provide elements count in the response (if true, then the count is provided) (optional)
     * @return Call&lt;DeploymentDetailsDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments")
    Call<DeploymentDetailsDTO> getDeployments(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Query("buildCode") String buildCode, @retrofit2.http.Query("environmentCode") String environmentCode, @retrofit2.http.Query("status") String status, @retrofit2.http.Query("$top") Integer $top, @retrofit2.http.Query("$skip") Integer $skip, @retrofit2.http.Query("$orderby") String $orderby, @retrofit2.http.Query("$count") Boolean $count);

    /**
     * Get deployment Traffic Split
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;TrafficSplitDetailDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/trafficsplit")
    Call<TrafficSplitDetailDTO> getTrafficSplit(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

    /**
     * Get deployment Traffic Split Audit History
     *
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @param $top             Number of items to be returned (page size) (optional)
     * @param $skip            Number of items to be skipped (offset) (optional)
     * @param $orderby         Comma separated list of attribute names, attributed with asc or desc (optional)
     * @param $count           Flag to provide elements count in the response (if true, then the count is provided) (optional)
     * @return Call&lt;TrafficSplitHistoryListDTO&gt;
     */
    @GET("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/trafficsplit/history")
    Call<TrafficSplitHistoryListDTO> getTrafficSplitHistory(@retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode, @retrofit2.http.Query("$top") Integer $top, @retrofit2.http.Query("$skip") Integer $skip, @retrofit2.http.Query("$orderby") String $orderby, @retrofit2.http.Query("$count") Boolean $count);

    /**
     * Update deployment traffic split
     *
     * @param body             Data to create/update traffic split of a deployment (required)
     * @param subscriptionCode Customer subscription code (required)
     * @param deploymentCode   Deployment code (required)
     * @return Call&lt;TrafficSplitDetailDTO&gt;
     */
    @Headers({"Content-Type:application/json"})
    @PUT("subscriptions/{subscriptionCode}/deployments/{deploymentCode}/trafficsplit")
    Call<TrafficSplitDetailDTO> updateTrafficSplit(@retrofit2.http.Body UpdateTrafficSplitRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("deploymentCode") String deploymentCode);

}
