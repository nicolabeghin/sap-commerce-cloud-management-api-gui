package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.okhttp;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.App;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpLoggingInterceptor implements Interceptor {

    public OkHttpLoggingInterceptor() {
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        App.LOG.info("Sending request " + request.url());
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        App.LOG.info(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    }
}
