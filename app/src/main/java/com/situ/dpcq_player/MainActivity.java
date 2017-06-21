package com.situ.dpcq_player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.situ.Contants.Utils;
import com.situ.com.dpcq_player.R;
import com.situ.db.DBHelper;
import com.situ.net.UrlGetUtil;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity {

    private boolean isFristStart = false;
    private TextView tv = null;
    private DBHelper dbHelper;
    private StringBuffer buffer = new StringBuffer();
    public Handler mHandler;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.tv);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv.setText("----");
    }
    public void getAllUrl(int n){
        for(int i=1;i<=n;i++){
            getStr(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isFristStart){
            mHandler = new MainActivity.ActivityHandler(this);
            dbHelper = DBHelper.getInstance(this);
//            getAllUrl();
            UrlGetUtil.GetCountByZero(mHandler);
            isFristStart=!isFristStart;
        }
    }

    public void getStr(int id){
        UrlGetUtil.GetUrlById(id,mHandler);
    }
    public void handleMessage(Message msg){
        switch (msg.what){
            case Utils.MSG_GETURL_SUCCESS:
                if(msg!=null&&msg.obj!=null){
                    count++;
                    dbHelper.addOrUpdateURLData(msg.arg1+"",(String)msg.obj);
                    if(count>=1029){
                        tv.setText(dbHelper.getURLData());
                    }

                }
                break;
            case Utils.MSG_GETCOUNT_SUCCESS:
                getAllUrl(msg.arg1);
                break;
        }

    }
    private static class ActivityHandler extends Handler{
        WeakReference<MainActivity> mRefActivity;
        MainActivity activity;
        private ActivityHandler(MainActivity activity){
            this.mRefActivity = new WeakReference<MainActivity>(activity);
            if(mRefActivity != null) {
                this.activity = (MainActivity)this.mRefActivity.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            if(activity != null){
                activity.handleMessage(msg);
            }
        }
    }
}
