package com.itheima.rookiemall.config;

/**
 * Created by lyl on 2016/10/3.
 */

public class Urls {
    private static final String baseUrl = "http://112.124.22.238:8081/course_api/";

    //主页banner接口
    public static final String api_homepage_banner = "banner/query";
    //主页->首页热门活动
    public static final String api_homepage_lists = "campaign/recommend";
    //home 热卖
    public static final String api_homehot_lists = "wares/hot";
    //home 分类->商品一级分类
    public static final String api_hometype_left_lists = "category/list";
    //home 分类->获取分类下的商品
    public static final String api_hometype_right_lists = "wares/list";

    //home 详情页面
    public static final String api_home_details = "wares/campaign/list";

    //webView详情页面地址
    public static final String api_details_webview = "wares/detail.html";


    public static final String api_login = "auth/login";
    //注册
    public static final String api_register = "auth/reg";

    public static final String api_cretae_order = "order/create";
    public static final String api__order_list = "order/list";


    public static final String api_add_address = "addr/create";

    public static final String api_address_list = "addr/list";

    public static final String api_del_address = "addr/del";



    public static String getBaseUrl() {
        return baseUrl;
    }
}
