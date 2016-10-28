package com.itheima.rookiemall.bean;

/**
 * Created by lyl on 2016/10/3.
 */

public class HomeListsBean {
    public int id;
    public String title;

    public CpOne cpOne;
    public CpOne cpThree;
    public CpOne cpTwo;

    public static class CpOne {
        public int id;
        public String imgUrl;
        public String title;

    }
}
