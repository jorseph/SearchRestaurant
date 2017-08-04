package com.example.currentplacedetailsonmap.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.data.LocationInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jorseph on 2017/7/31.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<LocationInfo> mData;
    private Context mContext;

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
        Picasso.with(mContext).load(mData.get(position).getPhoto()).into(holder.store_photo);
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

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView store_photo;
        public TextView store_name;

        public ViewHolder(View view) {
            super(view);
            store_photo = (ImageView) view.findViewById(R.id.img);
            store_name = (TextView) view.findViewById(R.id.info_text);
        }
    }

}
