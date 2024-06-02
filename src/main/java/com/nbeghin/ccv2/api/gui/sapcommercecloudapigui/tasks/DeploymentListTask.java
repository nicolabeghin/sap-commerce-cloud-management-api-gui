package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.DeploymentDetailsDTO;

import java.io.IOException;

public class DeploymentListTask extends AbstractTask<DeploymentDetailsDTO> {

    private final String environmentCode;

    public DeploymentListTask(String environmentCode) {
        this.environmentCode = environmentCode;
    }

    @Override
    protected DeploymentDetailsDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Loading latest deployments...");
        return getDeploymentApi().getDeployments(Constants.SUBSCRIPTION_CODE, null, environmentCode, null, 5, null, null, false).execute().body();
    }
}
