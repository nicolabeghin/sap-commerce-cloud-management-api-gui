package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.BuildProgressDTO;

import java.io.IOException;

import static com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.App.LOG;


/**
 * Polls a build until it reaches a terminal state. First waits for the build to leave
 * {@code UNKNOWN}/{@code SCHEDULED} ({@link #waitForStart}), then polls its progress,
 * relaying started tasks and percentage to the UI, until it succeeds, fails, or times out.
 */
public class BuildWaitForCompletionTask extends AbstractTask<BuildProgressDTO> {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final String buildCode;

    public BuildWaitForCompletionTask(String buildCode) {
        this.buildCode = buildCode;
    }

    @Override
    protected BuildProgressDTO call() throws Exception {
        updateTitle("Build progress");
        updateProgress(0, 100);
        updateMessage("Waiting for build completion...");
        // pollInterval in ms, timeout in minutes.
        return waitCompletion(buildCode, 4200, "120");
    }

    // Poll build progress every pollInterval ms until SUCCESS, or a failure/timeout is
    // detected by checkError. Only forward newly-started tasks and rising percentages to
    // the UI to avoid duplicate/backwards updates.
    public BuildProgressDTO waitCompletion(String buildCode, int pollInterval, String timeout) throws InterruptedException, IOException {
        LOG.info("Starting wait for build completion " + buildCode);
        this.waitForStart(buildCode, pollInterval, timeout);
        BuildProgressDTO previousProgress = null;
        updateMessage("Waiting for the build with code '" + buildCode + "' to complete.");
        int waitTime = 0;

        while (true) {
            BuildProgressDTO currentProgress = execute(getBuildApi().getBuildProgress(Constants.SUBSCRIPTION_CODE, buildCode));
            int previousNumOfStartedTasks = previousProgress == null ? 0 : previousProgress.getStartedTasks().size();
            if (currentProgress.getStartedTasks().size() > previousNumOfStartedTasks) {
                currentProgress.getStartedTasks().stream().skip(previousNumOfStartedTasks).forEach((startedTaskDTO) -> {
                    updateMessage(startedTaskDTO.getName() + " (" + startedTaskDTO.getTask() + ")");
                });
            }

            if (previousProgress == null || previousProgress.getPercentage() < currentProgress.getPercentage()) {
                LOG.info("Progress: " + currentProgress.getPercentage() + "%");
                updateProgress(currentProgress.getPercentage(), 100);
            }

            waitTime += pollInterval;
            if (this.checkError(currentProgress, waitTime, timeout)) {
                throw new InterruptedException("Error");
            }

            if ("SUCCESS".equals(currentProgress.getBuildStatus())) {
                return currentProgress;
            }

            previousProgress = currentProgress;
            Thread.sleep(pollInterval);

        }
    }

    private void waitForStart(String buildCode, int pollInterval, String timeout) throws InterruptedException, IOException {
        LOG.info("Starting wait for build start " + buildCode);
        String previousStatus = null;
        long timeoutMs = (long) Integer.parseInt(timeout) * 60000L;
        int waitTime = 0;

        while (true) {
            String currentStatus = execute(getBuildApi().getBuild(Constants.SUBSCRIPTION_CODE, buildCode)).getStatus();
            if (!"UNKNOWN".equals(currentStatus) && !"SCHEDULED".equals(currentStatus)) {
                return;
            }

            if (previousStatus == null) {
                updateMessage("Waiting for the build to start...");
            }

            previousStatus = currentStatus;

            waitTime += pollInterval;
            if (waitTime > timeoutMs) {
                throw new InterruptedException("Build '" + buildCode + "' did not start within " + timeout + " minutes, canceling the wait.");
            }

            Thread.sleep(pollInterval);

        }
    }

    private boolean checkError(BuildProgressDTO currentProgress, int waitTime, String timeout) {
        if (currentProgress.getErrorMessage() != null) {
            String var10001 = LINE_SEPARATOR;
            updateMessage("Build has failed with an error:" + var10001 + currentProgress.getErrorMessage());
            return true;
        } else {
            String buildStatus = currentProgress.getBuildStatus();
            if (!"DELETED".equals(buildStatus) && !"FAIL".equals(buildStatus)) {
                if (waitTime > Integer.parseInt(timeout) * 60000L) {
                    updateMessage("Build has not completed in " + Integer.parseInt(timeout) + " minutes, canceling the wait.");
                    return true;
                } else {
                    return false;
                }
            } else {
                updateMessage("Build has failed with the status " + buildStatus);
                return true;
            }
        }
    }
}
