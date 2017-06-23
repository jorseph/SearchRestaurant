package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.currentplacedetailsonmap.data.LocationInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.currentplacedetailsonmap.util.ConfigUtil.TAG;

/**
 * Created by jorseph on 2017/5/29.
 */

public class JudgeStore {
    private List<LocationInfo> locationInfoList;
    private int show_index = -1;

    public JudgeStore(List<LocationInfo> locationInfoList) {
        this.locationInfoList = locationInfoList;
    }

    public LocationInfo SuggestStore() {
        String nowTime = getTime();
        Log.v(TAG,nowTime);
        return null;
    }

    public LocationInfo GetNextStore() {
        show_index++;
        return JudgeShowSuggestStore();
    }

    private boolean ShowSuggestStore(int selected_index) {
        if (locationInfoList.get(selected_index).getOpen()) {
            return true;
        } else {
            return false;
        }
    }

    private LocationInfo JudgeShowSuggestStore() {
        final int index_max = locationInfoList.size();
        while (!ShowSuggestStore(show_index)) {
            Log.v(TAG,"show_index = " + show_index);
            if((show_index + 1) >= index_max) {
                show_index = -1;
            }
            show_index++;
        }
        return locationInfoList.get(show_index);
    }

    private String getTime() {
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
