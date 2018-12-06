package com.example.tyler.trafficapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraList extends AppCompatActivity {

    private RecyclerView camList;
    private RecyclerView.Adapter camAdapter;
    private RecyclerView.LayoutManager camLayout;
    private CameraAdapter camAd;
    private EditText searchBar;
    ArrayList<Camera> cameraArrayList = new ArrayList<>();

    //String[][] camArray = loadArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);

        //working with recyclerview in xml
        //camList = (RecyclerView) findViewById(R.id.recyclerCam);
        //camLayout = new LinearLayoutManager(this);
        //camList.setLayoutManager(camLayout);
        //camArray = loadArray();
        //camAdapter = new CameraAdapter(camArray);
        //camList.setAdapter(camAdapter);
        cameraArrayList = loadArray();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCam);
        //recyclerView.setHasFixedSize(true);
        camAd = new CameraAdapter(this,cameraArrayList);
        recyclerView.setAdapter(camAd);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchBar = findViewById(R.id.editText);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable search) {
                searchCameras(search.toString());
            }
        });

    }

    void searchCameras(String searchText){
        ArrayList<Camera> filteredCams = new ArrayList();

        for(Camera cam: cameraArrayList){
            if(cam.getCameraName().toLowerCase().contains(searchText.toLowerCase())){
                filteredCams.add(cam);
            }
        }

        camAd.filteredList(filteredCams);
    }

    public ArrayList<Camera> loadArray(){

        //String[][] camInfo = null;
        ArrayList<Camera> cameras = new ArrayList<>();

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
            String query = "SELECT COUNT(*) FROM CamerasFrench;";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                rows = Integer.parseInt(rs.getString(1));           //gets the amount of rows in database
            }
            //camInfo = new String[rows][4];

            stmt = dbCon.createStatement();
            query = "SELECT * FROM CamerasFrench;";
            rs = stmt.executeQuery(query);
            while(rs.next()){  //goes through every row, puts the data into the 2d array
                String cameraName = rs.getString("cam_name");
                String cameraLong = rs.getString("cam_longitude");
                String cameraLat = rs.getString("cam_latitude");
                String cameraId = rs.getString("cam_id");
                if (getResources().getConfiguration().locale.getLanguage() == "fr") {

                    cameraName = rs.getString("cam_frName");

                }
                cameras.add(new Camera(cameraName, cameraId, cameraLong, cameraLat));
                //i++;
                //System.out.println("List Size: "+cameras.size());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        if (cameras.size() > 0) {
            Collections.sort(cameras, new Comparator<Camera>() {
                @Override
                public int compare(final Camera object1, final Camera object2) {
                    return object1.getCameraName().compareTo(object2.getCameraName());
                }
            });
        }

        return cameras;
    }
}
