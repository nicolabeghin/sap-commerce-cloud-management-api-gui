package com.sap.cx.commercecloud.management.openapi.api;

import com.sap.cx.commercecloud.management.openapi.CollectionFormats.*;

import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import com.sap.cx.commercecloud.management.openapi.model.CreateUserRoleAssignmentDTO;
import com.sap.cx.commercecloud.management.openapi.model.DeleteUserRoleAssignmentDTO;
import com.sap.cx.commercecloud.management.openapi.model.ErrorDTO;
import com.sap.cx.commercecloud.management.openapi.model.UpdateUserRoleAssignmentDTO;
import com.sap.cx.commercecloud.management.openapi.model.UserRoleAssignmentDTO;
import com.sap.cx.commercecloud.management.openapi.model.UserRoleAssignmentListDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserRoleAssignmentsApi {
  /**
   * 
   * assign a role for a user for a list of environments for a given subscription
   * @param body user role to assign (required)
   * @param subscriptionCode subscription code (required)
   * @return Call&lt;UserRoleAssignmentDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("subscriptions/{subscriptionCode}/userroleassignments")
  Call<UserRoleAssignmentDTO> createUserRoleAssignment(
    @retrofit2.http.Body CreateUserRoleAssignmentDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

  /**
   * 
   * delete a user role assignment for a given subscription
   * @param body user role assignment to delete (required)
   * @param subscriptionCode subscription code (required)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @DELETE("subscriptions/{subscriptionCode}/userroleassignments")
  Call<Void> deleteUserRoleAssignment(
    @retrofit2.http.Body DeleteUserRoleAssignmentDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

  /**
   * 
   * get all user roles for a subscription
   * @param subscriptionCode subscription code (required)
   * @return Call&lt;List&lt;String&gt;&gt;
   */
  @GET("subscriptions/{subscriptionCode}/roles")
  Call<List<String>> getAllRolesForSubscription(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

  /**
   * 
   * get all users and their roles for a subscription
   * @param subscriptionCode subscription code (required)
   * @return Call&lt;UserRoleAssignmentListDTO&gt;
   */
  @GET("subscriptions/{subscriptionCode}/userroleassignments")
  Call<UserRoleAssignmentListDTO> getAllUserRoleAssignments(
    @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

  /**
   * 
   * update user role assignment for environment for a given subscription
   * @param body user role assignment to update (required)
   * @param subscriptionCode subscription code (required)
   * @return Call&lt;UserRoleAssignmentDTO&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PATCH("subscriptions/{subscriptionCode}/userroleassignments")
  Call<UserRoleAssignmentDTO> updateUserRoleAssignment(
    @retrofit2.http.Body UpdateUserRoleAssignmentDTO body, @retrofit2.http.Path("subscriptionCode") String subscriptionCode
  );

}
