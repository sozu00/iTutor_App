package com.uca.jiniguez.itutor;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class VoteData {
    String voterName;
    String receivingUser;
    String voterID;
    String comment;
    Integer rating;

    public static VoteData createVoteFromJson(JSONObject datos) {
        VoteData v = new VoteData();
        try {
            v.voterName = datos.getString("voterName");
            v.voterID = datos.getString("voterUser");
            v.receivingUser = datos.getString("receivingUser");
            v.comment = datos.getString("comment");
            v.comment = v.comment.equals("null")?"":v.comment;
            try {
                v.rating = datos.getInt("rating");
            }catch(Exception e){
                v.rating = 0;
            }
        }catch (JSONException e) {
            e.printStackTrace();

        }
        return v;
    }

    public void voteUser(){
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL(UserData.url+"/vote");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept","application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("voterUser", voterID);
                jsonParam.put("voterName", voterName);
                jsonParam.put("receivingUser", receivingUser);
                jsonParam.put("comment", comment);
                jsonParam.put("rating", rating);

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
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
