/*
 * SAP Commerce Cloud - Management API
 * The API to manage your SAP Commerce environments in the cloud, including provisioning, building releases, deploying, operating and more.
 *
 * OpenAPI spec version: 2.0.11
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.sap.cx.commercecloud.management.openapi.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 * Details of an environment
 */
@Schema(description = "Details of an environment")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class EnvironmentDetailDTO {
    public EnvironmentDetailDTO() {
    }

    @SerializedName("subscriptionCode")
    private String subscriptionCode = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("code")
    private String code = null;

    @SerializedName("type")
    private String type = null;

    @SerializedName("status")
    private String status = null;

    @SerializedName("deploymentStatus")
    private String deploymentStatus = null;

    public EnvironmentDetailDTO subscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
        return this;
    }

    /**
     * Subscription code
     *
     * @return subscriptionCode
     **/
    @Schema(description = "Subscription code")
    public String getSubscriptionCode() {
        return subscriptionCode;
    }

    public void setSubscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }

    public EnvironmentDetailDTO name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Environment name
     *
     * @return name
     **/
    @Schema(description = "Environment name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnvironmentDetailDTO code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Environment code
     *
     * @return code
     **/
    @Schema(description = "Environment code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EnvironmentDetailDTO type(String type) {
        this.type = type;
        return this;
    }

    /**
     * Environment type (persona)
     *
     * @return type
     **/
    @Schema(description = "Environment type (persona)")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EnvironmentDetailDTO status(String status) {
        this.status = status;
        return this;
    }

    /**
     * Environment status
     *
     * @return status
     **/
    @Schema(description = "Environment status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EnvironmentDetailDTO deploymentStatus(String deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
        return this;
    }

    /**
     * Deployment status of the environment
     *
     * @return deploymentStatus
     **/
    @Schema(description = "Deployment status of the environment")
    public String getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(String deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnvironmentDetailDTO environmentDetailDTO = (EnvironmentDetailDTO) o;
        return Objects.equals(this.subscriptionCode, environmentDetailDTO.subscriptionCode) && Objects.equals(this.name, environmentDetailDTO.name) && Objects.equals(this.code, environmentDetailDTO.code) && Objects.equals(this.type, environmentDetailDTO.type) && Objects.equals(this.status, environmentDetailDTO.status) && Objects.equals(this.deploymentStatus, environmentDetailDTO.deploymentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriptionCode, name, code, type, status, deploymentStatus);
    }

    @Override
    public String toString() {
        return "%s - %s [%s]".formatted(this.code, this.type, this.status);
    }
//
//  @Override
//  public String toString() {
//    StringBuilder sb = new StringBuilder();
//    sb.append("class EnvironmentDetailDTO {\n");
//
//    sb.append("    subscriptionCode: ").append(toIndentedString(subscriptionCode)).append("\n");
//    sb.append("    name: ").append(toIndentedString(name)).append("\n");
//    sb.append("    code: ").append(toIndentedString(code)).append("\n");
//    sb.append("    type: ").append(toIndentedString(type)).append("\n");
//    sb.append("    status: ").append(toIndentedString(status)).append("\n");
//    sb.append("    deploymentStatus: ").append(toIndentedString(deploymentStatus)).append("\n");
//    sb.append("}");
//    return sb.toString();
//  }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
