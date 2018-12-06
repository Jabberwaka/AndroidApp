package com.example.tyler.trafficapp;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.util.Scanner;

import android.widget.Toast;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Camera_JSON extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        //  Toast.makeText(this, "Lol", Toast.LENGTH_SHORT).show();;

    }

    public void loadGrades(View view) {


        ArrayList<Camera> dataList = new ArrayList<>();

        try {

            String protocol = "http://";
            //this will continually have to be updated, everytime, to the ip of the computer running the restful server thing
            String ip = "192.168.2.21";
            String urlS = ":50323/Cam_Sql/webresources/com.mycompany.cam_sql.camerasfrench/1/250";
            String urlString = protocol + ip + urlS;
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

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
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
                xform.transform(new DOMSource(docb), result);
                stresult = writer.toString();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

            String[] camerasAll = stresult.split("<camerasFrench>");
            int len = camerasAll.length;
            String[][] cameraInfo = new String[len - 1][10];
            for (int i = 1; i < len; ++i) {
                cameraInfo[i - 1] = camerasAll[i].split("<\\s*[/a-zA-Z]+\\s*>");
                if (cameraInfo[i - 1][7].contains("&amp;")) {
                    cameraInfo[i - 1][7] = cameraInfo[i - 1][7].replace("&amp;", "&");
                }
            }

            for (int i = 0; i < cameraInfo.length; ++i) {
                String cameraName = cameraInfo[i][7];
                if (getResources().getConfiguration().locale.getLanguage() == "fr") {

                    cameraName = cameraInfo[i][9];

                }
                String cameraLong = cameraInfo[i][5];
                String cameraLat = cameraInfo[i][3];
                String cameraId = cameraInfo[i][1];
                dataList.add(new Camera(cameraName, cameraId, cameraLat, cameraLong));

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (dataList.size() > 0) {
            Collections.sort(dataList, new Comparator<Camera>() {
                @Override
                public int compare(final Camera object1, final Camera object2) {
                    return object1.getCameraName().compareTo(object2.getCameraName());
                }
            });
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCam);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new CameraAdapter(getApplicationContext(), dataList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }



}
