package com.itheima.rookiemall.bean;

import java.util.List;

/**
 * Created by lyl on 2016/10/3.
 */

public class HomeHotBean extends BasePageBean<HomeHotBean.ItemHomeHotBean> {
    public List<ItemHomeHotBean> list;

    @Override
    public List<HomeHotBean.ItemHomeHotBean> getItemDatas() {
        return list;
    }


    public static class ItemHomeHotBean {
        public int id;
        public String name;
        public String imgUrl;
        public String description;
        public double price;
        public int sale;
    }
}
