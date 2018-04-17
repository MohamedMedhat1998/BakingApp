package com.andalus.abomed7at55.bakingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andalus.abomed7at55.bakingapp.Networking.Networking;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Check for the Network state before requesting an Internet Connection
        new AsyncTask<String,Void,String>(){
            String jsonString;
            @Override
            protected String doInBackground(String... strings) {
                try {
                    jsonString = Networking.retrieveJson("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return jsonString;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("Json String" , jsonString);
            }
        }.execute();

    }
}
