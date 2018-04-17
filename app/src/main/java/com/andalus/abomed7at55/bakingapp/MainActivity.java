package com.andalus.abomed7at55.bakingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andalus.abomed7at55.bakingapp.Networking.Networking;
import com.andalus.abomed7at55.bakingapp.Recipes.JsonParser;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

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
                    jsonString = Networking.retrieveJson(getBaseContext().getString(R.string.api));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return jsonString;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("Json String" , s);
                JsonParser jsonParser = new JsonParser(getApplicationContext());
                try {
                    ArrayList<Recipe> myArrayList = jsonParser.getRecipeArrayList(s);
                    Log.d("ArrayList" ,"Success");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }
}
