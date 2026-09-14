package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.ScheduledActivityDetailDTO;

import java.io.IOException;

public class ScheduledActivityDetailTask extends AbstractTask<ScheduledActivityDetailDTO> {

    private final String environmentCode;
    private final String activityCode;

    public ScheduledActivityDetailTask(String environmentCode, String activityCode) {
        this.environmentCode = environmentCode;
        this.activityCode = activityCode;
        this.updateTitle("Loading activity details...");
    }

    @Override
    protected ScheduledActivityDetailDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Please wait...");
        return execute(getScheduledActivityApi()
            .getScheduledActivity(Constants.SUBSCRIPTION_CODE, environmentCode, activityCode));
    }
}
