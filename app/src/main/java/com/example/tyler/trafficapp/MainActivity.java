package com.example.tyler.trafficapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.support.constraint.Constraints.TAG;


public class MainActivity extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE = "com.example.tyler.trafficapp.MESSAGE";
    Button b1,b2;       // Login and Signup buttons
    EditText ed1,ed2;   // Username and password fields

    TextView tx1;       // "Login Screen" text display on top of page
    int counter = 3;    // 3 attempts to login counter



    static String ip = "192.168.1.118";
    static String protocol = "http://";
    static String urlS = ":33117/restful_camera/webresources/com.mycompany.restful_camera.users/count";
    static String urlGetUsers = ":33117/restful_camera/webresources/com.mycompany.restful_camera.users/0/";
    static String urlP = ":33117/restful_camera/webresources/com.mycompany.restful_camera.users/";


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

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//login function

                DoLogin doLogin = new DoLogin();
                doLogin.execute("");


                /*
                User user = new User(ed1.getText().toString(), ed1.getText().toString());
                if(user.validateUser() >= 0){
                    Toast.makeText(getApplicationContext(), "This works" , Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), user.getData() , Toast.LENGTH_SHORT).show();
                */

                /*
                if(type < 0){
                    Toast.makeText(getApplicationContext(), "The count is " + user.getCount() , Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Its valid", Toast.LENGTH_SHORT).show();
                }
                */



                //regular user login
                /*
                if(user.validateUser(user) == 0){

                     standardUser(v);
                }
                else if(user.validateUser(user) == 1){
                    //  Toast.makeText(getApplicationContext(),
                    //          "Successfully logged in as admin...", Toast.LENGTH_SHORT).show();
                      adminUser(v);
                }
                */
                //Toast.makeText(getApplicationContext(),
                 //       "Could not log in, please try again...", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void click(View view){
        User user = new User(ed1.getText().toString(), ed1.getText().toString());
        if(user.validateUser() >= 0){
            Toast.makeText(getApplicationContext(), "This works" , Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getApplicationContext(), user.getType() , Toast.LENGTH_SHORT).show();
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

    public void registerUser(){

        EditText username = (EditText)findViewById(R.id.login_edittext_username);
        EditText password = (EditText)findViewById(R.id.login_edittext_password);

        User user = new User(username.getText().toString(), password.getText().toString());
        if(user.addUserToDatabase()){
            Toast.makeText(getApplicationContext(),
                    "Successfully registered...", Toast.LENGTH_SHORT).show();
        }
        else{

        }
    }

    public void loginUser(){
/*
        EditText username = (EditText)findViewById(R.id.login_edittext_username);
        EditText password = (EditText)findViewById(R.id.login_edittext_password);

        User user = new User(username.getText().toString(), password.getText().toString());
        if(user.validateUser() >= 0){
            if(user.getType() == 0){
                Toast.makeText(getApplicationContext(),
                        "Lets goddamn goooo", Toast.LENGTH_SHORT).show();
                // standardUser(view);
            }
            else if(user.getType() == 1){
                Toast.makeText(getApplicationContext(),
                        "Lets goddamn goooo!!!", Toast.LENGTH_SHORT).show();
                //  adminUser(view);
            }
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Could not log in, please try again...", Toast.LENGTH_SHORT).show();
        }
*/

    }

    public class DoLogin extends AsyncTask<String, Void, String> {
        String userid = ed1.getText().toString();
        String password = ed2.getText().toString();
        User user = new User(userid, password);


        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlUser = protocol+ip+urlGetUsers+"1000";

                //getting users
                URL url2 = null;

                try {
                    url2 = new URL(urlUser);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                URLConnection conn2 = null;
                try {
                    conn2 = url2.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder2 = factory2.newDocumentBuilder();
                Document docb2 = null;

                try {
                    try {
                        docb2 = builder2.parse(conn2.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                TransformerFactory transformerFactory2 = TransformerFactory.newInstance();
                Transformer xform2 = null;

                try {
                    xform2 = transformerFactory2.newTransformer();
                } catch (TransformerConfigurationException e) {
                    e.printStackTrace();
                }

                StringWriter writer2 = new StringWriter();
                StreamResult result2 = new StreamResult(writer2);
                String stresult2 = null;
                try {
                    xform2.transform(new DOMSource(docb2),result2);
                    stresult2 = writer2.toString();
                } catch (TransformerException e) {
                    e.printStackTrace();
                }


                String[] usersAll = stresult2.split("<users>");
                int len = usersAll.length;
                String[][] users = new String[len-1][8];
                for(int i = 1; i < len; ++i){
                    users[i-1]=usersAll[i].split("<\\s*[/a-zA-Z]+\\s*>");

                    if(users[i-1][3].equals(user.username) && users[i-1][5].equals(user.userpass)){
                        return Integer.toString(len);

                        //return (users[i-1][7]);
                    }
                }
                //users[i][1] = id
                //users[i][3] = username
                //users[i][5] = password
                //users[i][7] = type
            }
            catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "Did not work";
        }
    }

}
