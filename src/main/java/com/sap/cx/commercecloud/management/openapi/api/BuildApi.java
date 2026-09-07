package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.BuildDetailDTO;
import com.sap.cx.commercecloud.management.openapi.model.BuildDetailsDTO;
import com.sap.cx.commercecloud.management.openapi.model.BuildProgressDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateBuildRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateBuildResponseDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BuildApi {
  /**
   * 
   * Create a build
   * @param body Create a new build (required)
   * @param subscriptionCode Customer subscription code (required)
   * @return Call&lt;CreateBuildResponseDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/builds")
  Call<CreateBuildResponseDTO> createBuild(
    @retrofit2.http.Body CreateBuildRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

  /**
   * 
   * Delete build
   * @param subscriptionCode Customer subscription code (required)
   * @param buildCode Build code (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("subscriptions/{subscriptionCode}/builds/{buildCode}")
  Call<Void> deleteBuild(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("buildCode") String buildCode
  );

  /**
   * 
   * Get build details
   * @param subscriptionCode Customer subscription code (required)
   * @param buildCode Build code (required)
   * @return Call&lt;BuildDetailDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/builds/{buildCode}")
  Call<BuildDetailDTO> getBuild(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("buildCode") String buildCode
  );

  /**
   * 
   * Get build logs
   * @param subscriptionCode Customer subscription code (required)
   * @param buildCode Build code (required)
   * @return Call&lt;File&gt;
   */
  @GET("subscriptions/{subscriptionCode}/builds/{buildCode}/logs")
  Call<ResponseBody> getBuildLogs(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("buildCode") String buildCode
  );

  /**
   * 
   * Get build progress
   * @param subscriptionCode Customer subscription code (required)
   * @param buildCode Build code (required)
   * @return Call&lt;BuildProgressDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/builds/{buildCode}/progress")
  Call<BuildProgressDTO> getBuildProgress(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("buildCode") String buildCode
  );

  /**
   * 
   * Get builds
   * @param subscriptionCode Customer subscription code (required)
   * @param statusNot Get all builds excluding provided status (optional)
   * @param $top Number of items to be returned (page size) (optional)
   * @param $skip Number of items to be skipped (offset) (optional)
   * @param $orderby Comma separated list of attribute names, attributed with asc or desc (optional)
   * @param $count Flag to provide elements count in the response (if true, then the count is provided) (optional)
   * @return Call&lt;BuildDetailsDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/builds")
  Call<BuildDetailsDTO> getBuilds(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Query("statusNot") List<String> statusNot, @retrofit2.http.Query("$top") Integer $top, @retrofit2.http.Query("$skip") Integer $skip, @retrofit2.http.Query("$orderby") String $orderby, @retrofit2.http.Query("$count") Boolean $count
  );

}
