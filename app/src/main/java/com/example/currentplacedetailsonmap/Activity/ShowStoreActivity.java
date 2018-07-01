package com.example.currentplacedetailsonmap.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.data.LocationInfo;
import com.example.currentplacedetailsonmap.util.ConfigUtil;
import com.example.currentplacedetailsonmap.util.DetailJSONParser;
import com.example.currentplacedetailsonmap.util.PlaceJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ShowStoreActivity extends AppCompatActivity {
    private static final String TAG = ShowStoreActivity.class.getSimpleName();
    private Context mcontext;
    private ArrayList<LocationInfo> locationInfoList;
    private int view_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getApplication().getApplicationContext();
        setContentView(R.layout.activity_show_store);
        locationInfoList =  (ArrayList<LocationInfo>)getIntent().getSerializableExtra("LocationInfo");
        StoreDetail(locationInfoList.get(view_position).getPlaceid());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * A class, to download Google Places
     */
    private class DetailsTask extends AsyncTask<String, Integer, String> {

        private ShowStoreActivity context = null;
        String data = null;

        public DetailsTask(ShowStoreActivity context) {
            this.context = context;
        }



        @Override
        protected String doInBackground(String... url) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Log.d(TAG,"error = " + e.toString());
            }
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            ShowStoreActivity.ParserDetailTask parserTask = new ShowStoreActivity.ParserDetailTask();
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserDetailTask extends
            AsyncTask<String, Integer, HashMap<String, String>> {

        JSONObject jObject;

        @Override
        protected HashMap<String, String> doInBackground(
                String... jsonData) {

            HashMap<String, String> details = null;
            DetailJSONParser detailJsonParser = new DetailJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.v(TAG,jObject.toString());
                details = detailJsonParser.parse(jObject);
                //page_token = placeJsonParser.getPageToken(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return details;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> list) {
            //for (int i = 0; i < list.size(); i++) {
            HashMap<String, String> hmPlace = list;
            String phone = hmPlace.get("phone");
            Log.v(TAG,"phone = " + phone);
            if(view_position == 2) {
                DataAdapter adapter = new DataAdapter(getApplicationContext(),locationInfoList);
                RecyclerView mList = (RecyclerView) findViewById(R.id.list_view);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mList.setLayoutManager(layoutManager);
                mList.setAdapter(adapter);
            }

            locationInfoList.get(view_position).setPhone(phone);
            Log.v(TAG, "view_positin is " + view_position);
            if(view_position < (locationInfoList.size() - 1)) {
                view_position++;
                StoreDetail(locationInfoList.get(view_position).getPlaceid());
            }
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void StoreDetail(String placeid) {
        try {
            String unitStr = URLEncoder.encode(placeid, "utf8");  //字體要utf8編碼
            StringBuilder sb = new StringBuilder(ConfigUtil.GOOGLE_DETAIL_API);
            sb.append("?placeid=" + placeid);
            sb.append("&key=" + ConfigUtil.API_KEY_GOOGLE_MAP);  //server key
            ShowStoreActivity.DetailsTask placesTask = new ShowStoreActivity.DetailsTask(ShowStoreActivity.this);
            Log.v(TAG, sb.toString());
            placesTask.execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(ConfigUtil.TAG, "Exception:" + e);
        }
    }
}
