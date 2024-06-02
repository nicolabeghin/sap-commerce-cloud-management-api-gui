package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.DeploymentProgressDTO;
import com.sap.cx.commercecloud.management.openapi.model.DeploymentProgressStageDTO;
import com.sap.cx.commercecloud.management.openapi.model.DeploymentProgressStepDTO;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;


public class DeploymentWaitForCompletionTask extends AbstractTask<Boolean> {

    private final String deploymentCode;

    public DeploymentWaitForCompletionTask(String deploymentCode) {
        this.deploymentCode = deploymentCode;
    }

    @Override
    protected Boolean call() throws Exception {
        updateTitle("Deployment progress");
        updateProgress(0, 100);
        updateMessage("Waiting for deployment completion...");
        return waitCompletion(deploymentCode, "120");
    }

    public boolean waitCompletion(String deploymentCode, String timeout) throws Exception {
        return this.waitCompletion(deploymentCode, 4200, timeout);
    }

    /**
     * @param deploymentCode
     * @param pollInterval
     * @param timeout
     * @return true for fail, false for success
     * @throws Exception
     */
    public boolean waitCompletion(String deploymentCode, int pollInterval, String timeout) throws Exception {
        this.waitForStart(deploymentCode, pollInterval);
        DeploymentProgressDTO previousProgress = null;
        updateMessage("Waiting for the deployment with code '" + deploymentCode + "' to complete.");
        boolean[] failed = new boolean[]{false};
        int waitTime = 0;

        while (!failed[0]) {
            DeploymentProgressDTO currentProgress = getDeploymentApi().getDeploymentProgress(Constants.SUBSCRIPTION_CODE, deploymentCode).execute().body();
            DeploymentProgressDTO tmpProgress = previousProgress == null ? new DeploymentProgressDTO() : previousProgress;
            currentProgress.getStages().stream().skip(this.countPrintedStages(tmpProgress)).forEach((stage) -> {
                this.printStage(stage, tmpProgress, failed);
            });
            int currentPercentage = currentProgress.getPercentage();
            if (currentPercentage != 0 && (previousProgress == null || previousProgress.getPercentage() < currentPercentage)) {
                updateProgress(currentPercentage, 100);
            }

            String status = currentProgress.getDeploymentStatus();
            if (this.checkIfFailed(status, failed, deploymentCode)) {
                return true;
            }

            if (currentPercentage >= 100) {
                this.checkIfAllStagesDone(currentProgress, deploymentCode);
                return false;
            }

            tmpProgress.setPercentage(currentPercentage);
            previousProgress = tmpProgress;
            waitTime += pollInterval;
            if (this.passWaitLimit(waitTime, deploymentCode, timeout)) {
                return true;
            }

            Thread.sleep(pollInterval);

        }

        return failed[0];
    }

    private void waitForStart(String deploymentCode, int pollInterval) throws Exception {
        String previousStatus = null;

        while (true) {
            String currentStatus = getDeploymentApi().getDeployment(Constants.SUBSCRIPTION_CODE, deploymentCode).execute().body().getStatus();
            if (!"SCHEDULED".equals(currentStatus)) {
                return;
            }

            if (previousStatus == null) {
                updateMessage("Waiting for the deployment with code '" + deploymentCode + "' to start.");
            }

            previousStatus = currentStatus;
            Thread.sleep(pollInterval);
        }
    }

    private boolean checkIfFailed(String status, boolean[] failed, String deploymentCode) {
        if (!"FAIL".equals(status) && !"UNDEPLOYED".equals(status) && !failed[0]) {
            return false;
        } else {
            updateMessage("This deployment with code '" + deploymentCode + "' has failed, see deployment log for details.");
            return true;
        }
    }

    private void checkIfAllStagesDone(DeploymentProgressDTO currentProgress, String deploymentCode) {
        boolean[] stillRunning = new boolean[]{false};
        currentProgress.getStages().forEach((stage) -> {
            if ("RUNNING".equals(stage.getStatus())) {
                stillRunning[0] = true;
            }

        });
        if (!stillRunning[0]) {
            updateMessage("This deployment with code '" + deploymentCode + "' has completed, see deployment log for details");
        } else {
            updateMessage("This deployment with code '" + deploymentCode + "' has completed but with stages still running, see deployment log for details.");
        }

    }

    private boolean passWaitLimit(int waitTime, String deploymentCode, String timeout) {
        if (waitTime > Integer.parseInt(timeout) * '\uea60') {
            updateMessage("This deployment with code '" + deploymentCode + "' has not completed in " + waitTime + "ms, see deployment log for details.");
            return true;
        } else {
            return false;
        }
    }


    private void printStage(DeploymentProgressStageDTO stage, DeploymentProgressDTO tmpProgress, boolean... failed) {
        List<DeploymentProgressStageDTO> stageList = tmpProgress.getStages() == null ? new ArrayList() : tmpProgress.getStages();
        OffsetDateTime endTime = stage.getEndTimestamp();
        if ("DONE".equals(stage.getStatus()) && endTime != null) {
            updateMessage("Stage '" + stage.getType() + "' finished at " + endTime);
            stage.getSteps().forEach((stepDTO) -> {
                this.printStep(stepDTO, failed);
            });
            ((List) stageList).add(stage);
        }

        if ("FAIL".equals(stage.getStatus())) {
            updateMessage("Stage '" + stage.getType() + "' has failed");
            stage.getSteps().forEach((stepDTO) -> {
                this.printStep(stepDTO, failed);
            });
            ((List) stageList).add(stage);
            if (!failed[0]) {
                failed[0] = true;
            }
        }

        tmpProgress.setStages((List) stageList);
    }

    private void printStep(DeploymentProgressStepDTO stepDTO, boolean... failed) {
        String stepStatus = stepDTO.getStatus();
        OffsetDateTime endTime = stepDTO.getEndTimestamp();
        if ("FAIL".equals(stepDTO.getStatus())) {
            updateMessage("Step '" + stepDTO.getName() + "' has failed.");
            if (!failed[0]) {
                failed[0] = true;
            }
        }

        if ("DONE".equals(stepStatus) && endTime != null) {
            updateMessage("Step '" + stepDTO.getName() + "' finished at " + endTime);
        }
    }

    private long countPrintedStages(DeploymentProgressDTO previousProgress) {
        return previousProgress.getStages() == null ? 0L : (long) previousProgress.getStages().size();
    }
}
