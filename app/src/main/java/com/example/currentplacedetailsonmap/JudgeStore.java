package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.currentplacedetailsonmap.data.LocationInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.currentplacedetailsonmap.util.ConfigUtil.TAG;

/**
 * Created by jorseph on 2017/5/29.
 */

public class JudgeStore {
    private SQLiteDatabase store_db;

    public JudgeStore(SQLiteDatabase DB) {
        store_db = DB;
    }

    public JudgeStore() {    }

    public LocationInfo SuggestStore() {
        String nowTime = getTime();
        Log.v(TAG,nowTime);
        return null;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //sdf.applyPattern("MM/dd/yyyy") ;
        //try {
            Date dt=new Date();
            //Date d = sdf.parse("01/17/2013") ;
            String dts=sdf.format(dt);
        //定義要取的內容
        SimpleDateFormat sdf5 = new SimpleDateFormat("E");//星期
        SimpleDateFormat sdf6 = new SimpleDateFormat("a");//時段

        //取出
        String day=sdf5.format(dt);//星期
        String td=sdf6.format(dt);
        Log.v(TAG, dts);
        Log.v(TAG, day);
        Log.v(TAG, td);
            return dts;
        //} catch (ParseException e) {
        //    e.printStackTrace();
        //    return null;
        //c}
    }
}
