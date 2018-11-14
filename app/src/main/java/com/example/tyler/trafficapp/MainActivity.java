package com.example.tyler.trafficapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE = "com.example.tyler.trafficapp.MESSAGE";
    Button b1,b2;       // Login and Signup buttons
    EditText ed1,ed2;   // Username and password fields

    TextView tx1;       // "Login Screen" text display on top of page
    int counter = 3;    // 3 attempts to login counter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.login_button_login);
        ed1 = (EditText)findViewById(R.id.login_edittext_username);
        ed2 = (EditText)findViewById(R.id.login_edittext_password);

        b2 = (Button)findViewById(R.id.login_button_regUser);
        tx1 = (TextView)findViewById(R.id.login_textView_loginScreen);
        tx1.setVisibility(View.VISIBLE);
    }

    public void standardUser(View view) {
        Intent intent = new Intent(this, ListOptionsActivity.class);
        String message = "Logged in as Standard User";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void adminUser(View v) {

        if (ed1.getText().toString().equals("admin") &&
                ed2.getText().toString().equals("admin")) {
            //Login as Admin
            Toast.makeText(getApplicationContext(),
                    "Redirecting...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ListOptionsActivity.class);
            String message = "Logged in as Admin";
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);

        } else {
            //Invalid Login, decrement counter
            Toast.makeText(getApplicationContext(), "Wrong" +
                    "Credentials", Toast.LENGTH_SHORT).show();

            tx1.setVisibility(View.VISIBLE);
            tx1.setBackgroundColor(Color.RED);
            counter--;
            tx1.setText(Integer.toString(counter));

            if (counter == 0) {
                //Ran out of attempts to login, disable admin login
                b1.setEnabled(false);
            }
        }
    }
}