package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.CreateSslCertificateDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;
import com.sap.cx.commercecloud.management.openapi.model.SslCertificateDTO;
import com.sap.cx.commercecloud.management.openapi.model.SslCertificatesDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SslCertificateApi {
  /**
   * 
   * Create a new SSL certificate
   * @param body Created a new SSL certificate (required)
   * @param subscriptionCode Customer subscription code (required)
   * @return Call&lt;SslCertificateDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/certificates")
  Call<SslCertificateDTO> createCertificate(
    @retrofit2.http.Body CreateSslCertificateDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

  /**
   * 
   * Delete SSL certificate with given code.
   * @param subscriptionCode Customer subscription code (required)
   * @param certificateCode SSL certificate code (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("subscriptions/{subscriptionCode}/certificates/{certificateCode}")
  Call<Void> deleteCertificate(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("certificateCode") String certificateCode
  );

  /**
   * 
   * Get SSL certificate with given code.
   * @param subscriptionCode Customer subscription code (required)
   * @param certificateCode SSL certificate code (required)
   * @return Call&lt;SslCertificateDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/certificates/{certificateCode}")
  Call<SslCertificateDTO> getCertificateDetails(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode, @retrofit2.http.Path("certificateCode") String certificateCode
  );

  /**
   * 
   * Get SSL certificates for given subscription.
   * @param subscriptionCode Customer subscription code (required)
   * @return Call&lt;SslCertificatesDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/certificates")
  Call<SslCertificatesDTO> getCertificates(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

}
