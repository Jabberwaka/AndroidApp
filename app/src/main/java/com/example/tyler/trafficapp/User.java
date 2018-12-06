package com.example.tyler.trafficapp;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static android.support.constraint.Constraints.TAG;

public class User {


     static String count = "0";
     static String username;
     static String userpass;
     static int type = -1;String data = "";


     static String ip = "192.168.1.118";
     static String protocol = "http://";
     static String urlS = ":33117/restful_camera/webresources/com.mycompany.restful_camera.users/count";
     static String urlGetUsers = ":33117/restful_camera/webresources/com.mycompany.restful_camera.users/0/";
     static String urlP = ":33117/restful_camera/webresources/com.mycompany.restful_camera.users/";


    //constructor puts in username and scrambles the
    public User (String uN, String uP){
        this.username = uN;
        this.userpass = scramble(uP);
    }

    public String getUsername(){
        return username;
    }

    public String getUserpass(){
        return userpass;
    }

    public String getCount() {return count;}

    public int getType() {return type;}

    public String getData() {return data;};

    //function to scramble the password of the user for security purposes
    private String scramble (String uP){
        String passScramble = "";

        for (int i = 0; i < uP.length()-1; ++i){
            if(uP.charAt(i) > 'Z'){
                passScramble+=(Integer.toString(uP.charAt(i)-i));
            }
            else{
                passScramble+=(Integer.toString(uP.charAt(i)+i));
            }

            //salting
            if(i == uP.length()/2){
                passScramble+=("0x00");
            }

        }
        return passScramble;
    }


    public void passwordScrambler (){
        if(userpass != null){
            userpass = scramble(userpass);
        }
    }

    //checks to see if the user is already in the database
    //returns the user type, 0 for normal, 1 for admin, -1 for not in database
    public int validateUser(){

       // Counting c = new Counting();
       // c.execute();

        new Validate().execute();

        if(type >= 0){
            return Integer.parseInt(getCount());
        }

        return -1;
    }


    //this function goes through all the user id's and finds the newest id that is +1 larger than the largest ID
    public int returnValidId(){
        int id = 0;

        try {

            String urlString = protocol+ip+urlS;

            Counting c = new Counting();
            c.execute();

            URL url = null;

            String urlUser = protocol+ip+urlGetUsers+count;

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
                if(Integer.parseInt(users[i-1][1]) > id){
                    id = Integer.parseInt(users[i-1][1]) + 1;
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
        return id;
    }


    public boolean addUserToDatabase(){

        //check to see if the user is already in the database
        //a negative number means it wasnt found in the database

     //   if(validateUser() > 0){
    //        return false;
    //    }

        int id = returnValidId();

        //creates a regular user
        String thePut = "{\"userId\":"+ id +",\"userName\":\" " +username+ "\",\"userPass\":\"" + userpass + "\",\"userType\":0}";
        String urlStringPut = protocol+ip+urlP+id;
        //HTTP PUT WORKS
        try {
            URL urler = new URL(urlStringPut);
            HttpURLConnection httpCon = (HttpURLConnection) urler.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-Type", "application/json");

            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(thePut);
            out.close();
            httpCon.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



     class Counting extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;
            String c = "100";


            try {
                url = new URL(protocol+ip+urlS);
            } catch (MalformedURLException e) {
                count = "harp me";
                e.printStackTrace();
            }

            URLConnection conn = null;
            count = "jorp";

            try {
                conn = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
                count = "really help me";
            }

            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                count = "please god help me";
            }
            try {
               c  = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                count = "please hindu gods help me";

            }

            //count = c;
            return null;
        }
    }

    private class Validate extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            try {
                String urlUser = User.protocol+User.ip+User.urlGetUsers+"1000";

                //getting users
                URL url2 = null;
                Log.d(TAG, "the goddamn hell: ");

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

                    if(users[i-1][3].equals(User.username) && users[i-1][5].equals(User.userpass)){
                        return (users[i-1][7]);
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            type = Integer.parseInt(s);
        }
    }
}

