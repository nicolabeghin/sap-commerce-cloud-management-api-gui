package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.ScheduledActivityDetailsDTO;

import java.io.IOException;

public class ScheduledActivityListTask extends AbstractTask<ScheduledActivityDetailsDTO> {

    private final String environmentCode;

    public ScheduledActivityListTask(String environmentCode) {
        this.environmentCode = environmentCode;
        this.updateTitle("Loading scheduled activities...");
    }

    @Override
    protected ScheduledActivityDetailsDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Loading scheduled activities...");
        return execute(getScheduledActivityApi()
            .getScheduledActivities(Constants.SUBSCRIPTION_CODE, environmentCode,
                null, null, null, null, null));
    }
}
