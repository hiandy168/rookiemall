package com.itheima.rookiemall.bean;

/**
 * Created by lyl on 2016/10/15.
 */

public class BaseBean {
    //1代表成功，非1代表失败
    public int status;
    //状态码说明
    public String message;

    public boolean isSuccess() {
        return status == 1;
    }
}
