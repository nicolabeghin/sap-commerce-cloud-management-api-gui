package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.CreateBuildRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.CreateBuildResponseDTO;

import java.io.IOException;


/** Triggers a new build for the given branch/name and returns the created build's code. */
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
        CreateBuildResponseDTO createBuildResponseDTO = execute(getBuildApi().createBuild(createBuildRequestDTO, Constants.SUBSCRIPTION_CODE));
        updateProgress(100, 100);
        updateMessage("Build request accepted - " + createBuildResponseDTO.getCode());
        return createBuildResponseDTO;
    }
}
