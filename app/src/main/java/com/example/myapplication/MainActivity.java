package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView txtView;
    String jokeString;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.example_widget_button);
        txtView = findViewById(R.id.JokeText);

        btn.setOnClickListener(v -> new loadJoke().execute());
    }

    public class loadJoke extends AsyncTask<Void, Void, Void>
    {
        @Override
        public Void doInBackground(Void... voids)
        {
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");
            try {
                jokesModel newJoke = new jokesModel(new JSONObject(jsonString));
                jokeString = newJoke.value.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            jokeString = "";
            txtView.setText("Loading...");
        }

        @Override
        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!jokeString.equals(""))
                txtView.setText(jokeString);
        }
    }

    private String getJson (String link)
    {
        String data = "";
        try {
            URL url = new URL(link.trim());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                data = reader.readLine();
                urlConnection.disconnect();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  data;
    }
}