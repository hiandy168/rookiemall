package com.itheima.rookiemall.call;

import com.itheima.rookiemall.utils.L;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by lyl on 2016/10/3.
 */

public abstract class HttpResponseCall<T> {

    public Type getType() {
        Type mySuperClass = getClazz().getGenericSuperclass();
        Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
        return type;
    }

    public Class getClazz() {
        return getClass();
    }

    public abstract void onResponse(T t);

    public void onFailure(Call<ResponseBody> call, Throwable e) {
        L.e(e);
    }
}
