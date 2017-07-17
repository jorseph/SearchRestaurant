package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.currentplacedetailsonmap.data.LocationInfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowStoreActivity extends AppCompatActivity {
    private static final String TAG = ShowStoreActivity.class.getSimpleName();
    private Context mcontext;
    private ArrayList<LocationInfo> locationInfoList;
    private SelectiveViewPager mViewPager;
    private List<PageView> pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getApplication().getApplicationContext();
        setContentView(R.layout.activity_show_store);
        locationInfoList =  (ArrayList<LocationInfo>)getIntent().getSerializableExtra("LocationInfo");
        initData();
        initView();
        /*for(int i = 0; i < locationInfoList.size(); i++) {
            Log.v(TAG, "locationInfoList = " + locationInfoList.get(i).getName().toString());
            Log.v(TAG, "locationInfoList = " + locationInfoList.get(i).getRating());
        }*/
    }

    private void initView() {
        mViewPager  = (SelectiveViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new ShowStoreActivity.SamplePagerAdapter());
    }

    private void initData() {
        pageList = new ArrayList<>();
        for(LocationInfo mLocationInfo : locationInfoList) {
            pageList.add(new PageOneView(mcontext, mLocationInfo));
        }
    }


    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageList.get(position));
            return pageList.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
