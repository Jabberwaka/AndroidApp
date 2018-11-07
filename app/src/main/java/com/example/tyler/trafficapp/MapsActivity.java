package com.example.tyler.trafficapp;


import java.sql.SQLData;
import java.util.Locale;
import java.lang.String;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLData;
import android.content.Intent;

//import android.graphics.Camera;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.os.StrictMode;
import android.support.v7.view.menu.MenuBuilder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.L;


//import android.content.Context;
//import android.os.Message;
//import android.os.Bundle;
//import android.util.Log;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.widget.CursorAdapter;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
        ,LocationListener{
    //private Context context;
    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;


    private GoogleMap mMap;
    float zoom = 97 / 9;
    public String camera_Tables;
    List<Double> longitudes = new ArrayList<Double>();
    List<Double> latitudes = new ArrayList<Double>();

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()

                        .position(latLng)
                        .title(getString(R.string.dropped_pin))
                        .snippet(snippet));


            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //String[][] camInfo = null;
        ArrayList<LatLng> cameras = new ArrayList<LatLng>();
        ArrayList<String> cam_id = new ArrayList<String>();
        ArrayList<String> cam_name = new ArrayList<String>();

      //  WebView myWebView = findViewById(R.id.buttonMap);
       // WebSettings ws = myWebView.getSettings();
      //  ws.setJavaScriptEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String db = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection dbCon = DriverManager.getConnection("jdbc:jtds:sqlserver://traffic-cam.database.windows.net:1433/Android;user=tyler@traffic-cam;password=Password!;");
            db = dbCon.toString();
            int i = 0; //iterator
            int rows = 0;

            Statement stmt = dbCon.createStatement();
            String query = "SELECT COUNT(*) FROM Cameras;";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                rows = Integer.parseInt(rs.getString(1));           //gets the amount of rows in database
            }
            //camInfo = new String[rows][4];
            // Cursor cursor = dbCon.createStatement();;

            stmt = dbCon.createStatement();
            query = "SELECT * FROM Cameras;";
            rs = stmt.executeQuery(query);
            while (rs.next()) {  //goes through every row, puts the data into the 2d array
                String cameraName = rs.getString("cam_name");
                String cameraLong = rs.getString("cam_longitude");
                String cameraLat = rs.getString("cam_latitude");
                String cameraId = rs.getString("cam_id");
                //cameras.add(new Camera(cameraName, cameraId, cameraLong, cameraLat));
                //i++;
                //System.out.println("List Size: "+cameras.size());
                LatLng locate = new LatLng(Double.parseDouble(cameraLat), Double.parseDouble(cameraLong));
                cameras.add(locate);
                cam_id.add(cameraId);
                cam_name.add(cameraName);


            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        mMap = googleMap;
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        latlngs.add(new LatLng(45.425533, -75.692482)); //some latitude and logitude value

        Iterator<LatLng> iterator = cameras.iterator();
        Iterator<String> iterator1 = cam_id.iterator();
        Iterator<String> iterator2 = cam_name.iterator();

        while(iterator.hasNext()){

            LatLng point = iterator.next();
            options.position(point);

            options.title(iterator1.next());
            options.snippet(iterator2.next());
            googleMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        }
        LatLng ottawa = new LatLng(45.425533, -75.692482);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ottawa,zoom));

    }


    protected synchronized void buildGoogleApiClient () {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
    }

//
//        @Override
    public void onLocationChanged(Location location)
    {
//            mLastLocation = location;
//            if (mCurrLocationMarker != null) {
//                mCurrLocationMarker.remove();
//            }
//
//            //Place current location marker
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//            //move map camera
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//
//            //stop location updates
//            if (mGoogleApiClient != null) {
//
//              //  LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//                LocationServices.getFusedLocationProviderClient(this);
//            }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//          //  LocationServices.getFusedLocationProviderClient(this);
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // Permission was granted.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                       // mMap.setMyLocationEnabled(true);
//                       //   mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//                    }
//
//                } else {
//
//                    // Permission denied, Disable the functionality that depends on this permission.
//                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }

    // other 'case' lines to check for other permissions this app might request.
    //You can add here other case statements according to your requirement.
    // }
    // }

//    private float distance(LatLng latLng) {
//
//
//        Location lool = new Location("Location A");
//        lool.setLatitude(latLng.latitude);
//        lool.setLongitude(latLng.longitude);
//
//        Location current =  new Location("Current ");
//
//
//        if (!current.canGetLocation()) {
//            current.showSettingsAlert();
//        } else {
//           double latitude = current.getLatitude();
//
//            float longitude = current.getLongitude();
//
//        }
//
//        float distance = tracker.distanceTo(lool);
//
//    }

//    protected void connection() {
//        //super.onCreate(savedInstanceState);
//        //setContentView(R.layout.MapsActivity);
//        try {
//
//            // Set the connection string
//            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
//            String username = "tyler@traffic-cam";
//            String password = "Password!";
//
//            Connection DBconn = DriverManager.getConnection("jdbc:jtds:sqlserver://Testdev.mssql.somee.com/Testdev;user=" + username + ";password=" + password);
//            Log.w("Connection", "open");
//
//            Statement stmt = DBconn.createStatement();
//            ResultSet resultSet = stmt.executeQuery("select * from Customer");
//
//            //TextView text = (TextView) findViewById(R.id.login_label);
//            //text.setText(resultSet.getString(1));
//
//            DBconn.close();
//
//        } catch (Exception e) {
//
//            Log.w("Error connection", "" + e.getMessage());
//
//
//        }
//
//    }

//    public void getLatitudes(   ) {
//        //List<Double> longitudes = new ArrayList<Double>();
//      //  List<Double> latitudes = new ArrayList<Double>();
//
//        String query1 = "SELECT cam_latitude FROM Camera ";
//        String query = "SELECT cam_longitude FROM Camera ";
//
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, null);
//        Cursor cursor1 = db.rawQuery(query1, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                Double latitude1s = new Double(cursor.toString());
//                Double longitude1s = new Double(cursor1.toString());
//
//                LatLng latLng = new LatLng(latitude1s, longitude1s);
//                // st.setId(cursor.getString(cursor.getColumnIndex(ID)));
//                // st.setName(cursor.getString(cursor.getColumnIndex(NAME)));
//                // st.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED)));
//                longitudes.add(longitude1s);
//                latitudes.add(latitude1s);
//
//            } while (cursor.moveToNext());
    //       }
    //       db.close();
    //return latitudes;
    //   }


}