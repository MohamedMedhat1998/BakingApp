package com.andalus.abomed7at55.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andalus.abomed7at55.bakingapp.Networking.MyLoader;
import com.andalus.abomed7at55.bakingapp.Networking.Networking;
import com.andalus.abomed7at55.bakingapp.Recipes.JsonParser;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final int LOADER_ID = 1;
    private JsonParser mJsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJsonParser = new JsonParser(this);

        if(isOnline()){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(LOADER_ID,null,this).forceLoad();
        }else{
            //TODO support onSavedInstanceState
            Toast.makeText(getApplicationContext(),getString(R.string.noInternetMessage),Toast.LENGTH_LONG).show();
            //TODO Load the cached Items
        }



    }

    private boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }
    //Loader callbacks
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new MyLoader(this,getString(R.string.api));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d("data",data);
        //TODO create a RecyclerView and an adapter and populate the data here
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    //-------------------


}
