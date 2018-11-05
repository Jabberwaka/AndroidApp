package com.example.tyler.trafficapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CameraList extends AppCompatActivity {

    private RecyclerView camList;
    private RecyclerView.Adapter camAdapter;
    private RecyclerView.LayoutManager camLayout;

    String[][] camArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);

        //working with recyclerview in xml
        camList = (RecyclerView) findViewById(R.id.recyclerCam);
        camLayout = new LinearLayoutManager(this);
        camList.setLayoutManager(camLayout);
        camArray = loadArray();
        camAdapter = new CameraAdapter(camArray);
    }

    public String[][] loadArray(){

        String[][] camInfo = null;

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
            if(rs.next()){
                rows = Integer.parseInt(rs.getString(1));           //gets the amount of rows in database
            }
            camInfo = new String[rows][4];

             stmt = dbCon.createStatement();
             query = "SELECT * FROM Cameras;";
             rs = stmt.executeQuery(query);
            if(rs.next()){  //goes through every row, puts the data into the 2d array
                camInfo[i][1] = rs.getString("cam_name");
                camInfo[i][2] = rs.getString("cam_longitude");
                camInfo[i][3] = rs.getString("cam_latitude");
                camInfo[i][0] = rs.getString("cam_id");
                i++;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return camInfo;
    }
}
