package com.example.tyler.trafficapp;

import android.app.VoiceInteractor;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;

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


        cameraArrayList = loadArray();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCam);
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

        ArrayList<Camera> cameras = new ArrayList<>();

        try {

            String protocol = "http://";
            //this will continually have to be updated, everytime, to the ip of the computer running the restful server thing
            String ip = "10.70.106.72";
            String urlS = ":50323/Cam_Sql/webresources/com.mycompany.cam_sql.camerasfrench/1/250";
            String urlString = protocol+ip+urlS;
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            URLConnection conn = null;
            try {
                conn = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            Document docb = null;
            try {
                docb = builder.parse(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            TransformerFactory transformerFactory= TransformerFactory.newInstance();
            Transformer xform = null;
            try {
                xform = transformerFactory.newTransformer();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            String stresult = null;
            try {
                xform.transform(new DOMSource(docb),result);
                stresult = writer.toString();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

            String[] camerasAll = stresult.split("<camerasFrench>");
            int len = camerasAll.length;
            String[][] cameraInfo = new String[len-1][10];
            for(int i = 1; i < len; ++i){
                cameraInfo[i-1]=camerasAll[i].split("<\\s*[/a-zA-Z]+\\s*>");
                if(cameraInfo[i-1][7].contains("&amp;")){
                    cameraInfo[i-1][7] = cameraInfo[i-1][7].replace("&amp;", "&");
                }
            }

            //id is in 1, latitude 3, longitude 5, name 7
            for(int i = 0; i < cameraInfo.length; ++i){
                String cameraName = cameraInfo[i][7];
                if (getResources().getConfiguration().locale.getLanguage() == "fr") {

                    cameraName = cameraInfo[i][9];

                }
                String cameraLong = cameraInfo[i][5];
                String cameraLat = cameraInfo[i][3];
                String cameraId = cameraInfo[i][1];
                cameras.add(new Camera(cameraName, cameraId, cameraLong, cameraLat));
            }

//            String[] camerasAll = stresult.split("<cameras>");
//            int len = camerasAll.length;
//            String[][] cameraInfo = new String[len-1][8];
//            for(int i = 1; i < len; ++i){
//                cameraInfo[i-1]=camerasAll[i].split("<\\s*[/a-zA-Z]+\\s*>");
//                if(cameraInfo[i-1][7].contains("&amp;")){
//                    cameraInfo[i-1][7] = cameraInfo[i-1][7].replace("&amp;", "&");
//                }
//            }
//
//            //id is in 1, latitude 3, longitude 5, name 7
//            for(int i = 0; i < cameraInfo.length; ++i){
//                String cameraName = cameraInfo[i][7];
//                String cameraLong = cameraInfo[i][5];
//                String cameraLat = cameraInfo[i][3];
//                String cameraId = cameraInfo[i][1];
//                cameras.add(new Camera(cameraName, cameraId, cameraLong, cameraLat));
//            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String db = null;
        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection dbCon = DriverManager.getConnection("jdbc:jtds:sqlserver://traffic-cam.database.windows.net:1433/Android;user=tyler@traffic-cam;password=Password!;");
           // db = dbCon.toString();
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
        */
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
