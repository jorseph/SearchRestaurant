package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.currentplacedetailsonmap.data.LocationInfo;
import com.spreada.utils.chinese.ZHConverter;

import java.net.URL;

import static com.example.currentplacedetailsonmap.util.ConfigUtil.TAG;

/**
 * Created by jorseph on 2017/7/11.
 */

public class PageOneView extends PageView{
    private static final String TAG = PageOneView.class.getSimpleName();
    private TextView store_name;
    private ImageView store_photo;
    public PageOneView(final Context context, final LocationInfo mlocationinfo) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.page_content, null);
        store_name = (TextView) view.findViewById(R.id.text);
        store_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startmaps = "geo:" + mlocationinfo.getLat() + "," + mlocationinfo.getLng() + "?q=" + mlocationinfo.getName();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(startmaps));
                context.startActivity(intent);
            }
        });

        store_photo = (ImageView) view.findViewById(R.id.image);
        String traditionalStr = ZHConverter.convert(mlocationinfo.getName(), ZHConverter.TRADITIONAL);
        store_name.setText(traditionalStr);
        getBMPFromURL(context, mlocationinfo.getPhoto());
        addView(view);
    }


    public void getBMPFromURL(Context context, String URL) {
        PhotoTask photoTask = new PhotoTask(context);
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
            store_photo.setImageBitmap(result);
        }
    }

    @Override
    public void refreshView() {

    }
}