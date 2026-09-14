package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.ScheduledActivityDetailDTO;

import java.io.IOException;

public class ScheduledActivityCancelTask extends AbstractTask<ScheduledActivityDetailDTO> {

    private final String environmentCode;
    private final String activityCode;

    public ScheduledActivityCancelTask(String environmentCode, String activityCode) {
        this.environmentCode = environmentCode;
        this.activityCode = activityCode;
        this.updateTitle("Cancelling scheduled activity...");
    }

    @Override
    protected ScheduledActivityDetailDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Cancelling activity " + activityCode + "...");
        ScheduledActivityDetailDTO result = execute(getScheduledActivityApi()
            .cancelScheduledActivity(Constants.SUBSCRIPTION_CODE, environmentCode, activityCode));
        updateProgress(100, 100);
        return result;
    }
}
