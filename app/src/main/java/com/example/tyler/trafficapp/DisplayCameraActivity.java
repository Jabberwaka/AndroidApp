package com.example.tyler.trafficapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisplayCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_camera);
        WebView myWebView = findViewById(R.id.webview);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://beta.html5test.com/");

    }


    public void viewWeb1(){
        WebView myWebView = findViewById(R.id.webview);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://beta.html5test.com/");
    }


    public void viewWeb2(){
        WebView myWebView = findViewById(R.id.webview);
        WebSettings ws = myWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://github.com/");
    }

}
