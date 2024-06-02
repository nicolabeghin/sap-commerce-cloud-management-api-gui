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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Deployment decisions ENUM
 */
@JsonAdapter(DeploymentDecision.Adapter.class)
public enum DeploymentDecision {
    @SerializedName("ACCEPT") ACCEPT("ACCEPT"), @SerializedName("REJECT") REJECT("REJECT"), @SerializedName("PREPARE_CANARY") PREPARE_CANARY("PREPARE_CANARY");

    private final String value;

    DeploymentDecision(String value) {
        this.value = value;
    }

    public static DeploymentDecision fromValue(String input) {
        for (DeploymentDecision b : DeploymentDecision.values()) {
            if (b.value.equals(input)) {
                return b;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static class Adapter extends TypeAdapter<DeploymentDecision> {
        @Override
        public void write(final JsonWriter jsonWriter, final DeploymentDecision enumeration) throws IOException {
            jsonWriter.value(String.valueOf(enumeration.getValue()));
        }

        @Override
        public DeploymentDecision read(final JsonReader jsonReader) throws IOException {
            Object value = jsonReader.nextString();
            return DeploymentDecision.fromValue((String) (value));
        }
    }
}
