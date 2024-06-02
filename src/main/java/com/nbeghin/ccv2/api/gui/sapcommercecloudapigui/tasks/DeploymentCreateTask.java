package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.CreateDeploymentRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateDeploymentResponseDTO;

import java.io.IOException;


public class DeploymentCreateTask extends AbstractTask<CreateDeploymentResponseDTO> {

    private final CreateDeploymentRequestDTO createDeploymentRequestDTO;

    public DeploymentCreateTask(CreateDeploymentRequestDTO createDeploymentRequestDTO) {
        this.createDeploymentRequestDTO = createDeploymentRequestDTO;
    }

    @Override
    protected CreateDeploymentResponseDTO call() throws IOException {
        updateProgress(0, 100);
        updateTitle("Deployment request");
        updateMessage("Entering deployment request...");
        CreateDeploymentResponseDTO createDeploymentResponseDTO = getDeploymentApi().createDeployment(createDeploymentRequestDTO, Constants.SUBSCRIPTION_CODE).execute().body();
        updateProgress(100, 100);
        updateMessage("Deployment request accepted - " + createDeploymentResponseDTO.getCode());
        return createDeploymentResponseDTO;
    }
}
