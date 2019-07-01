package com.jianwuch.giaifu.net;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jianwuch.giaifu.ThisApplication;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author jove.chen
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class RetrofitInstance {
    private Retrofit retrofit;
    private static OkHttpClient okHttpClient = null;
    private static final String HOST = "https://www.soogif.com/";
    private static final RetrofitInstance ourInstance = new RetrofitInstance();

    public static RetrofitInstance getInstance() {
        return ourInstance;
    }

    private RetrofitInstance() {
    }

    public void init() {
        initOKhttp();
        retrofit = new Retrofit.Builder().baseUrl(HOST)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    private void initOKhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(ThisApplication.getApplication().getExternalCacheDir(), "geAiFu");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        //Interceptor cacheInterceptor = new Interceptor() {
        //    @Override
        //    public Response intercept(Chain chain) throws IOException {
        //        Request request = chain.request();
        //        if (!SystemUtil.isNetworkConnected()) {
        //            request = request.newBuilder()
        //                    .cacheControl(CacheControl.FORCE_CACHE)
        //                    .build();
        //        }
        //        Response response = chain.proceed(request);
        //        if (SystemUtil.isNetworkConnected()) {
        //            int maxAge = 0;
        //            // 有网络时, 不缓存, 最大保存时长为0
        //            response.newBuilder()
        //                    .header("Cache-Control", "public, max-age=" + maxAge)
        //                    .removeHeader("Pragma")
        //                    .build();
        //        } else {
        //            // 无网络时，设置超时为4周
        //            int maxStale = 60 * 60 * 24 * 28;
        //            response.newBuilder()
        //                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
        //                    .removeHeader("Pragma")
        //                    .build();
        //        }
        //        return response;
        //    }
        //};
        //        Interceptor apikey = new Interceptor() {
        //            @Override
        //            public Response intercept(Chain chain) throws IOException {
        //                Request request = chain.request();
        //                request = request.newBuilder()
        //                        .addHeader("apikey",Constants.KEY_API)
        //                        .build();
        //                return chain.proceed(request);
        //            }
        //        };
        //        builder.addInterceptor(apikey);
        //设置缓存
        //builder.addNetworkInterceptor(cacheInterceptor);
        //builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }
}
