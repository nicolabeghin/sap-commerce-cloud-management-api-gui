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
 * Property key and value pair
 */
@Schema(description = "Property key and value pair")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class PropertyDTO {
    @SerializedName("key")
    private String key = null;

    @SerializedName("value")
    private Object value = null;

    public PropertyDTO key(String key) {
        this.key = key;
        return this;
    }

    /**
     * Property key
     *
     * @return key
     **/
    @Schema(description = "Property key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PropertyDTO value(Object value) {
        this.value = value;
        return this;
    }

    /**
     * Property value
     *
     * @return value
     **/
    @Schema(description = "Property value")
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PropertyDTO propertyDTO = (PropertyDTO) o;
        return Objects.equals(this.key, propertyDTO.key) && Objects.equals(this.value, propertyDTO.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class PropertyDTO {\n");

        sb.append("    key: ").append(toIndentedString(key)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
