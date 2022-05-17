package com.example.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class newAppWidget extends AppWidgetProvider {

    String jokeString;
    RemoteViews remoteViews;
    ComponentName watchWidget;
    AppWidgetManager appWidgetManager;
    private static final String SYNC_CLICKED = "MY_PACKAGE_NAME.WIDGET_BUTTON";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        watchWidget = new ComponentName(context, newAppWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.GetJokeBtn, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            appWidgetManager = AppWidgetManager.getInstance(context);

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            watchWidget = new ComponentName(context, newAppWidget.class);

            new JokeLoader().execute();

            appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public class JokeLoader extends AsyncTask<Void, Void, Void>
    {
        @Override
        public Void doInBackground(Void... voids)
        {
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");
            try {
                jokesModel newJoke = new jokesModel(new JSONObject(jsonString));
                jokeString = newJoke.value.toString();
            } catch (JSONException e) {
                System.out.println("Something went wrong");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            jokeString = "";
            remoteViews.setTextViewText(R.id.textJoke, "Loading...");
        }

        @Override
        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!jokeString.equals("")) {
                System.out.println(jokeString);
                remoteViews.setTextViewText(R.id.JokeText, jokeString);
                appWidgetManager.updateAppWidget(watchWidget, remoteViews);
            }
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