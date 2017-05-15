package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.currentplacedetailsonmap.data.LocationInfo;
import com.example.currentplacedetailsonmap.data.MarkerHelper;
import com.example.currentplacedetailsonmap.util.ConfigUtil;
import com.example.currentplacedetailsonmap.util.GsonUtil;
import com.example.currentplacedetailsonmap.util.PlaceJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.HashMap;
import java.util.List;

public class MainActivity2 extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    private static final String TAG = MainActivity2.class.getSimpleName();
    private TextView store_name;
    private TextView store_address;
    private TextView store_tel;

    private final int radius = 500;
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

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

     private LocationManager mLocationManager;
    private Location mCurrentLocation;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        setContentView(R.layout.activity_main2);
        store_name = (TextView)findViewById(R.id.store_name);
        store_address = (TextView)findViewById(R.id.store_address);
        store_tel = (TextView)findViewById(R.id.store_phone_number);
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
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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

        if (!(isGPSEnabled || isNetworkEnabled)){}
        //Snackbar.make(mMapView, R.string.error_location_provider, Snackbar.LENGTH_INDEFINITE).show();
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
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
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

        private MainActivity2 context = null;
        String data = null;

        public PlacesTask(MainActivity2 context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... url) {
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
            //context.dialog = ProgressDialog.show(context, "",
            //       context.getString(R.string.loading), true);
            //context.openPlacesDialog();
        }

        @Override
        protected void onPostExecute(String result) {
            //context.dialog.dismiss();
            //context.dismissDialog(2);
            MainActivity2.ParserTask parserTask = new MainActivity2.ParserTask();
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

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            //mMap.clear();
            //mapInfoAdapter.setKeyword(true);
            List<LocationInfo> locationInfoList = new ArrayList<LocationInfo>();
            String textname = "";
            for (int i = 0; i < list.size(); i++) {
                //MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> hmPlace = list.get(i);
                double lat = Double.parseDouble(hmPlace.get("lat"));
                double lng = Double.parseDouble(hmPlace.get("lng"));
                LatLng latLng = new LatLng(lat, lng);
                //markerOptions.position(latLng);
                String name = hmPlace.get("place_name");
                //markerOptions.title(name);
                String vicinity = hmPlace.get("vicinity");
                //MarkerHelper markerHelper = new MarkerHelper(name, vicinity);
                //MarkerHelper markerHelper = new MarkerHelper(new Lo)
                //String snippet = GsonUtil.gson.toJson(markerHelper);
                //arkerOptions.snippet(snippet);
                //textname = name;
                //Log.v(TAG,"name = " + name);
                locationInfoList.add(new LocationInfo(String.valueOf(lat),String.valueOf(lng),vicinity,"",name,"restaurant",1));
                Log.v(TAG,"textname = " + name);
            }

            store_name.setText(locationInfoList.get((int)(Math.random()*list.size())).getName());
            store_address.setText(locationInfoList.get((int)(Math.random()*list.size())).getVicinity());
            store_tel.setText(locationInfoList.get((int)(Math.random()*list.size())).getTel());
            //LatLng latLng = new LatLng(mLatitude, mLongitude);
            //addMyLocationIcon(latLng);
        }
    }
    /**
     * 用關鍵字搜尋地標
     *
     * @param keyword
     */
    private void searchKeyword(String keyword) {
        try {
            String unitStr = URLEncoder.encode(keyword, "utf8");  //字體要utf8編碼
            StringBuilder sb = new StringBuilder(ConfigUtil.GOOGLE_SEARCH_API);
            sb.append("location=" + mLatitude + "," + mLongitude);
            sb.append("&radius=" + radius);
            //sb.append("&keyword=" + unitStr);
            sb.append("&types=" + keyword);
            sb.append("&sensor=true");
            sb.append("&key=" + ConfigUtil.API_KEY_GOOGLE_MAP);  //server key
            MainActivity2.PlacesTask placesTask = new MainActivity2.PlacesTask(MainActivity2.this);
            Log.v(TAG, sb.toString());
            placesTask.execute(sb.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.i(ConfigUtil.TAG, "Exception:" + e);
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

}
