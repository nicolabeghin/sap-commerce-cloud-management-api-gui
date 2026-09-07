package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks.BuildCreateTask;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks.AbstractTask;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks.BuildWaitForCompletionTask;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks.DeploymentCreateTask;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks.DeploymentWaitForCompletionTask;
import com.sap.cx.commercecloud.management.openapi.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.TaskProgressView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the modal progress dialog ({@code progress.fxml}) that drives the
 * build-and-deploy flow. Depending on what the caller sets, it runs one of:
 * build → (optionally) wait → deploy → wait, or deploy → wait only.
 *
 * <p>Each phase is a daemon task chained from the previous one's success handler, and
 * every task is added to the {@link TaskProgressView} so the user sees per-step progress.
 */
public class BuildController extends AbstractController implements Initializable {

    @FXML
    public TaskProgressView taskProgressView;

    private CreateBuildRequestDTO createBuildRequestDTO;
    private CreateDeploymentRequestDTO createDeploymentRequestDTO;

    private boolean deploy = false;

    public CreateBuildRequestDTO getCreateBuildRequestDTO() {
        return createBuildRequestDTO;
    }

    public void setCreateBuildRequestDTO(CreateBuildRequestDTO createBuildRequestDTO) {
        this.createBuildRequestDTO = createBuildRequestDTO;
    }

    public CreateDeploymentRequestDTO getCreateDeploymentRequestDTO() {
        return createDeploymentRequestDTO;
    }

    public void setCreateDeploymentRequestDTO(CreateDeploymentRequestDTO createDeploymentRequestDTO) {
        this.createDeploymentRequestDTO = createDeploymentRequestDTO;
    }

    public boolean isDeploy() {
        return deploy;
    }

    public void setDeploy(boolean deploy) {
        this.deploy = deploy;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskProgressView.setRetainTasks(true);
        // Kick off the flow once the dialog is shown: a build request implies the
        // build(+optional deploy) path; otherwise a deployment request must be present.
        Platform.runLater(() -> {
            try {
                if (createBuildRequestDTO != null) {
                    onStartBuild();
                } else {
                    if (createDeploymentRequestDTO == null) {
                        throw new Exception("No deployment info");
                    }
                    onStartDeployment();
                }

            } catch (Exception e) {
                App.LOG.error(e.getMessage());
                dialogError(e.getMessage());
            }
        });
    }

    private void onStartBuild() {
        BuildCreateTask task = new BuildCreateTask(createBuildRequestDTO);
        task.setOnSucceeded(event -> {
            CreateBuildResponseDTO createBuildResponseDTO = task.getValue();
            notificationInfo("Build " + createBuildResponseDTO.getCode(), "Build request entered correctly");
            waitForBuildComplete(createBuildResponseDTO.getCode());
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        taskProgressView.getTasks().add(task);
        AbstractTask.startDaemon(task);
    }

    private void waitForBuildComplete(String buildCode) {
        BuildWaitForCompletionTask task = new BuildWaitForCompletionTask(buildCode);
        task.setOnSucceeded(event -> {
            BuildProgressDTO buildProgressDTO = task.getValue();
            notificationInfo("Build complete", "Build completed");
            if (isDeploy()) {
                onStartDeploymentFromCompletedBuild(buildProgressDTO);
            }
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        taskProgressView.getTasks().add(task);
        AbstractTask.startDaemon(task);
    }

    private void onStartDeploymentFromCompletedBuild(BuildProgressDTO build) {
        if (!"SUCCESS".equals(build.getBuildStatus())) {
            dialogError("Build cannot be deployed (status " + build.getBuildStatus() + ")");
            return;
        }
        createDeploymentRequestDTO.setBuildCode(build.getBuildCode());
        onStartDeployment();
    }

    private void onStartDeployment() {
        DeploymentCreateTask task = new DeploymentCreateTask(createDeploymentRequestDTO);
        task.setOnSucceeded(event -> {
            CreateDeploymentResponseDTO createDeploymentResponseDTO = task.getValue();
            notificationInfo("Deployment " + createDeploymentResponseDTO.getCode(), "Deployment request entered correctly");
            waitForDeploymentComplete(createDeploymentResponseDTO.getCode());
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        taskProgressView.getTasks().add(task);
        AbstractTask.startDaemon(task);
    }

    private void waitForDeploymentComplete(String deploymentCode) {
        DeploymentWaitForCompletionTask task = new DeploymentWaitForCompletionTask(deploymentCode);
        task.setOnSucceeded(event -> {
            // The wait task returns true on FAILURE, false on success (inverted convention).
            Boolean aBoolean = task.getValue();
            if (aBoolean) {
                notificationError("Deployment", "Deployment failed");
            } else {
                notificationInfo("Deployment completed", "Deployment completed successfully");
            }
        });
        task.setOnFailed(event -> {
            dialogError(task.getException().getMessage());
        });
        taskProgressView.getTasks().add(task);
        AbstractTask.startDaemon(task);
    }
}

