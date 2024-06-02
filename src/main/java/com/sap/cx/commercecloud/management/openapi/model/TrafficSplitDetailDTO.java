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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Traffic Split detail DTO.
 */
@Schema(description = "Traffic Split detail DTO.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class TrafficSplitDetailDTO {
    @SerializedName("services")
    private List<TrafficPercentageDTO> services = null;

    @SerializedName("endpoints")
    private List<TrafficPercentageDTO> endpoints = null;

    @SerializedName("lastModifiedTimestamp")
    private OffsetDateTime lastModifiedTimestamp = null;

    @SerializedName("lastModifiedBy")
    private String lastModifiedBy = null;

    @SerializedName("subscriptionCode")
    private String subscriptionCode = null;

    @SerializedName("deploymentCode")
    private String deploymentCode = null;

    @SerializedName("code")
    private String code = null;

    public TrafficSplitDetailDTO services(List<TrafficPercentageDTO> services) {
        this.services = services;
        return this;
    }

    public TrafficSplitDetailDTO addServicesItem(TrafficPercentageDTO servicesItem) {
        if (this.services == null) {
            this.services = new ArrayList<TrafficPercentageDTO>();
        }
        this.services.add(servicesItem);
        return this;
    }

    /**
     * Traffic Split for services
     *
     * @return services
     **/
    @Schema(description = "Traffic Split for services")
    public List<TrafficPercentageDTO> getServices() {
        return services;
    }

    public void setServices(List<TrafficPercentageDTO> services) {
        this.services = services;
    }

    public TrafficSplitDetailDTO endpoints(List<TrafficPercentageDTO> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public TrafficSplitDetailDTO addEndpointsItem(TrafficPercentageDTO endpointsItem) {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<TrafficPercentageDTO>();
        }
        this.endpoints.add(endpointsItem);
        return this;
    }

    /**
     * Get endpoints
     *
     * @return endpoints
     **/
    @Schema(description = "")
    public List<TrafficPercentageDTO> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<TrafficPercentageDTO> endpoints) {
        this.endpoints = endpoints;
    }

    public TrafficSplitDetailDTO lastModifiedTimestamp(OffsetDateTime lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
        return this;
    }

    /**
     * last Modified Timestamp
     *
     * @return lastModifiedTimestamp
     **/
    @Schema(description = "last Modified Timestamp")
    public OffsetDateTime getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(OffsetDateTime lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public TrafficSplitDetailDTO lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    /**
     * last Modified user
     *
     * @return lastModifiedBy
     **/
    @Schema(description = "last Modified user")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public TrafficSplitDetailDTO subscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
        return this;
    }

    /**
     * Code of the subscription
     *
     * @return subscriptionCode
     **/
    @Schema(description = "Code of the subscription")
    public String getSubscriptionCode() {
        return subscriptionCode;
    }

    public void setSubscriptionCode(String subscriptionCode) {
        this.subscriptionCode = subscriptionCode;
    }

    public TrafficSplitDetailDTO deploymentCode(String deploymentCode) {
        this.deploymentCode = deploymentCode;
        return this;
    }

    /**
     * Code of the deployment
     *
     * @return deploymentCode
     **/
    @Schema(required = true, description = "Code of the deployment")
    public String getDeploymentCode() {
        return deploymentCode;
    }

    public void setDeploymentCode(String deploymentCode) {
        this.deploymentCode = deploymentCode;
    }

    public TrafficSplitDetailDTO code(String code) {
        this.code = code;
        return this;
    }

    /**
     * unique code of traffic split
     *
     * @return code
     **/
    @Schema(description = "unique code of traffic split")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrafficSplitDetailDTO trafficSplitDetailDTO = (TrafficSplitDetailDTO) o;
        return Objects.equals(this.services, trafficSplitDetailDTO.services) && Objects.equals(this.endpoints, trafficSplitDetailDTO.endpoints) && Objects.equals(this.lastModifiedTimestamp, trafficSplitDetailDTO.lastModifiedTimestamp) && Objects.equals(this.lastModifiedBy, trafficSplitDetailDTO.lastModifiedBy) && Objects.equals(this.subscriptionCode, trafficSplitDetailDTO.subscriptionCode) && Objects.equals(this.deploymentCode, trafficSplitDetailDTO.deploymentCode) && Objects.equals(this.code, trafficSplitDetailDTO.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(services, endpoints, lastModifiedTimestamp, lastModifiedBy, subscriptionCode, deploymentCode, code);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TrafficSplitDetailDTO {\n");

        sb.append("    services: ").append(toIndentedString(services)).append("\n");
        sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
        sb.append("    lastModifiedTimestamp: ").append(toIndentedString(lastModifiedTimestamp)).append("\n");
        sb.append("    lastModifiedBy: ").append(toIndentedString(lastModifiedBy)).append("\n");
        sb.append("    subscriptionCode: ").append(toIndentedString(subscriptionCode)).append("\n");
        sb.append("    deploymentCode: ").append(toIndentedString(deploymentCode)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
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
