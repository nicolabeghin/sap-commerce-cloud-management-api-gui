package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.EnvironmentScalingDetailDTO;
import com.sap.cx.commercecloud.management.openapi.model.EnvironmentScalingOptionsDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;
import com.sap.cx.commercecloud.management.openapi.model.UpdateEnvironmentScalingDetailRequestDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EnvironmentScalingApi {
  /**
   * 
   * Get scaling details for a given environment
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment created into the subscription (required)
   * @return Call&lt;EnvironmentScalingDetailDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/scaling")
  Call<EnvironmentScalingDetailDTO> getEnvironmentScalingDetail(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * Get scaling options for a given environment
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment created into the subscription (required)
   * @return Call&lt;EnvironmentScalingOptionsDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/scalingOptions")
  Call<EnvironmentScalingOptionsDTO> getEnvironmentScalingOption(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * Update and apply scaling details for a given environment
   * @param body Scaling details (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment created into the subscription (required)
   * @return Call&lt;EnvironmentScalingDetailDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PATCH("subscriptions/{subscriptionCode}/environments/{environmentCode}/scaling")
  Call<EnvironmentScalingDetailDTO> updateEnvironmentScalingDetail(
    @retrofit2.http.Body UpdateEnvironmentScalingDetailRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

}
