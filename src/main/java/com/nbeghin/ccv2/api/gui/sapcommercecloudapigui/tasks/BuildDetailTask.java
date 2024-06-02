package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.BuildDetailDTO;

import java.io.IOException;


public class BuildDetailTask extends AbstractTask<BuildDetailDTO> {

    private final String buildCode;

    public BuildDetailTask(String buildCode) {
        this.buildCode = buildCode;
    }

    @Override
    protected BuildDetailDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Please wait...");
        return getBuildApi().getBuild(Constants.SUBSCRIPTION_CODE, this.buildCode).execute().body();
    }
}
