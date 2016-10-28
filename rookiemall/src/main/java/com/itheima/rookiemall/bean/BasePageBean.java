package com.itheima.rookiemall.bean;

import java.util.List;

/**
 * 分页的基类bean
 * Created by lyl on 2016/10/7.
 */

public abstract class BasePageBean<ItemBean> {
    public int totalCount;
    public int currentPage;
    public int totalPage;
    public int pageSize;

    public abstract List<ItemBean> getItemDatas();
}
