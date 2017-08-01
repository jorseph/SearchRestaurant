package com.example.currentplacedetailsonmap.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.currentplacedetailsonmap.PageViewFile.PageOneView;
import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.data.LocationInfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorseph on 2017/7/27.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = MyAdapter.class.getSimpleName();
    private ArrayList<LocationInfo> mData;
    private Context mContext;
    private Bitmap mBitmap;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mImageView = (ImageView) v.findViewById(R.id.img);
        }
    }

    public MyAdapter(ArrayList<LocationInfo> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        getBMPFromURL(mContext,mData.get(position).getPhoto());
        holder.mTextView.setText(mData.get(position).getName());
        holder.mImageView.setImageBitmap(mBitmap);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void getBMPFromURL(Context context, String URL) {
        MyAdapter.PhotoTask photoTask = new MyAdapter.PhotoTask(context);
        Log.v(TAG, URL);
        photoTask.execute(URL);
    }

    /**
     * A class, to download Places Photo
     */
    private class PhotoTask extends AsyncTask<String, Integer, Bitmap> {

        private Context context = null;

        public PhotoTask(Context context) {
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                Bitmap bitmap = null;
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bitmap;
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            //store_photo.setImageBitmap(result);
            mBitmap = result;
        }
    }
}
