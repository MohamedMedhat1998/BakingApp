package com.andalus.abomed7at55.bakingapp.Networking;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.andalus.abomed7at55.bakingapp.R;

import java.io.IOException;

/**
 * This class is used to handle background threads
 */

public class MyLoader extends AsyncTaskLoader<String> {

    private String api;

    /**
     * The Constructor of the loader
     * @param context the context of the calling activity
     * @param dataSourceApi the api that contains the json data
     */
    public MyLoader(Context context , String dataSourceApi) {
        super(context);
        api = dataSourceApi;
    }

    @Override
    public String loadInBackground() {
        String jsonString = null;
        try {
            jsonString = Networking.retrieveJson(api);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}