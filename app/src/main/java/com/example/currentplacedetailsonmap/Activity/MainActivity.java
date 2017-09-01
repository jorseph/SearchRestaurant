package com.example.currentplacedetailsonmap.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.data.LocationInfo;
import com.example.currentplacedetailsonmap.util.ConfigUtil;
import com.example.currentplacedetailsonmap.util.DetailJSONParser;
import com.example.currentplacedetailsonmap.util.PlaceJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

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

public class MainActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private static final String TAG = MainActivity.class.getSimpleName();
    private final int radius = 500;
    private final String language = "zh-TW";
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private final LatLng mDefaultLocation = new LatLng(25.12, 121.50);
    private double mLatitude = mDefaultLocation.latitude;
    private double mLongitude = mDefaultLocation.longitude;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    private static final String KEY_LOCATION = "location";
    private String page_token = "";

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private LocationManager mLocationManager;
    private Location mCurrentLocation;
    LocationRequest mLocationRequest;
    //private Button btn_OK,btn_next;
    private ArrayList<LocationInfo> locationInfoList;
    //private MyDBHelper helper;
    private ArrayList<String> phoneList = new ArrayList<>();
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        locationInfoList = new ArrayList<LocationInfo>();
        setContentView(R.layout.activity_main);
        //helper = new MyDBHelper(this, "expense.db", null, 1);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(1000);


    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location Update", "CHANGED");
        mCurrentLocation = location;
        mLatitude = mCurrentLocation.getLatitude();
        mLongitude = mCurrentLocation.getLongitude();
    }
    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        // Display the connection status
        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        getCurrentLocation();
        searchKeyword("restaurant");
    }

    private void getCurrentLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        if (mLastKnownLocation != null) {
            mLatitude = mLastKnownLocation.getLatitude();
            mLongitude = mLastKnownLocation.getLongitude();

        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
        }

        if(mLocationPermissionGranted) {

            if (!(isGPSEnabled || isNetworkEnabled)) {
            }
            else {
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (isGPSEnabled) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, this);
                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
            if (location != null) {
                Log.d(TAG, "" + location.getLatitude() + " " + location.getLongitude());
            }
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently

        // ...
    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        private MainActivity context = null;
        String data = null;

        public PlacesTask(MainActivity context) {
            this.context = context;
        }



        @Override
        protected String doInBackground(String... url) {
            try {
                Thread.sleep(1000);
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
            MainActivity.ParserTask parserTask = new MainActivity.ParserTask();
            parserTask.execute(result);
        }
    }




    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends
            AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(
                String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.v(TAG,jObject.toString());
                places = placeJsonParser.parse(jObject);
                page_token = placeJsonParser.getPageToken(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, String> hmPlace = list.get(i);

                double lat = Double.parseDouble(hmPlace.get("lat"));
                double lng = Double.parseDouble(hmPlace.get("lng"));
                String name = hmPlace.get("place_name");
                String placeid = hmPlace.get("place_id");
                String vicinity = hmPlace.get("vicinity");
                int rating = (int)(Float.valueOf(hmPlace.get("rating"))*10);
                String photo = hmPlace.get("photo");
                boolean nowopen = Boolean.parseBoolean(hmPlace.get("nowopen"));
                locationInfoList.add(new LocationInfo(placeid, String.valueOf(lat),String.valueOf(lng),vicinity,"",name,"restaurant",photo,rating,100,nowopen,""));
                Log.v(TAG,"rating = " + rating);
            }

            if(page_token != null) {
                searchKeywordNextPage("restaurant");
            } else {
                //for(int i = 0; i < locationInfoList.size(); i++) {

                    StoreDetail(locationInfoList.get(i).getPlaceid());
                //}

                //for(int i = 0; i < phoneList.size(); i++) {

                //}

                //Collections.sort(locationInfoList, new Comparator<LocationInfo>() {
                 //   @Override
                //    public int compare(LocationInfo o1, LocationInfo o2) {
                //        return o2.getRating()-o1.getRating();
                //    }
                //});
                /*for(int i = 0; i < locationInfoList.size(); i++) {
                    Log.v(TAG, "locationInfoList = " + locationInfoList.get(i).getName().toString());
                    Log.v(TAG, "locationInfoList = " + locationInfoList.get(i).getRating());
                }*/
                //ShowStoreStart(locationInfoList);
            }
        }
    }


    /**
     * 用關鍵字搜尋地標
     *
     *
     * @param keyword
     */
    private void searchKeyword(String keyword) {
        try {
            String unitStr = URLEncoder.encode(keyword, "utf8");  //字體要utf8編碼
            StringBuilder sb = new StringBuilder(ConfigUtil.GOOGLE_SEARCH_API);
            sb.append("location=" + mLatitude + "," + mLongitude);
            sb.append("&radius=" + radius);
            sb.append("&language =" + language);
            sb.append("&types=" + keyword);
            sb.append("&sensor=true");
            sb.append("&key=" + ConfigUtil.API_KEY_GOOGLE_MAP);  //server key
            MainActivity.PlacesTask placesTask = new MainActivity.PlacesTask(MainActivity.this);
            Log.v(TAG, sb.toString());
            placesTask.execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(ConfigUtil.TAG, "Except ion:" + e);
        }
    }

    private void searchKeywordNextPage(String keyword) {
        try {
            String unitStr = URLEncoder.encode(keyword, "utf8");  //字體要utf8編碼
            StringBuilder sb = new StringBuilder(ConfigUtil.GOOGLE_SEARCH_API);
            sb.append("location=" + mLatitude + "," + mLongitude);
            sb.append("&radius=" + radius);
            sb.append("&language =" + language);
            sb.append("&types=" + keyword);
            sb.append("&sensor=true");
            sb.append("&key=" + ConfigUtil.API_KEY_GOOGLE_MAP);  //server key
            sb.append("&pagetoken=" + page_token);
            MainActivity.PlacesTask placesTask = new MainActivity.PlacesTask(MainActivity.this);
            Log.v(TAG, sb.toString());
            placesTask.execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(ConfigUtil.TAG, "Exception:" + e);
        }
    }

    private void StoreDetail(String placeid) {
        try {
            String unitStr = URLEncoder.encode(placeid, "utf8");  //字體要utf8編碼
            StringBuilder sb = new StringBuilder(ConfigUtil.GOOGLE_DETAIL_API);
            sb.append("?placeid=" + placeid);
            sb.append("&key=" + ConfigUtil.API_KEY_GOOGLE_MAP);  //server key
            MainActivity.DetailsTask placesTask = new MainActivity.DetailsTask(MainActivity.this);
            Log.v(TAG, sb.toString());
            placesTask.execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(ConfigUtil.TAG, "Exception:" + e);
        }
    }

    /**
     * A class, to download Google Places
     */
    private class DetailsTask extends AsyncTask<String, Integer, String> {

        private MainActivity context = null;
        String data = null;

        public DetailsTask(MainActivity context) {
            this.context = context;
        }



        @Override
        protected String doInBackground(String... url) {
            try {
                Thread.sleep(1000);
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
            MainActivity.ParserDetailTask parserTask = new MainActivity.ParserDetailTask();
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
            locationInfoList.get(i).setPhone(phone);
            if(i < (locationInfoList.size()-1)) {
                i++;
                StoreDetail(locationInfoList.get(i).getPlaceid());
            } else {
                Collections.sort(locationInfoList, new Comparator<LocationInfo>() {
                   @Override
                    public int compare(LocationInfo o1, LocationInfo o2) {
                        return o2.getRating()-o1.getRating();
                   }
                });
                /*for(int i = 0; i < locationInfoList.size(); i++) {
                    Log.v(TAG, "locationInfoList = " + locationInfoList.get(i).getName().toString());
                    Log.v(TAG, "locationInfoList = " + locationInfoList.get(i).getRating());
                }*/
                ShowStoreStart(locationInfoList);
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

    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void ShowStoreStart(ArrayList<LocationInfo> data) {
        Log.d(TAG, "ShowStoreStart()");
        Intent intentToShowStoreActivity = new Intent();
        intentToShowStoreActivity.putExtra("LocationInfo", data);
        intentToShowStoreActivity.setClass(MainActivity.this, ShowStoreActivity.class);
        intentToShowStoreActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentToShowStoreActivity);
    }
}

