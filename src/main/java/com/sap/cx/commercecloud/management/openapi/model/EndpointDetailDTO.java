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
 * Entity to represent an endpoint
 */
@Schema(description = "Entity to represent an endpoint")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class EndpointDetailDTO {
    @SerializedName("code")
    private String code = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("webProxy")
    private String webProxy = null;

    @SerializedName("service")
    private String service = null;

    @SerializedName("url")
    private String url = null;

    public EndpointDetailDTO code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Endpoint code
     *
     * @return code
     **/
    @Schema(required = true, description = "Endpoint code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EndpointDetailDTO name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Endpoint name
     *
     * @return name
     **/
    @Schema(required = true, description = "Endpoint name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EndpointDetailDTO webProxy(String webProxy) {
        this.webProxy = webProxy;
        return this;
    }

    /**
     * Endpoint web proxy
     *
     * @return webProxy
     **/
    @Schema(required = true, description = "Endpoint web proxy")
    public String getWebProxy() {
        return webProxy;
    }

    public void setWebProxy(String webProxy) {
        this.webProxy = webProxy;
    }

    public EndpointDetailDTO service(String service) {
        this.service = service;
        return this;
    }

    /**
     * Endpoint service
     *
     * @return service
     **/
    @Schema(required = true, description = "Endpoint service")
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public EndpointDetailDTO url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Endpoint url
     *
     * @return url
     **/
    @Schema(required = true, description = "Endpoint url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndpointDetailDTO endpointDetailDTO = (EndpointDetailDTO) o;
        return Objects.equals(this.code, endpointDetailDTO.code) && Objects.equals(this.name, endpointDetailDTO.name) && Objects.equals(this.webProxy, endpointDetailDTO.webProxy) && Objects.equals(this.service, endpointDetailDTO.service) && Objects.equals(this.url, endpointDetailDTO.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, webProxy, service, url);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EndpointDetailDTO {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    webProxy: ").append(toIndentedString(webProxy)).append("\n");
        sb.append("    service: ").append(toIndentedString(service)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
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
