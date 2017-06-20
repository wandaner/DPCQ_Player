package com.situ.com.dpcq_player;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private TextView tv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.tv);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
//        Log.e("xukai",UrlGetUtil.Union2String("*104*116*116*112*58*47*47*119*116*105*110*103*46*105*110*102*111*58*56*49*47*97*115*100*98*47*102*105*99*116*105*111*110*47*120*117*97*110*104*117*97*110*47*100*111*117*112*111*99*113*47*55*117*49*48*56*52*52*120*46*109*112*51*38*49*49*50*57*38*109*112*51"));
//        tv.setText(UrlGetUtil.Union2String("*104*116*116*112*58*47*47*119*116*105*110*103*46*105*110*102*111*58*56*49*47*97*115*100*98*47*102*105*99*116*105*111*110*47*120*117*97*110*104*117*97*110*47*100*111*117*112*111*99*113*47*55*117*49*48*56*52*52*120*46*109*112*51*38*49*49*50*57*38*109*112*51"));
        initSQLite();


    }
    public void getAllUrl(){
//        SQLiteDatabase db = openOrCreateDatabase("basic.db", Context.MODE_PRIVATE, null);
        for(int i=1;i<10;i++){
            getStr(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllUrl();
    }

    public void getStr(int id){
        UrlGetUtil.GetHtmlInfo(id,new ICallBack() {
            @Override
            public void success(int id,String str) {
//                tv.setText(str+"");
//                db.execSQL("INSERT INTO table_url VALUES (?, ?)", new Object[]{id, str});
                  Log.e("xu_kai",id+": "+str);
            }

            @Override
            public void failed(String msg) {
                tv.setText(msg);
            }

        });
    }
    public void initSQLite(){
        SQLiteDatabase db = openOrCreateDatabase("basic.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS table_url (id INTEGER PRIMARY KEY, url VARCHAR)");
        db.close();
    }
    public void insert(int id,String url){
        SQLiteDatabase db = openOrCreateDatabase("basic.db", Context.MODE_PRIVATE, null);
        db.execSQL("INSERT INTO table_url VALUES (?, ?)", new Object[]{id, url});
        db.close();
    }
    
}
