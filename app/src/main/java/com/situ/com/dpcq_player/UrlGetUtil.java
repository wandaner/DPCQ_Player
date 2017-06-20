package com.situ.com.dpcq_player;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rrdev on 2017/6/6.
 */

public class UrlGetUtil {
    private static final String regex ="FonHen_JieMa\\(.*\\).split\\(\\'&\\'\\)";
    private static String Url = "http://www.ting56.com/video/1758-0-";
    private static String Suffix = ".html";
    public static void GetHtmlInfo(final int id,final ICallBack iCallBack){
        if(id<=0){
            iCallBack.failed("id 不可以小于0");
            return;
        }
        final Pattern mPattern = Pattern.compile(regex);
        final Handler mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj!=null) {
                    String reslStr = "";
                    String tempStr = (String)msg.obj;
                    Matcher mMatcher = mPattern.matcher(tempStr);
                    while (mMatcher.find()) {
                        reslStr = mMatcher.group(0);
                    }
                    //解析加密串，获取数据
                    reslStr = reslStr.replace("FonHen_JieMa('*","");
                    reslStr = reslStr.replace("').split('&')","");
                    iCallBack.success(id,Union2String(reslStr));
                }else{
                    iCallBack.failed("网络获取数据失败");
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                String result = "";
                try {
                    URL url = new URL(Url+(id-1)+Suffix);
                    InputStream in =url.openStream();
                    InputStreamReader isr = new InputStreamReader(in);
                    BufferedReader buff = new BufferedReader(isr);
                    String str;
                    while ((str = buff.readLine()) != null) {
                        result+=str;
                    }
                    Message msg = new Message();
                    msg.obj=result;
                    mhandler.sendMessage(msg);
                    buff.close();
                    isr.close();
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();



    }
//    public static String String2Union(){
//        String a = "http://wting.info:81/asdb/fiction/xuanhuan/doupocq/jlgfimhx.mp3&1129&mp3";
//        String result="";
//        char[] b = a.toCharArray();
//        for(int i=0;i<b.length;i++) {
//            result+=(byte)b[i]+",";
//            Log.e("hello-->", "," +(byte)b[i]);
//        }
//        return result;
//    }
    public static String  Union2String(String str){
        StringBuffer sbu = new StringBuffer();
        String temp[] = str.split("[*]");
        for(int i=0;i<temp.length;i++) {
            if(!TextUtils.isEmpty(temp[i])){
                sbu.append((char) Integer.parseInt(temp[i]));
            }
        }
        return sbu.toString();//.split("&")[0];
    }
}
