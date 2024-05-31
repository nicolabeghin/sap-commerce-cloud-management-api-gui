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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Update Traffic Split request.
 */
@Schema(description = "Update Traffic Split request.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class UpdateTrafficSplitRequestDTO {
    @SerializedName("services")
    private List<TrafficPercentageRequestDTO> services = null;

    @SerializedName("endpoints")
    private List<TrafficPercentageRequestDTO> endpoints = null;

    public UpdateTrafficSplitRequestDTO services(List<TrafficPercentageRequestDTO> services) {
        this.services = services;
        return this;
    }

    public UpdateTrafficSplitRequestDTO addServicesItem(TrafficPercentageRequestDTO servicesItem) {
        if (this.services == null) {
            this.services = new ArrayList<TrafficPercentageRequestDTO>();
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
    public List<TrafficPercentageRequestDTO> getServices() {
        return services;
    }

    public void setServices(List<TrafficPercentageRequestDTO> services) {
        this.services = services;
    }

    public UpdateTrafficSplitRequestDTO endpoints(List<TrafficPercentageRequestDTO> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public UpdateTrafficSplitRequestDTO addEndpointsItem(TrafficPercentageRequestDTO endpointsItem) {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<TrafficPercentageRequestDTO>();
        }
        this.endpoints.add(endpointsItem);
        return this;
    }

    /**
     * Traffic Split for endpoints
     *
     * @return endpoints
     **/
    @Schema(description = "Traffic Split for endpoints")
    public List<TrafficPercentageRequestDTO> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<TrafficPercentageRequestDTO> endpoints) {
        this.endpoints = endpoints;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UpdateTrafficSplitRequestDTO updateTrafficSplitRequestDTO = (UpdateTrafficSplitRequestDTO) o;
        return Objects.equals(this.services, updateTrafficSplitRequestDTO.services) && Objects.equals(this.endpoints, updateTrafficSplitRequestDTO.endpoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(services, endpoints);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UpdateTrafficSplitRequestDTO {\n");

        sb.append("    services: ").append(toIndentedString(services)).append("\n");
        sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
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
