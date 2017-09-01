package com.example.currentplacedetailsonmap.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.data.LocationInfo;
import com.example.currentplacedetailsonmap.util.ConfigUtil;
import com.example.currentplacedetailsonmap.util.DetailJSONParser;
import com.example.currentplacedetailsonmap.util.PlaceJSONParser;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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


/**
 * Created by jorseph on 2017/7/31.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private static final String TAG = DataAdapter.class.getSimpleName();
    private ArrayList<LocationInfo> mData;
    private Context mContext;
    private int view_position = 0;
    private DataAdapter.ViewHolder holder;

    public DataAdapter(Context context,ArrayList<LocationInfo> data){
        mContext = context;
        mData = data;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        //Picasso.with(mContext).load(mData.get(position).getPhoto()).into(holder.store_photo);
        //view_position = position;
        StoreDetail(mData.get(view_position).getPlaceid());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                // You can pass your own memory cache implementation
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(15)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage(mData.get(position).getPhoto(),holder.store_photo, options);

        final LocationInfo mlocationinfo = mData.get(position);
        holder.store_name.setText(mData.get(position).getName());
        holder.store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startmaps = "geo:" + mlocationinfo.getLat() + "," + mlocationinfo.getLng() + "?q=" + mlocationinfo.getName();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(startmaps));
                mContext.startActivity(intent);
            }
        });
        holder.store_phone.setText(mData.get(position).getPhone());

        //StoreDetail(mData.get(position).getPlaceid());
        //holder.store_phone.setText()
        //holder.store_phone.setText(phoneList.get(position));

    }

    /**
     * A class, to download Google Places
     */
    private class DetailsTask extends AsyncTask<String, Integer, String> {

        private DataAdapter context = null;
        String data = null;

        public DetailsTask(DataAdapter context) {
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
            DataAdapter.ParserDetailTask parserTask = new DataAdapter.ParserDetailTask();
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
            if(view_position < (mData.size()-1)) {
                mData.get(view_position).setPhone(phone);
                view_position++;
                StoreDetail(mData.get(view_position).getPlaceid());
            } else {
                for(int i = 0; i < mData.size(); i++) {

                }
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
            DataAdapter.DetailsTask placesTask = new DataAdapter.DetailsTask(DataAdapter.this);
            Log.v(TAG, sb.toString());
            placesTask.execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(ConfigUtil.TAG, "Exception:" + e);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView store_photo;
        public TextView store_name;
        public TextView store_price;
        public TextView store_phone;

        public ViewHolder(View view) {
            super(view);
            store_photo = (ImageView) view.findViewById(R.id.img);
            store_name = (TextView) view.findViewById(R.id.info_text);
            store_price = (TextView) view.findViewById(R.id.price_text);
            store_phone = (TextView) view.findViewById(R.id.phone_text);
        }
    }
}
