package com.situ.com.dpcq_player;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.situ.com.com.situ.com.Contants.Utils;

import java.io.BufferedReader;
import java.io.IOException;
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
    private static Pattern mPattern = Pattern.compile(regex);
    /**
     * 根据剧集id获取mp3的url
     * @param id 剧集id
     * @param handler 回调handler
     */
    public static void GetUrlById(final int id,final Handler handler){
        if(id<=0){
            return;
        }
        new Thread(){
            @Override
            public void run() {
                String result = HttpGetRequest(id);
                    if(!TextUtils.isEmpty(result)) {
                        String match = MatchResult(result);
                        if (!TextUtils.isEmpty(match)) {
                            String[] temp = match.split("&");
                            Message msg = new Message();
                            msg.what = Utils.MSG_GETURL_SUCCESS;
                            msg.arg1 = id;
                            msg.obj = temp[0];
                            handler.sendMessage(msg);
                        }
                    }
            }
        }.start();
    }
    public static void GetCountByZero(final Handler handler){
        new Thread(){
            @Override
            public void run() {
                String result = HttpGetRequest(1);
                if(!TextUtils.isEmpty(result)) {
                    String str = MatchResult(result);
                    if (!TextUtils.isEmpty(str)) {
                        String[] temp = str.split("&");
                        Message msg = new Message();
                        msg.what = Utils.MSG_GETCOUNT_SUCCESS;
                        msg.arg1 = Integer.parseInt(temp[1]);
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();
    }
    /**
     * httpGet 请求，获取网页信息
     * @param id
     * @return
     */
    private static String HttpGetRequest(int id){
        String result = "";
        InputStream in = null;
        InputStreamReader isr = null;
        BufferedReader buff = null;
        try {
            URL url = new URL(Url+(id-1)+Suffix);
            in =url.openStream();
            isr = new InputStreamReader(in);
            buff = new BufferedReader(isr);
            String str;
            while ((str = buff.readLine()) != null) {
                result+=str;
            }
        } catch (Exception e) {
            return null;
        }finally {
            try {
                if(buff!=null) {
                    buff.close();
                }
                if(isr!=null){
                    isr.close();
                }
                if(in!=null){
                    in.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }
    /**
     * 从网页字符串中解析出网址数据
     * @param str 网页数据
     * @return 解析出来的网址
     */
    public static String MatchResult(String str){
        String matchString = "";
        Matcher mMatcher = mPattern.matcher(str);
        while (mMatcher.find()) {
            matchString = mMatcher.group(0);
        }
        //解析加密串，获取数据
        matchString = matchString.replace("FonHen_JieMa('*","");
        matchString = matchString.replace("').split('&')","");
        if(TextUtils.isEmpty(matchString)){
            return null;
        }
        return Union2String(matchString);
    }

    /**
     * 将Union字符串转换成String
     * @param str Union字符串
     * @return String字符串
     */
    public static String Union2String(String str){
        StringBuffer sbu = new StringBuffer();
        String temp[] = str.split("[*]");
        for(int i=0;i<temp.length;i++) {
            if(!TextUtils.isEmpty(temp[i])){
                sbu.append((char) Integer.parseInt(temp[i]));
            }
        }
        return sbu.toString();
    }
    public static String GetUrlFromResult(){
        return "";
    }
    public static int GetCountFromResult(){
        return 1;
    }
}
