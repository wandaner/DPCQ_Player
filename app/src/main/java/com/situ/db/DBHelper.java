package com.situ.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.situ.com.dpcq_player.R;


/**
 * Created by rrdev on 2017/6/20.
 */

public class DBHelper extends SQLiteOpenHelper {
    private Context context;
    private static DBHelper mInstance=null;
    private SQLiteDatabase db;

    private static final String DBNAME = "dpcq_player.db";
    private static final int VERSION = 1;

    private DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DBNAME, factory, VERSION);
    }

    public static DBHelper getInstance(Context context){
        if(mInstance==null){
            mInstance = new DBHelper(context,null);
        }
        mInstance.context = context;
        return mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Toast.makeText(context,"111",Toast.LENGTH_LONG).show();
        String [] tables = context.getResources().getStringArray(R.array.sql_ct);
        for (int i = 0; i < tables.length; i++) {
            Log.e("xukai",tables[i]);
            db.execSQL(tables[i]);
        }
//        Log.e("xukai","onCreate");
//        Toast.makeText(context,"111",Toast.LENGTH_LONG).show();
//        db.execSQL("create table if not exists UrlData(id integer PRIMARY KEY, url text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 添加或者更新url缓存
     * @param id 剧集id
     * @param url 剧集url
     */
    public synchronized void addOrUpdateURLData(String id,String url){
        boolean isExists = checkURLData(url);
        try {
            db = getWritableDatabase(); // 获得数据库写对象
            if(isExists){
                String Sql_update = context.getResources().getStringArray(R.array.sql_up)[0];
                db.execSQL(Sql_update,new String[]{url,id});
            }else{
                String Sql_insert = context.getResources().getStringArray(R.array.sql_in)[0];
                db.execSQL(Sql_insert,new String[]{id,url});
            }
        } catch (Exception e) {
//            Log.e("xukai","URLData-->:"+e.getMessage());
        } finally {
        }
    }

    /**
     * 判断表中是否存在此id记录
     * @param id 剧集id
     * @return false/true
     */
    public synchronized boolean checkURLData(String id){
        Cursor cursor = null;
        String Sql_select = context.getResources().getStringArray(R.array.sql_sl)[0];
        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(Sql_select, new String[]{id});
            while (cursor.moveToNext()) {
                cursor.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor!=null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * 获得url 缓存
     * @return
     */
    public String getURLData(){
        String result = "";
        Cursor cursor = null;
        String Sql_select = context.getResources().getStringArray(R.array.sql_sl)[1];
        try {
            db = getReadableDatabase(); // 获得数据库读对象
            cursor = db.rawQuery(Sql_select, new String[]{});
            while (cursor.moveToNext()) {
                String id =cursor.getString(0);
                String url =cursor.getString(1);
                result+="->"+id+"\t:"+url+"\r\n";
            }
            return result;
        } catch (Exception e) {
            Log.e("Log",e.getMessage());
            return e.getMessage();
        } finally {
            if(cursor!=null){
                cursor.close();
            }
        }
//        return null;
    }
}
