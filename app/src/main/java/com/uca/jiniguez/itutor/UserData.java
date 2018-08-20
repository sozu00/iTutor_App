package com.uca.jiniguez.itutor;

import android.util.Log;
import android.widget.Toast;

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
import java.util.Optional;

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

    public List<VoteData> getVotes(){
        final List<VoteData> votes = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Simulate network access.
                    //Thread.sleep(2000);
                    String server_response = "";
                    String url = UserData.url + "/vote/" + mID;
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

                    String json = server_response;
                    JSONArray jsonarray = new JSONArray(json);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);
                        votes.add(VoteData.createVoteFromJson(jsonobject));
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

        return votes;
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
            mName = mName.equals("null")?"":mName;

            mPhone = datos.getString("phoneNum");
            mPhone = mPhone.equals("null")?"":mPhone;

            mEmail = datos.getString("email");

            mDescription = datos.getString("quote");
            mDescription = mDescription.equals("null")?"":mDescription;

            mPassword = datos.getString("password");
            try {
                mPosition = new LatLng(datos.getDouble("latitude"), datos.getDouble("longitude"));
            }
            catch (Exception e){
                mPosition = new LatLng(0,0);
            }
            mTeachers.clear();
            JSONArray jsonTeachers = datos.getJSONArray("teachers");
            for (int i = 0; i < jsonTeachers.length(); i++)
                mTeachers.add(jsonTeachers.getString(i));

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
        u.getDataFromJson(datos);
        return u;
    }

    public static UserData findUser(final String teacher) {
        final UserData[] u = {new UserData()};
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json;
                    String server_response = "";
                    String url = UserData.url + "/user/"+teacher;
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
                    JSONObject jsonobject = new JSONObject(json);
                    u[0] = createUserFromJson(jsonobject);
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
        return u[0];
    }


}
