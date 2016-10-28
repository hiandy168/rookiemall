package com.itheima.rookiemall.api;

import android.util.Log;

import com.itheima.rookiemall.call.HttpResponseCall;
import com.itheima.rookiemall.config.Urls;
import com.itheima.rookiemall.utils.GsonUtils;
import com.itheima.rookiemall.utils.L;
import com.itheima.rookiemall.utils.UIUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by lyl on 2016/10/3.
 */

public class HttpHelper {
    public static final int TOKEN_MISSING = 401;// token 丢失
    public static final int TOKEN_ERROR = 402; // token 错误
    public static final int TOKEN_EXPIRE = 403; // token 过期


    private volatile static WeakReference<HttpHelper> sInstance;
    private final Retrofit mRetrofit;

    private HttpHelper() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Urls.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HttpHelper getInstance() {
        if (sInstance == null || sInstance.get() == null) {
            synchronized (HttpHelper.class) {
                if (sInstance == null || sInstance.get() == null) {
                    sInstance = new WeakReference<HttpHelper>(new HttpHelper());
                }
            }
        }
        return sInstance.get();
    }


    public <T> Call get(String apiUrl, Map<String, Object> paramMap, final HttpResponseCall<T> httpResponseCall) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        HttpService httpService = mRetrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.get(apiUrl, paramMap);
        parseNetData(call, httpResponseCall);
        return call;
    }

    public <T> Call post(String apiUrl, Map<String, Object> paramMap, HttpResponseCall<T> httpResponseCall) {
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }
        HttpService httpService = mRetrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.post(apiUrl, paramMap);
        parseNetData(call, httpResponseCall);
        return call;
    }


    private static <T> void parseNetData(Call<ResponseBody> call, final HttpResponseCall<T> httpResponseCall) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    T t = GsonUtils.parseToBean(json, httpResponseCall.getType());
                    httpResponseCall.onResponse(t);
                } catch (Exception e) {
                    String message = "未知错误";
                    switch (response.code()) {
                        case TOKEN_MISSING:
                            message = "token 丢失";
                            break;
                        case TOKEN_ERROR:
                            message = "token 错误";
                            break;
                        case TOKEN_EXPIRE:
                            message = "token 过期";
                            break;
                    }
                    UIUtils.showDefToast(message);
                    L.e("HttpLelper onResponse:" + message);
                    httpResponseCall.onFailure(call, e);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                httpResponseCall.onFailure(call, t);
            }
        });
    }


    public static interface HttpService<T> {
        @GET
        public Call<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> param);

        @FormUrlEncoded
        @POST
        public Call<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> param);
    }
}
