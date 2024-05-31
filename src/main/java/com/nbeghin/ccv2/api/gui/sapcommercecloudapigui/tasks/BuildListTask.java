package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.BuildDetailsDTO;

import java.io.IOException;
import java.util.ArrayList;

public class BuildListTask extends AbstractTask<BuildDetailsDTO> {

    public BuildListTask() {
    }

    @Override
    protected BuildDetailsDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Loading latest builds...");
        return getBuildApi().getBuilds(Constants.SUBSCRIPTION_CODE, new ArrayList<>(), Constants.MAX_NUM_BUILDS, 0, "buildStartTimestamp desc", false).execute().body();
    }
}
