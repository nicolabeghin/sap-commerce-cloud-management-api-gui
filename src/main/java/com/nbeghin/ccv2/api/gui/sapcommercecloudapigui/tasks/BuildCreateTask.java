package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.CreateBuildRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateBuildResponseDTO;

import java.io.IOException;


public class BuildCreateTask extends AbstractTask<CreateBuildResponseDTO> {

    private final CreateBuildRequestDTO createBuildRequestDTO;

    public BuildCreateTask(CreateBuildRequestDTO createBuildRequestDTO) {
        this.createBuildRequestDTO = createBuildRequestDTO;
    }

    @Override
    protected CreateBuildResponseDTO call() throws IOException {
        updateProgress(0, 100);
        updateTitle("Build request");
        updateMessage("Entering build request...");
        CreateBuildResponseDTO createBuildResponseDTO = getBuildApi().createBuild(createBuildRequestDTO, Constants.SUBSCRIPTION_CODE).execute().body();
        updateProgress(100, 100);
        updateMessage("Build request accepted - " + createBuildResponseDTO.getCode());
        return createBuildResponseDTO;
    }
}
