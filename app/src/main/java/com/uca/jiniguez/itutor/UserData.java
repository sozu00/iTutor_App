package com.uca.jiniguez.itutor;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    }

    public void getDataFromJson(JSONObject datos) {
        try {
            datos.put("name", "nombre?de?test");
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
