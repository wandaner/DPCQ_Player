package com.situ.net;

/**
 * Created by rrdev on 2017/6/7.
 */

public interface ICallBack {
    public void success(int id, String str);
    public void failed(String msg);
}
