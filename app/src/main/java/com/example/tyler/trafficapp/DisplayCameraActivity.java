package com.example.tyler.trafficapp;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayCameraActivity extends AppCompatActivity {

    String cameraId, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cameraId = intent.getStringExtra("cameraId");
        setContentView(R.layout.activity_display_camera);
        TextView tv = findViewById(R.id.textView5);
        tv.setText("Please press back again to go to the camera list page.");
        String url = ("https://traffic.ottawa.ca/opendata/camera?c="+cameraId+"&certificate=reingatsca531122018102&id=2");
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

}
