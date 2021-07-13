package com.wawa.wawaandroid_ep.bean.game;

/**
 * 作者：create by 张金 on 2021/7/13 12:01
 * 邮箱：564813746@qq.com
 */
public class SocketBaseBean<T> {
    private String id;
    private String method;
    private T params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
