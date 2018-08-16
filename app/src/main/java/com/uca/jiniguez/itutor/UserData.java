package com.uca.jiniguez.itutor;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    static String url = "http://10.23.99.82:5000";
    String mID;
    String mName;
    String mPhone;
    String mEmail;
    String mDescription;
    String mPassword;
    List<String> mSkills;
    LatLng mPosition;
    List<String> mTeachers;
    float mRating;
    private String jsonObject = "";

    public UserData(){
        mSkills = new ArrayList<>();
        mPosition = new LatLng(0,0);
        mTeachers = new ArrayList<>();
    }

    public static List<UserData> getUsers(final String skill,
                                          final Integer distance,
                                          final LatLng latlng,
                                          final Integer minimumRating){
        final List<UserData> users = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = "";
                    String nextCondition = "?";
                    String server_response = "";
                    String url = UserData.url + "/user";

                    if(skill.length()>0) {
                        url += nextCondition + "skillName=" + skill;
                        nextCondition = "&";
                    }
                    if(latlng!=null) {
                        url += nextCondition + "latitude=" + latlng.latitude + "&longitude="+latlng.longitude;
                        nextCondition = "&";
                    }
                    if(minimumRating>0) {
                        url += nextCondition + "minimumRating=" + minimumRating;
                        nextCondition = "&";
                    }
                    if(distance>0)
                        url += nextCondition + "distance=" + distance;

                    URL urlEndPoint = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) urlEndPoint.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        server_response += current;
                    }

                    json = server_response;
                    JSONArray jsonarray = new JSONArray(json);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        users.add(createUserFromJson(jsonobject));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void getRating(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Simulate network access.
                    //Thread.sleep(2000);
                    String server_response = "";
                    String url = UserData.url + "/vote/" + mID + "/rating";
                    //String url = "http://itutor-env.eu-west-3.elasticbeanstalk.com/";
                    URL urlEndPoint = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) urlEndPoint.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        char current = (char) data;
                        data = isw.read();
                        server_response += current;
                    }

                    jsonObject = server_response;
                    try{
                        mRating = Float.valueOf(jsonObject);
                    } catch (Exception e){
                        mRating = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void createUser(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(UserData.url+"/user");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("email", mEmail);
                    jsonParam.put("password", mPassword);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void uploadData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(UserData.url+"/user/"+ mID);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("name", mName);
                    jsonParam.put("phoneNum", mPhone);
                    jsonParam.put("email", mEmail);
                    jsonParam.put("quote", mDescription);
                    jsonParam.put("password", mPassword);
                    jsonParam.put("latitude", mPosition.latitude);
                    jsonParam.put("longitude", mPosition.longitude);
                    jsonParam.put("teachers", new JSONArray(mTeachers));
                    jsonParam.put("skills", new JSONArray(mSkills));

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getDataFromJson(JSONObject datos) {
        try {
            mID = datos.getString("id");
            mName = datos.getString("name");
            mPhone = datos.getString("phoneNum");
            mEmail = datos.getString("email");
            mDescription = datos.getString("quote");
            mPassword = datos.getString("password");
            mPosition = new LatLng(datos.getDouble("latitude"), datos.getDouble("longitude"));
            mTeachers.clear();
            JSONArray jsonTeachers = datos.getJSONArray("teachers");
            for (int i = 0; i < jsonTeachers.length(); i++) {
                JSONObject row = jsonTeachers.getJSONObject(i);
                mTeachers.add(row.getString("id"));
            }
            mSkills.clear();
            JSONArray jsonSkills = datos.getJSONArray("skills");
            for (int i = 0; i < jsonSkills.length(); i++) {
                JSONObject row = jsonSkills.getJSONObject(i);
                mSkills.add(row.getString("skillName"));
            }
            getRating();
        }catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static UserData createUserFromJson(JSONObject datos) {
        UserData u = new UserData();
        try {
            u.mID = datos.getString("id");
            u.mName = datos.getString("name");
            u.mPhone = datos.getString("phoneNum");
            u.mEmail = datos.getString("email");
            u.mDescription = datos.getString("quote");
            u.mPassword = datos.getString("password");
            u.mPosition = new LatLng(datos.getDouble("latitude"), datos.getDouble("longitude"));
            u.mTeachers.clear();
            JSONArray jsonTeachers = datos.getJSONArray("teachers");
            for (int i = 0; i < jsonTeachers.length(); i++) {
                JSONObject row = jsonTeachers.getJSONObject(i);
                u.mTeachers.add(row.getString("id"));
            }
            u.mSkills.clear();
            JSONArray jsonSkills = datos.getJSONArray("skills");
            for (int i = 0; i < jsonSkills.length(); i++) {
                JSONObject row = jsonSkills.getJSONObject(i);
                u.mSkills.add(row.getString("skillName"));
            }
            u.getRating();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
