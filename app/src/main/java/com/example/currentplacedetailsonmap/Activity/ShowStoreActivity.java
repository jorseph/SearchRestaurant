package com.example.currentplacedetailsonmap.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.data.LocationInfo;
import java.util.ArrayList;

public class ShowStoreActivity extends AppCompatActivity {
    private static final String TAG = ShowStoreActivity.class.getSimpleName();
    private Context mcontext;
    private ArrayList<LocationInfo> locationInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getApplication().getApplicationContext();
        setContentView(R.layout.activity_show_store);
        locationInfoList =  (ArrayList<LocationInfo>)getIntent().getSerializableExtra("LocationInfo");

        DataAdapter adapter = new DataAdapter(getApplicationContext(),locationInfoList);
        RecyclerView mList = (RecyclerView) findViewById(R.id.list_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
