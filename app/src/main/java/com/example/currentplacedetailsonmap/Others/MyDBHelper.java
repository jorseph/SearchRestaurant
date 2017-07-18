package com.example.currentplacedetailsonmap.Others;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jorseph on 2017/5/29.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "mydata.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static int VERSION = 1;
    // 資料庫物件，固定的欄位變數
    private static SQLiteDatabase database;
    //context為當前activity的上下文,name為要操作的資料庫名稱,factory用來做深入查詢用,version為版本
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }
    //只有在資料庫不存在時,才會呼叫onCreate()建立資料庫
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // 建立要存放資料的資料表格
        // 1. SQL語法不分大小寫
        // 2. 這裡大寫代表的是SQL標準語法, 小寫字是資料表/欄位re命名
        // 建立應用程式需要的表格
        db.execSQL("CREATE  TABLE main.exp " +
                "(_id INTEGER PRIMARY KEY  NOT NULL , " +
                "cdate DATETIME NOT NULL , " +
                "storeinfo VARCHAR, " +
                "score INTEGER)");
    }
    //使用建構子時如果版本增加,便會呼叫onUpgrade()刪除舊的資料表與其內容,再重新呼叫onCreate()建立新的資料表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // 刪除原有的表格
        //db.execSQL("DROP TABLE IF EXISTS " + "mydata");
        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new MyDBHelper(context, DATABASE_NAME, null, VERSION).getWritableDatabase();
        }
        return database;
    }
}
