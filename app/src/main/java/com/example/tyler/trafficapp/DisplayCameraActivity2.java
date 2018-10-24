package com.example.tyler.trafficapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DisplayCameraActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_camera2);




        WebView myWebView = findViewById(R.id.webview);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String db = null;
        String url = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection dbCon = DriverManager.getConnection("jdbc:jtds:sqlserver://traffic-cam.database.windows.net:1433/Android;user=tyler@traffic-cam;password=Password!;");
            db = dbCon.toString();

            Statement stmt = dbCon.createStatement();
            String query = "SELECT cam_url FROM Traffic_Camera WHERE cam_id = 1;";
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                url = rs.getString(1);
            }
            myWebView.loadUrl(url);

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
