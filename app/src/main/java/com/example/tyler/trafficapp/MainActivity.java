package com.example.tyler.trafficapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button6);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String db = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection dbCon = DriverManager.getConnection("jdbc:jtds:sqlserver://traffic-cam.database.windows.net:1433/Android;user=tyler@traffic-cam;password=Password!;");
            db = dbCon.toString();

            Statement stmt = dbCon.createStatement();
            String query = "SELECT cam_name FROM Traffic_Camera WHERE cam_id = 1;";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                String name = rs.getString(1);
                button.setText(name);
            }

             query = "SELECT cam_name FROM Traffic_Camera WHERE cam_id = 2;";
             rs = stmt.executeQuery(query);
             if(rs.next()){
                String name = rs.getString(1);
                button2.setText(name);
            }

        }
        catch (Exception e){
            button.setText(e.getMessage());
        }

        TextView text = findViewById(R.id.textView2);
        text.setText(SQLConnect()); //to prove sql server connected
    }

    public void viewCamera(View view){
        Intent viewTraffic = new Intent(this, DisplayCameraActivity.class);
        startActivity(viewTraffic);
    }

    public void viewCamera2(View view){
        Intent viewTraffic2 = new Intent(this, DisplayCameraActivity2.class);
        startActivity(viewTraffic2);
    }

    public String SQLConnect(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String db = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection dbCon = DriverManager.getConnection("jdbc:jtds:sqlserver://traffic-cam.database.windows.net:1433;database=Android.dbo;user=tyler@traffic-cam;password=Password!;Initial Catalog=dbo");
            db = dbCon.toString();

            dbCon.close();

    } catch (Exception e)
    {
        db = e.getMessage();
    }

    return db;
    }
}
