package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.DeploymentDetailDTO;

import java.io.IOException;


public class DeploymentDetailTask extends AbstractTask<DeploymentDetailDTO> {

    private final String deploymentCode;

    public DeploymentDetailTask(String deploymentCode) {
        this.deploymentCode = deploymentCode;
    }

    @Override
    protected DeploymentDetailDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Please wait...");
        return getDeploymentApi().getDeployment(Constants.SUBSCRIPTION_CODE, this.deploymentCode).execute().body();
    }
}
