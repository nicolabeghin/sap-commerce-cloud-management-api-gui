package com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.utils.okhttp;

import com.nbeghin.ccv2.api.gui.sapcommercecloudapigui.App;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpTimingLoggingInterceptor implements Interceptor {

    public OkHttpTimingLoggingInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        App.LOG.info(String.format("Received response for %s in %.1fms", response.request().url(), (t2 - t1) / 1e6d));
        return response;
    }
}
