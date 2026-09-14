package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.CreateScheduledActivityRequestDTO;
import com.sap.cx.commercecloud.management.openapi.model.ScheduledActivityDetailDTO;

import java.io.IOException;

public class ScheduledActivityCreateTask extends AbstractTask<ScheduledActivityDetailDTO> {

    private final String environmentCode;
    private final CreateScheduledActivityRequestDTO requestDTO;

    public ScheduledActivityCreateTask(String environmentCode,
                                       CreateScheduledActivityRequestDTO requestDTO) {
        this.environmentCode = environmentCode;
        this.requestDTO = requestDTO;
        this.updateTitle("Creating scheduled activity...");
    }

    @Override
    protected ScheduledActivityDetailDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Submitting scheduled activity...");
        ScheduledActivityDetailDTO result = execute(getScheduledActivityApi()
            .createScheduledActivity(requestDTO, Constants.SUBSCRIPTION_CODE, environmentCode));
        updateProgress(100, 100);
        updateMessage("Scheduled activity created: " + result.getCode());
        return result;
    }
}
