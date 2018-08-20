package com.uca.jiniguez.itutor;

import org.json.JSONException;
import org.json.JSONObject;

class VoteData {
    String voterUser;
    String comment;
    Integer rating;

    public static VoteData createVoteFromJson(JSONObject datos) {
        VoteData v = new VoteData();
        try {
            v.voterUser = datos.getString("voterName");

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
}
