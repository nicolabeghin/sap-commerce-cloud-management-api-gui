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
 * Entity to be used to cancel a deployment
 */
@Schema(description = "Entity to be used to cancel a deployment")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class DeploymentCancellationRequestDTO {
    @SerializedName("rollbackDatabase")
    private Boolean rollbackDatabase = null;

    @SerializedName("cancelReason")
    private String cancelReason = null;

    public DeploymentCancellationRequestDTO rollbackDatabase(Boolean rollbackDatabase) {
        this.rollbackDatabase = rollbackDatabase;
        return this;
    }

    /**
     * Optional flag to indicate if the database should be rolled back
     *
     * @return rollbackDatabase
     **/
    @Schema(description = "Optional flag to indicate if the database should be rolled back")
    public Boolean isRollbackDatabase() {
        return rollbackDatabase;
    }

    public void setRollbackDatabase(Boolean rollbackDatabase) {
        this.rollbackDatabase = rollbackDatabase;
    }

    public DeploymentCancellationRequestDTO cancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
        return this;
    }

    /**
     * Reason for cancelling a deployment
     *
     * @return cancelReason
     **/
    @Schema(description = "Reason for cancelling a deployment")
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeploymentCancellationRequestDTO deploymentCancellationRequestDTO = (DeploymentCancellationRequestDTO) o;
        return Objects.equals(this.rollbackDatabase, deploymentCancellationRequestDTO.rollbackDatabase) && Objects.equals(this.cancelReason, deploymentCancellationRequestDTO.cancelReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rollbackDatabase, cancelReason);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeploymentCancellationRequestDTO {\n");

        sb.append("    rollbackDatabase: ").append(toIndentedString(rollbackDatabase)).append("\n");
        sb.append("    cancelReason: ").append(toIndentedString(cancelReason)).append("\n");
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
