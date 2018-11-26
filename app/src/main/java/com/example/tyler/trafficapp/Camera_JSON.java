package com.example.tyler.trafficapp;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.Scanner;

import android.widget.Toast;


public class Camera_JSON extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        //  Toast.makeText(this, "Lol", Toast.LENGTH_SHORT).show();;

    }


    public void loadGrades(View view) {

        StringBuilder builder = new StringBuilder();

 InputStream is = null;
            Resources res = getResources();
            try {

               // if ()
                 is = res.openRawResource(R.raw.student_grades);
            }
            catch (Exception e){
                e.printStackTrace();
            }
                builder = new StringBuilder();
                Scanner scanner = new Scanner(is);

                while (scanner.hasNext()) {
                    builder.append(scanner.nextLine());
                }
                parseJSON(builder.toString());

      //  RecyclerView view1 = (RecyclerView) findViewById(R.id.recyclerCam);
       // view1.setText(builder.toString());
        ArrayList<Camera> dataList = new ArrayList<>();

        JSONArray jsonArr;
        try {

            jsonArr  = new JSONArray(builder.toString());

        for (int i = 0; i < jsonArr.length(); i++) {

            JSONObject jsonObj = jsonArr.getJSONObject(i);
           // Camera camera = null;

            String name = jsonObj.getString("cam_name");
            String id = jsonObj.getString("cam_id");
             String lon = jsonObj.getString("cam_longitude");
            String lat = jsonObj.getString("cam_latitude");

            if (getResources().getConfiguration().locale.getLanguage().equals("fr")) {

                name = jsonObj.getString("cam_frName");


            }
           // Camera camera= new Camera(name,id,lat,lon);
            dataList.add(new Camera(name,id,lat,lon));
           // view1.setText(camera.toString());//+" "+lon+""+lat);





            // data.value = jsonObj.getString("value");

        }
        }
        catch (JSONException e){
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
        recyclerView.setAdapter(new CameraAdapter(this,dataList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void parseJSON(String s) {

     //   RecyclerView view = (RecyclerView) findViewById(R.id.recyclerCam);

       // ArrayList<ArrayList<String>> list = gson.fromJson(jsonString, new TypeToken<ArrayList<ArrayList<String>>>() {}.getType());


      //  Type listType = new TypeToken<List<Camera>>() {}.getType();

//        StringBuilder builder = new StringBuilder();
//
//        try{
//            JSONObject root = new JSONObject();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        //view.setText(s);
    }



}
