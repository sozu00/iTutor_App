package com.uca.jiniguez.itutor;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserData {
    String userID;
    String userName;
    String phoneNumber;
    String email;
    String quote;
    List<String> skills = new ArrayList<>();
    LatLng position;
    List<String> teachers = new ArrayList<>();

    public UserData(){
        //downloadData();
    }

    public void downloadData(){
        //buscar(userID);
        skills.clear();
        skills.add("Matematicas");
        skills.add("Lengua");
        skills.add("Bicicleta");
        userName = "Jesus Iniguez";
        phoneNumber = "956123123";
        email = "correo@correo.com";
        quote = "Me gusta mucho escribir y escribir bla bla bla esto es lo mejor del mundo mundial";
        teachers.clear();
        teachers.add("id_DB_USER1");
        teachers.add("id_DB_USER2");
        position = new LatLng(40.5053817,-3.6812296);
    }

    public void uploadData(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.23.99.82:5000/user/"+userID);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PUT");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("name",userName);
                    jsonParam.put("phoneNum",phoneNumber);
                    jsonParam.put("email",email);
                    jsonParam.put("quote",quote);
                    jsonParam.put("latitude",position.latitude);
                    jsonParam.put("longitude",position.longitude);
                    jsonParam.put("teachers", new JSONArray(teachers));
                    jsonParam.put("skills", new JSONArray(skills));

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
            //datos.put("name", "nombre?de?test");
            userID = datos.getString("id");
            userName = datos.getString("name");
            phoneNumber = datos.getString("phoneNum");
            email = datos.getString("email");
            quote = datos.getString("quote");
            position = new LatLng(datos.getDouble("latitude"), datos.getDouble("longitude"));
            teachers.clear();
            JSONArray jsonTeachers = datos.getJSONArray("teachers");
            for (int i = 0; i < jsonTeachers.length(); i++) {
                JSONObject row = jsonTeachers.getJSONObject(i);
                teachers.add(row.getString("id"));
            }
            skills.clear();
            JSONArray jsonSkills = datos.getJSONArray("skills");
            for (int i = 0; i < jsonSkills.length(); i++) {
                JSONObject row = jsonSkills.getJSONObject(i);
                skills.add(row.getString("skillName"));
            }
        }catch (JSONException e) {
            e.printStackTrace();

        }
    }
}
