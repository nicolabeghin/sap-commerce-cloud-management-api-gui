package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.EnvironmentDetailsDTO;

import java.io.IOException;

public class EnvironmentListTask extends AbstractTask<EnvironmentDetailsDTO> {

    public EnvironmentListTask() {
    }

    @Override
    protected EnvironmentDetailsDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Loading environments...");
        return getEnvironmentApi().getEnvironments(Constants.SUBSCRIPTION_CODE, null, null).execute().body();
    }
}
