package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.CreateDatabackupRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateDatabackupResponseDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateDatarestoreRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateDatarestoreResponseDTO;
import com.sap.cx.commercecloud.management.openapi.model.DatabackupDetailDTO;
import com.sap.cx.commercecloud.management.openapi.model.DatabackupDetailsDTO;
import com.sap.cx.commercecloud.management.openapi.model.DatabackupStateChangeDTO;
import com.sap.cx.commercecloud.management.openapi.model.DatarestoreDetailDTO;
import com.sap.cx.commercecloud.management.openapi.model.DatarestoreDetailsDTO;
import com.sap.cx.commercecloud.management.openapi.model.DatarestoreStateChangeDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DatabackupApi {
  /**
   * 
   * Change databackup state
   * @param body Change databackup state (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param databackupCode Databackup code (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("subscriptions/{subscriptionCode}/environments/{environmentCode}/databackups/{databackupCode}/state")
  Call<Void> changeDatabackupState(
    @retrofit2.http.Body DatabackupStateChangeDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("databackupCode") String databackupCode
  );

  /**
   * 
   * Change datarestore state
   * @param body Change datarestore state (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param datarestoreCode datarestore code (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("subscriptions/{subscriptionCode}/environments/{environmentCode}/datarestores/{datarestoreCode}/state")
  Call<Void> changeDatarestoreState(
    @retrofit2.http.Body DatarestoreStateChangeDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("datarestoreCode") String datarestoreCode
  );

  /**
   * 
   * Create a databackup
   * @param body Create a databackup (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @return Call&lt;CreateDatabackupResponseDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/environments/{environmentCode}/databackups")
  Call<CreateDatabackupResponseDTO> createDatabackup(
    @retrofit2.http.Body CreateDatabackupRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * Create a datarestore from a databackup
   * @param body Create a databackup (required)
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @return Call&lt;CreateDatarestoreResponseDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/environments/{environmentCode}/datarestores")
  Call<CreateDatarestoreResponseDTO> createDatarestore(
    @retrofit2.http.Body CreateDatarestoreRequestDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * Delete databackup
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param databackupCode Databackup code (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("subscriptions/{subscriptionCode}/environments/{environmentCode}/databackups/{databackupCode}")
  Call<Void> deleteDatabackup(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("databackupCode") String databackupCode
  );

  /**
   * 
   * Get databackup
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param databackupCode Databackup code (required)
   * @return Call&lt;DatabackupDetailDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/databackups/{databackupCode}")
  Call<DatabackupDetailDTO> getDatabackup(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("databackupCode") String databackupCode
  );

  /**
   * 
   * List data backups
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @return Call&lt;DatabackupDetailsDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/databackups")
  Call<DatabackupDetailsDTO> getDatabackups(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

  /**
   * 
   * Get datarestore
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @param datarestoreCode datarestore code (required)
   * @return Call&lt;DatarestoreDetailDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/datarestores/{datarestoreCode}")
  Call<DatarestoreDetailDTO> getDatarestore(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode, @retrofit2.http.Path("datarestoreCode") String datarestoreCode
  );

  /**
   * 
   * List datarestores
   * @param subscriptionCode Customer subscription code (required)
   * @param environmentCode Environment in the subscription (required)
   * @return Call&lt;DatarestoreDetailsDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/environments/{environmentCode}/datarestores")
  Call<DatarestoreDetailsDTO> getDatarestores(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("environmentCode") String environmentCode
  );

}
