package com.uca.jiniguez.itutor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    List<String> mTeachers;
    float mRating;
    Integer formacion = 0;
    List<Boolean> levels;
    float price = 0;
    private String jsonObject = "";
    String newProfilePic = "";
    Boolean isFavourite = false;

    public UserData(){
        mSkills = new ArrayList<>();
        mTeachers = new ArrayList<>();
        levels = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            levels.add(Boolean.FALSE);
        }
    }

    public static List<UserData> getUsers(
            final String skill,
            final Integer minimumRating,
            final Integer maxPrice,
            final Integer formation,
            final List<Boolean> levels
    ){
        final List<UserData> users = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json;
                    String nextCondition = "?";
                    String server_response = "";
                    String url = UserData.url + "/user";
                    if(formation>0){
                        url+= "?formation="+ formation;
                        nextCondition = "&";
                    }
                    if(skill.length()>0) {
                        url +=nextCondition + "skillName=" + skill;
                        nextCondition = "&";
                    }
                    if(minimumRating>0) {
                        url +=nextCondition + "minimumRating=" + minimumRating;
                        nextCondition = "&";
                    }
                    if(maxPrice<99){
                        url +=nextCondition + "maxPrice=" + maxPrice;
                        nextCondition = "&";
                    }
                    for(Boolean level : levels){
                        url+=nextCondition + "level="+level;
                        nextCondition = "&";
                    }
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
                    jsonParam.put("teachers", new JSONArray(mTeachers));
                    jsonParam.put("skills", new JSONArray(mSkills));

                    jsonParam.put("formation", formacion);
                    jsonParam.put("levels", new JSONArray(levels));
                    jsonParam.put("price", price);

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

            formacion = datos.getInt("formation");

            levels.clear();
            JSONArray jsonLevels = datos.getJSONArray("levels");
            for (int i = 0; i < jsonTeachers.length(); i++)
                levels.add(jsonLevels.getBoolean(i));

            price = (float) datos.getDouble("price");

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
