package com.itheima.rookiemall.bean;

import java.util.List;

/**
 * Created by lyl on 2016/10/15.
 */

public class MyOrderBean {
    public int id;
    public String orderNum;
    public long createdTime;
    public int amount;
    public int status;
    public List<Items> items;
    public String address;

    public static class Items {
        public int id;
        public float amount;
        public Wares wares;

        public static class Wares {
            public int id;
            public String name;
            public String imgUrl;
            public String description;
            public float price;
            public int sale;
        }
    }

}
