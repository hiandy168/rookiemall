package com.itheima.rookiemall.bean;

/**
 * Created by lyl on 2016/10/14.
 */

public class SubmitOrderBean {
    public int status;
    public String message;
    public OrderDataBean data;
    public static class OrderDataBean{
        public String orderNum;

        public Charge charge;
    }
}
