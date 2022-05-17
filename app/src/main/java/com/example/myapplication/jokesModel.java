package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.List;

public class jokesModel {

    List<String> categories;
    String created_at;
    URL icon_url;
    String id;
    String updated_at;
    URL url;
    String value;

    jokesModel(JSONObject json)  {
        try {

            JSONArray a = json.getJSONArray("categories");

            for (int i = 0; i<a.length(); i++)
                categories.add(a.get(i).toString());

            this.created_at = json.getString("created_at");
            this.icon_url = new URL(json.getString("icon_url"));
            this.id = json.getString("id");
            this.updated_at = json.getString("updated_at");
            this.url = new URL(json.getString("url"));
            this.value = json.getString("value");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "jokeModel: " +
                "categories = " + categories +
                ", created at = " + created_at +
                ", icon url = " + icon_url +
                ", id = '" + id + '\'' +
                ", updated at = " + updated_at +
                ", url = " + url +
                ", joke = '" + value + '\''
                ;
    }
}