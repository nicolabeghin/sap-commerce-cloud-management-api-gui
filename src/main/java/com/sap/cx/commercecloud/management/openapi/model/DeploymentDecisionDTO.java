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
import org.threeten.bp.OffsetDateTime;

import java.util.Objects;

/**
 * Deployment decision
 */
@Schema(description = "Deployment decision")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class DeploymentDecisionDTO {
    @SerializedName("reason")
    private String reason = null;

    @SerializedName("type")
    private DeploymentDecision type = null;

    @SerializedName("subscriptionCode")
    private String subscriptionCode = null;

    @SerializedName("deploymentCode")
    private String deploymentCode = null;

    @SerializedName("createdTimestamp")
    private OffsetDateTime createdTimestamp = null;

    @SerializedName("createdBy")
    private String createdBy = null;

    public DeploymentDecisionDTO reason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * reason for decision
     *
     * @return reason
     **/
    @Schema(description = "reason for decision")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DeploymentDecisionDTO type(DeploymentDecision type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @Schema(description = "")
    public DeploymentDecision getType() {
        return type;
    }

    public void setType(DeploymentDecision type) {
        this.type = type;
    }

    public DeploymentDecisionDTO subscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
        return this;
    }

    /**
     * subscription code
     *
     * @return subscriptionCode
     **/
    @Schema(description = "subscription code")
    public String getSubscriptionCode() {
        return subscriptionCode;
    }

    public void setSubscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }

    public DeploymentDecisionDTO deploymentCode(String deploymentCode) {
        this.deploymentCode = deploymentCode;
        return this;
    }

    /**
     * deployment code
     *
     * @return deploymentCode
     **/
    @Schema(description = "deployment code")
    public String getDeploymentCode() {
        return deploymentCode;
    }

    public void setDeploymentCode(String deploymentCode) {
        this.deploymentCode = deploymentCode;
    }

    public DeploymentDecisionDTO createdTimestamp(OffsetDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }

    /**
     * date time when decision was made
     *
     * @return createdTimestamp
     **/
    @Schema(description = "date time when decision was made")
    public OffsetDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(OffsetDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public DeploymentDecisionDTO createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * userId who made the decision
     *
     * @return createdBy
     **/
    @Schema(description = "userId who made the decision")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeploymentDecisionDTO deploymentDecisionDTO = (DeploymentDecisionDTO) o;
        return Objects.equals(this.reason, deploymentDecisionDTO.reason) && Objects.equals(this.type, deploymentDecisionDTO.type) && Objects.equals(this.subscriptionCode, deploymentDecisionDTO.subscriptionCode) && Objects.equals(this.deploymentCode, deploymentDecisionDTO.deploymentCode) && Objects.equals(this.createdTimestamp, deploymentDecisionDTO.createdTimestamp) && Objects.equals(this.createdBy, deploymentDecisionDTO.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, type, subscriptionCode, deploymentCode, createdTimestamp, createdBy);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeploymentDecisionDTO {\n");

        sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    subscriptionCode: ").append(toIndentedString(subscriptionCode)).append("\n");
        sb.append("    deploymentCode: ").append(toIndentedString(deploymentCode)).append("\n");
        sb.append("    createdTimestamp: ").append(toIndentedString(createdTimestamp)).append("\n");
        sb.append("    createdBy: ").append(toIndentedString(createdBy)).append("\n");
        sb.append("}");
        return sb.toString();
    }

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
