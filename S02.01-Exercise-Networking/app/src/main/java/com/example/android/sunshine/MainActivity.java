/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        loadWeatherData();
    }

    private void loadWeatherData(){
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        URL url = NetworkUtils.buildUrl(location);
        new FetchWeatherTask().execute(url);
    }

    class FetchWeatherTask extends AsyncTask<URL, Void, String[]>{

        @Override
        protected String[] doInBackground(URL... urls) {
            String weather = null;
            try {
                weather = NetworkUtils.getResponseFromHttpUrl(urls[0]);
                String[] weatherArray = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, weather);
                return weatherArray;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] s) {
            if (s == null){
                mWeatherTextView.setText("Can't fetch weather data. Please try again.");
            }

            for (int i=0; i<s.length; i++){
                mWeatherTextView.append(s[i] + "\n\n");
            }
            
            /*
            try {
                JSONObject forecast = new JSONObject(s);
                JSONArray forcastList = forecast.getJSONArray("list");

                for (int i=0; i<forcastList.length(); i++){
                    JSONObject weatherItem = forcastList.getJSONObject(i);
                    JSONObject temp = weatherItem.getJSONObject("temp");
                    JSONObject weather = weatherItem.getJSONArray("weather").getJSONObject(0);
                    String weatherData = weather.getString("main") + " - " +
                            temp.getString("min")  +  " / "+ temp.getString("max");

                    mWeatherTextView.append(weatherData + "\n\n");
                }
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
        }
    }
}