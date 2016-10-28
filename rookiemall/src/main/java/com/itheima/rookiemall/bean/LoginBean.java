package com.itheima.rookiemall.bean;

/**
 * Created by lyl on 2016/10/11.
 */

public class LoginBean extends BaseBean{
    public String token;
    public Data data;

    public static class Data{
        public int id;
        public String email;
        public String logo_url;
        public String username;
        public String mobi;

        @Override
        public String toString() {
            return "data:{" +
                    "id:" + id +
                    ", email:'" + email + '\'' +
                    ", logo_url:'" + logo_url + '\'' +
                    ", username:'" + username + '\'' +
                    ", mobi:'" + mobi + '\'' +
                    '}';
        }
    }
}
