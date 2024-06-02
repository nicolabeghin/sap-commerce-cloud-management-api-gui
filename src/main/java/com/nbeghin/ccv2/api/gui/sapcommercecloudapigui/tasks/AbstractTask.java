package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.App;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.okhttp.OkHttpLoggingInterceptor;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.okhttp.OkHttpTimingLoggingInterceptor;
import com.sap.cx.commercecloud.management.openapi.ApiClient;
import com.sap.cx.commercecloud.management.openapi.api.BuildApi;
import com.sap.cx.commercecloud.management.openapi.api.DeploymentApi;
import com.sap.cx.commercecloud.management.openapi.api.EnvironmentApi;
import javafx.concurrent.Task;

public abstract class AbstractTask<T> extends Task<T> {

    private final ApiClient apiClient;
    private final BuildApi buildApi;
    private final EnvironmentApi environmentApi;
    private final DeploymentApi deploymentApi;

    public AbstractTask() {
        this.apiClient = new ApiClient("OAuth2");
        apiClient.setAccessToken(Constants.ACCESS_TOKEN);
        if (Constants.DEBUG_ENABLED) {
            apiClient.getOkBuilder().addInterceptor(new OkHttpLoggingInterceptor());
        }
        apiClient.getOkBuilder().addInterceptor(new OkHttpTimingLoggingInterceptor());
        buildApi = this.apiClient.createService(BuildApi.class);
        environmentApi = this.apiClient.createService(EnvironmentApi.class);
        deploymentApi = this.apiClient.createService(DeploymentApi.class);
    }

    protected BuildApi getBuildApi() {
        return this.buildApi;
    }

    protected EnvironmentApi getEnvironmentApi() {
        return this.environmentApi;
    }

    protected DeploymentApi getDeploymentApi() {
        return this.deploymentApi;
    }

    @Override
    protected void updateMessage(String s) {
        App.LOG.info(s);
        super.updateMessage(s);
    }
}
