package com.andalus.abomed7at55.bakingapp.Networking;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * This class is created to handle all networking operations
 */

public class Networking {

    /**
     * This method is used to connect to a specific Api
     * @param url The target API
     * @return the HttpURLConnection used to make the connection
     */
    private static HttpURLConnection startConnection(String url) throws IOException {
        URL mUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) mUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        return urlConnection;
    }

    /**
     * This method is used to get the json string from a given api
     * @param api the target api
     * @return json string
     * @throws IOException if the given api is invalid
     */
    public static String retrieveJson(String api) throws IOException {
        HttpURLConnection httpURLConnection = startConnection(api);
        InputStream in = httpURLConnection.getInputStream();
        InputStreamReader reader = new InputStreamReader(in);
        Scanner scanner = new Scanner(reader);
        scanner.useDelimiter("\\A");
        return scanner.next();
    }


}
