package com.example.currentplacedetailsonmap.Activity;

import android.content.Context;
import android.graphics.Bitmap;
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
        Picasso.with(mContext).load(mData.get(position).getPhoto()).into(holder.imageView);
        holder.textView.setText(mData.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.img);
            textView = (TextView) view.findViewById(R.id.info_text);
        }
    }

}
