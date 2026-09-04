package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.EndpointDetailsDTO;

import java.io.IOException;

public class EndpointListTask extends AbstractTask<EndpointDetailsDTO> {

    private final String environmentCode;

    public EndpointListTask(String environmentCode) {
        this.environmentCode = environmentCode;
    }

    @Override
    protected EndpointDetailsDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage("Loading endpoints...");
        return execute(getEndpointApi().getEndpoints(Constants.SUBSCRIPTION_CODE, environmentCode, null, null));
    }
}
