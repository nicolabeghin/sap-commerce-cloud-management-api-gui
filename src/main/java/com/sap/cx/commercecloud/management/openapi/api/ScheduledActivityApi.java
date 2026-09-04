package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.CreateScheduledActivityRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;
import com.sap.cx.commercecloud.management.openapi.model.ScheduledActivityDetailDTO;
import com.sap.cx.commercecloud.management.openapi.model.ScheduledActivityDetailsDTO;
import com.sap.cx.commercecloud.management.openapi.model.UpdateScheduledActivityRequestDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ScheduledActivityApi {
  /**
   * 
   * Cancels a scheduled activity
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment code in the subscription (required)
   * @param activityCode Activity code (required)
   * @return Call&lt;ScheduledActivityDetailDTO&gt;
   */
  @POST("subscriptions/{subscriptionCode}/environments/{environmentCode}/scheduledactivities/{activityCode}/cancel")
  Call<ScheduledActivityDetailDTO> cancelScheduledActivity(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("activityCode") String activityCode
  );

  /**
   * 
   * Create a scheduled activity for a given environment
   * @param body Create a new scheduled activity (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @return Call&lt;ScheduledActivityDetailDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/environments/{environmentCode}/scheduledactivities")
  Call<ScheduledActivityDetailDTO> createScheduledActivity(
    @retrofit2.http.Body CreateScheduledActivityRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * List scheduled activities for an environment
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param activityType The activity type by which to filter scheduled activities (optional)
   * @param status The status by which to filter scheduled activities (optional)
   * @param $top Number of items to be returned (page size) (optional)
   * @param $skip Number of items to be skipped (offset) (optional)
   * @param $orderby Comma separated list of attribute names, attributed with asc or desc (optional)
   * @return Call&lt;ScheduledActivityDetailsDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/scheduledactivities")
  Call<ScheduledActivityDetailsDTO> getScheduledActivities(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Query("activityType") List<String> activityType, @retrofit2.http.Query("status") List<String> status, @retrofit2.http.Query("$top") Integer $top, @retrofit2.http.Query("$skip") Integer $skip, @retrofit2.http.Query("$orderby") String $orderby
  );

  /**
   * 
   * Fetch scheduled activity for an environment
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment code in the subscription (required)
   * @param activityCode Activity code (required)
   * @return Call&lt;ScheduledActivityDetailDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/scheduledactivities/{activityCode}")
  Call<ScheduledActivityDetailDTO> getScheduledActivity(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("activityCode") String activityCode
  );

  /**
   * 
   * Update scheduled activity for an environment
   * @param body Date to reschedule scheduled activity (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment code in the subscription (required)
   * @param activityCode Activity code (required)
   * @return Call&lt;ScheduledActivityDetailDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PATCH("subscriptions/{subscriptionCode}/environments/{environmentCode}/scheduledactivities/{activityCode}")
  Call<ScheduledActivityDetailDTO> updateScheduledActivity(
    @retrofit2.http.Body UpdateScheduledActivityRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("activityCode") String activityCode
  );

}
