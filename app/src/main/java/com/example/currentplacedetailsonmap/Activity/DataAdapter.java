package com.example.currentplacedetailsonmap.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
    private final static String CALL = "android.intent.action.CALL";
    private final static String MAP = "android.content.Intent.ACTION_VIEW";
    private ArrayList<LocationInfo> mData;
    private Context mContext;

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
        //StoreDetail(mData.get(view_position).getPlaceid());
        Log.v(TAG, "position is " + position);
        Log.v(TAG, "phone is " + mData.get(position).getPhone());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                // You can pass your own memory cache implementation
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(15)) //rounded corner bitmap
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();


        final LocationInfo mlocationinfo = mData.get(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        String image_URI = mData.get(position).getPhoto();
        if(image_URI != null) {
            imageLoader.displayImage(mData.get(position).getPhoto(), holder.store_photo, options);
        } else {
            holder.store_photo.setImageResource(R.drawable.no_image_avaliable);
        }
        holder.store_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startmaps = "geo:" + mlocationinfo.getLat() + "," + mlocationinfo.getLng() + "?q=" + mlocationinfo.getName();
                Intent intent = new Intent(MAP, Uri.parse(startmaps));
                mContext.startActivity(intent);
            }
        });

        holder.store_name.setText(mData.get(position).getName());
        holder.store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startmaps = "geo:" + mlocationinfo.getLat() + "," + mlocationinfo.getLng() + "?q=" + mlocationinfo.getName();
                Intent intent = new Intent(MAP, Uri.parse(startmaps));
                mContext.startActivity(intent);
            }
        });
        String phone_number = mData.get(position).getPhone();
        if (phone_number.equals("-NA-")) {
            holder.store_phone.setText(mContext.getString(R.string.NoData));
        } else {
            holder.store_phone.setText(mData.get(position).getPhone());
        }
        holder.store_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(CALL, Uri.parse("tel:" + "0953162179"));
                mContext.startActivity(call);
            }
        });
        //holder.store_price.setText(Integer.toString(mData.get(position).getRating()));
        Drawable progress = holder.store_rating.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.RED);
        holder.store_rating.setRating(mData.get(position).getRating()/10);
        //StoreDetail(mData.get(position).getPlaceid());
        //holder.store_phone.setText()
        //holder.store_phone.setText(phoneList.get(position));

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
        public RatingBar store_rating;

        public ViewHolder(View view) {
            super(view);
            store_photo = (ImageView) view.findViewById(R.id.img);
            store_name = (TextView) view.findViewById(R.id.info_text);
            store_price = (TextView) view.findViewById(R.id.price_text);
            store_phone = (TextView) view.findViewById(R.id.phone_text);
            store_rating = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }
}
