package com.andalus.abomed7at55.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andalus.abomed7at55.bakingapp.Adapters.RecipeAdapter;
import com.andalus.abomed7at55.bakingapp.Interfaces.RecipeClickListener;
import com.andalus.abomed7at55.bakingapp.Networking.MyLoader;
import com.andalus.abomed7at55.bakingapp.Recipes.JsonParser;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> , RecipeClickListener {

    private static final int LOADER_ID = 1;
    private JsonParser mJsonParser;
    private static ArrayList<Recipe> exportableRecipes;
    private ArrayList<Recipe> recipes;
    private RecipeAdapter adapter;

    @BindView(R.id.recipe_recycler_view)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar indicatorProgressBar;
    @BindView(R.id.tv_main_no_connection)
    TextView tvNoConnection;
    //TODO add widgets
    //TODO add unit tests
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mJsonParser = new JsonParser(this);
        if(savedInstanceState != null){
            loadSavedData(savedInstanceState);
        }else {
            startLoadingData();
        }

    }

    /**
     * This method is used when the savedInstanceState is not available
     */
    private void startLoadingData(){
        if(isOnline()){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(LOADER_ID,null,this).forceLoad();
        }else{
            tvNoConnection.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method is used when the savedInstanceState is available
     */
    private void loadSavedData(Bundle savedState){
        recipes = savedState.getParcelableArrayList(getString(R.string.recipeArrayKey));
        if(recipes == null){
            startLoadingData();
            return;
        }
        exportableRecipes = recipes;
        adapter = new RecipeAdapter(recipes,this);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeRecyclerView.setAdapter(adapter);

    }

    /**
     * This method checks the internet state of the device
     * @return true if there is an active connection
     */
    private boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * The getter method of the recipes
     * @return recipe ArrayList
     */
    public static ArrayList<Recipe> getExportableRecipes(){
        return exportableRecipes;
    }

    //Loader callbacks
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        indicatorProgressBar.setVisibility(View.VISIBLE);
        return new MyLoader(this,getString(R.string.api));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        indicatorProgressBar.setVisibility(View.INVISIBLE);
        try {
            recipes = mJsonParser.getRecipeArrayList(data);
            exportableRecipes = recipes;
            adapter = new RecipeAdapter(recipes,this);
            recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            recipeRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    //-------------------

    @Override
    public void onRecipeClicked(String recipeId) {
        Intent i = new Intent(this,StepsActivity.class);
        i.putExtra(getString(R.string.recipeId),recipeId);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.recipeArrayKey),recipes);
    }
}
