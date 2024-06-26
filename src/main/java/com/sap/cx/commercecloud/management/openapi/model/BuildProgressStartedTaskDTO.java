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
 * Started task of a build progress
 */
@Schema(description = "Started task of a build progress")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class BuildProgressStartedTaskDTO {
    @SerializedName("task")
    private String task = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("startTimestamp")
    private OffsetDateTime startTimestamp = null;

    public BuildProgressStartedTaskDTO task(String task) {
        this.task = task;
        return this;
    }

    /**
     * Name of the task
     *
     * @return task
     **/
    @Schema(description = "Name of the task")
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public BuildProgressStartedTaskDTO name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Business name of the task
     *
     * @return name
     **/
    @Schema(description = "Business name of the task")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BuildProgressStartedTaskDTO startTimestamp(OffsetDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
        return this;
    }

    /**
     * Start timestamp of the task
     *
     * @return startTimestamp
     **/
    @Schema(description = "Start timestamp of the task")
    public OffsetDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(OffsetDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuildProgressStartedTaskDTO buildProgressStartedTaskDTO = (BuildProgressStartedTaskDTO) o;
        return Objects.equals(this.task, buildProgressStartedTaskDTO.task) && Objects.equals(this.name, buildProgressStartedTaskDTO.name) && Objects.equals(this.startTimestamp, buildProgressStartedTaskDTO.startTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, name, startTimestamp);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BuildProgressStartedTaskDTO {\n");

        sb.append("    task: ").append(toIndentedString(task)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    startTimestamp: ").append(toIndentedString(startTimestamp)).append("\n");
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
