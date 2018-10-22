package com.example.tyler.trafficapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DisplayCameraActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_camera2);
        WebView myWebView = findViewById(R.id.webview);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://beta.html5test.com/");
    }
}
