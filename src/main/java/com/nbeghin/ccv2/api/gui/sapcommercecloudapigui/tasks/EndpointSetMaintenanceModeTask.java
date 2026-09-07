package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.sap.cx.commercecloud.management.openapi.model.EndpointDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Enables or disables maintenance mode on a single endpoint via a partial endpoint update. */
public class EndpointSetMaintenanceModeTask extends AbstractTask<EndpointDTO> {

    private final String environmentCode;
    private final String endpointCode;
    private final boolean enable;

    public EndpointSetMaintenanceModeTask(String environmentCode, String endpointCode, boolean enable) {
        this.environmentCode = environmentCode;
        this.endpointCode = endpointCode;
        this.enable = enable;
    }

    @Override
    protected EndpointDTO call() throws IOException {
        updateProgress(0, 100);
        updateMessage((enable ? "Enabling" : "Disabling") + " maintenance mode on endpoint " + endpointCode + "...");
        Map<String, Object> body = new HashMap<>();
        body.put("maintenanceMode", enable);
        return execute(getEndpointApi().updateEndpoint(body, Constants.SUBSCRIPTION_CODE, environmentCode, endpointCode));
    }
}
