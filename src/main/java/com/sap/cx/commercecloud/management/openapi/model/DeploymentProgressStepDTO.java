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
 * One step in a deployment progress stage
 */
@Schema(description = "One step in a deployment progress stage")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2024-05-30T21:55:47.726988+02:00[Europe/Rome]")

public class DeploymentProgressStepDTO {
    @SerializedName("code")
    private String code = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("startTimestamp")
    private OffsetDateTime startTimestamp = null;

    @SerializedName("endTimestamp")
    private OffsetDateTime endTimestamp = null;

    @SerializedName("message")
    private String message = null;

    @SerializedName("status")
    private String status = null;

    @SerializedName("children")
    private List<DeploymentProgressStepDTO> children = null;

    public DeploymentProgressStepDTO code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Unique code of the step
     *
     * @return code
     **/
    @Schema(description = "Unique code of the step")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DeploymentProgressStepDTO name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the step
     *
     * @return name
     **/
    @Schema(description = "Name of the step")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeploymentProgressStepDTO startTimestamp(OffsetDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
        return this;
    }

    /**
     * Start timestamp of the step
     *
     * @return startTimestamp
     **/
    @Schema(description = "Start timestamp of the step")
    public OffsetDateTime getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(OffsetDateTime startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public DeploymentProgressStepDTO endTimestamp(OffsetDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
        return this;
    }

    /**
     * End timestamp of the step
     *
     * @return endTimestamp
     **/
    @Schema(description = "End timestamp of the step")
    public OffsetDateTime getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(OffsetDateTime endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public DeploymentProgressStepDTO message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Optional message for the step describing what happened
     *
     * @return message
     **/
    @Schema(description = "Optional message for the step describing what happened")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DeploymentProgressStepDTO status(String status) {
        this.status = status;
        return this;
    }

    /**
     * Current status of the step, possible values are \&quot;PENDING\&quot;,\&quot;RUNNING\&quot;,\&quot;DONE\&quot; or \&quot;FAIL\&quot;
     *
     * @return status
     **/
    @Schema(description = "Current status of the step, possible values are \"PENDING\",\"RUNNING\",\"DONE\" or \"FAIL\"")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DeploymentProgressStepDTO children(List<DeploymentProgressStepDTO> children) {
        this.children = children;
        return this;
    }

    public DeploymentProgressStepDTO addChildrenItem(DeploymentProgressStepDTO childrenItem) {
        if (this.children == null) {
            this.children = new ArrayList<DeploymentProgressStepDTO>();
        }
        this.children.add(childrenItem);
        return this;
    }

    /**
     * One or multiple deployment progress steps
     *
     * @return children
     **/
    @Schema(description = "One or multiple deployment progress steps")
    public List<DeploymentProgressStepDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DeploymentProgressStepDTO> children) {
        this.children = children;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeploymentProgressStepDTO deploymentProgressStepDTO = (DeploymentProgressStepDTO) o;
        return Objects.equals(this.code, deploymentProgressStepDTO.code) && Objects.equals(this.name, deploymentProgressStepDTO.name) && Objects.equals(this.startTimestamp, deploymentProgressStepDTO.startTimestamp) && Objects.equals(this.endTimestamp, deploymentProgressStepDTO.endTimestamp) && Objects.equals(this.message, deploymentProgressStepDTO.message) && Objects.equals(this.status, deploymentProgressStepDTO.status) && Objects.equals(this.children, deploymentProgressStepDTO.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, startTimestamp, endTimestamp, message, status, children);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeploymentProgressStepDTO {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    startTimestamp: ").append(toIndentedString(startTimestamp)).append("\n");
        sb.append("    endTimestamp: ").append(toIndentedString(endTimestamp)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    children: ").append(toIndentedString(children)).append("\n");
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
