package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.tasks;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.App;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.Constants;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.okhttp.OkHttpLoggingInterceptor;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.okhttp.OkHttpTimingLoggingInterceptor;
import com.sap.cx.commercecloud.management.openapi.ApiClient;
import com.sap.cx.commercecloud.management.openapi.api.BuildApi;
import com.sap.cx.commercecloud.management.openapi.api.DeploymentApi;
import com.sap.cx.commercecloud.management.openapi.api.EndpointApi;
import com.sap.cx.commercecloud.management.openapi.api.EnvironmentApi;
import com.sap.cx.commercecloud.management.openapi.api.ScheduledActivityApi;
import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.OAuthTokenFetcher;
import retrofit2.Call;
import retrofit2.Response;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Base class for every Management API call. Each task builds its own {@link ApiClient}
 * (with OkHttp timeouts, an auth-header interceptor, and optional debug logging) and the
 * Retrofit service interfaces. Subclasses implement {@link Task#call()} and use the
 * {@code get*Api()} accessors plus {@link #execute(Call)} to invoke the API.
 *
 * <p>Tasks run on daemon threads (see {@link #startDaemon}) so blocking API/poll calls
 * never freeze the JavaFX application thread.
 */
public abstract class AbstractTask<T> extends Task<T> {

    private final ApiClient apiClient;
    private final BuildApi buildApi;
    private final EnvironmentApi environmentApi;
    private final DeploymentApi deploymentApi;

    private final EndpointApi endpointApi;
    private final ScheduledActivityApi scheduledActivityApi;

    public AbstractTask() {
        this.apiClient = new ApiClient();
        apiClient.getAdapterBuilder().baseUrl(Constants.BASE_PATH);
        apiClient.getOkBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS);
        // The OAuth token is fetched lazily on the first request (see the interceptor below)
        // rather than here in the constructor: tasks are instantiated on the JavaFX
        // application thread, and fetching the token here would block the UI on a network
        // round-trip for every API action.
        apiClient.getOkBuilder().addInterceptor(chain ->
            chain.proceed(chain.request().newBuilder()
                .header("x-approuter-authorization", "Bearer " + getToken())
                .build())
        );
        if (Constants.DEBUG_ENABLED) {
            apiClient.getOkBuilder().addInterceptor(new OkHttpLoggingInterceptor());
        }
        apiClient.getOkBuilder().addInterceptor(new OkHttpTimingLoggingInterceptor());
        buildApi = this.apiClient.createService(BuildApi.class);
        environmentApi = this.apiClient.createService(EnvironmentApi.class);
        deploymentApi = this.apiClient.createService(DeploymentApi.class);
        endpointApi = this.apiClient.createService(EndpointApi.class);
        scheduledActivityApi = this.apiClient.createService(ScheduledActivityApi.class);
    }

    private volatile String token;

    // Double-checked locking: fetch the OAuth token at most once per task, lazily on
    // the first API call (off the FX thread). See the constructor comment for why.
    private String getToken() throws IOException {
        String t = token;
        if (t == null) {
            synchronized (this) {
                t = token;
                if (t == null) {
                    t = OAuthTokenFetcher.fetchBearerToken(Constants.CLIENT_ID, Constants.CLIENT_SECRET);
                    token = t;
                }
            }
        }
        return t;
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

    protected EndpointApi getEndpointApi() {
        return endpointApi;
    }

    protected ScheduledActivityApi getScheduledActivityApi() {
        return scheduledActivityApi;
    }

    @Override
    protected void updateMessage(String s) {
        App.LOG.info(s);
        super.updateMessage(s);
    }

    public static void startDaemon(javafx.concurrent.Task<?> task) {
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    // Unwrap a Retrofit response: return the body on 2xx, otherwise throw an IOException
    // carrying the status and error body (401 gets a hint that the token likely expired).
    protected <R> R execute(Call<R> call) throws IOException {
        Response<R> response = call.execute();
        if (!response.isSuccessful()) {
            String error = response.errorBody() != null ? response.errorBody().string() : "";
            if (response.code() == 401) {
                throw new IOException("HTTP 401 Unauthorized - API token may have expired. " + error);
            }
            throw new IOException("HTTP " + response.code() + " " + response.message() + ": " + error);
        }
        return response.body();
    }
}
