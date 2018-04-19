package com.andalus.abomed7at55.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;

import com.andalus.abomed7at55.bakingapp.Adapters.StepsAdapter;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsActivity extends AppCompatActivity {

    private ArrayList<Recipe> recipes;
    private Recipe selectedRecipe;

    @BindView(R.id.rv_steps_list)
    RecyclerView stepListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        //TODO add a click listener for the Ingredients text view and make it open an activity containing a fragment with a recycler view containing the ingredients
        ButterKnife.bind(this);

        recipes = MainActivity.getExportableRecipes();
        selectedRecipe = recipes.get(convertId(getIntent().getExtras().getString(getString(R.string.recipeId))));

        StepsAdapter adapter = new StepsAdapter(selectedRecipe.getSteps());
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepListRecyclerView.setAdapter(adapter);
        //TODO set up the details activity (Use fragments)

    }

    /**
     * This method is used to convert the string id to numeric id
     * @param stringId the target id to be converted
     * @return a numeric id resulted from the converting operation
     */
    private int convertId(String stringId){
        int id = Integer.parseInt(stringId);
        id--;
        return id;
    }
}
