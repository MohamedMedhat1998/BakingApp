package com.andalus.abomed7at55.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Adapters.StepsAdapter;
import com.andalus.abomed7at55.bakingapp.Interfaces.StepClickListener;
import com.andalus.abomed7at55.bakingapp.Recipes.Ingredient;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsActivity extends AppCompatActivity implements StepClickListener {

    private ArrayList<Recipe> recipes;
    private Recipe selectedRecipe;
    private static ArrayList<Ingredient> exportableIngredients;

    @BindView(R.id.rv_steps_list)
    RecyclerView stepListRecyclerView;
    @BindView(R.id.tv_steps_list_ingredients)
    TextView tvIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);

        recipes = MainActivity.getExportableRecipes();
        selectedRecipe = recipes.get(convertId(getIntent().getExtras().getString(getString(R.string.recipeId))));
        exportableIngredients = selectedRecipe.getIngredients();

        StepsAdapter adapter = new StepsAdapter(selectedRecipe.getSteps(),this);
        stepListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stepListRecyclerView.setAdapter(adapter);
        //TODO set up the details activity (Use fragments)

    }

    @OnClick(R.id.tv_steps_list_ingredients)
    void openIngredientsList(){
        startActivity(new Intent(this,IngredientsDetailsActivity.class));
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

    public static ArrayList<Ingredient> getExportableIngredients() {
        return exportableIngredients;
    }

    @Override
    public void onStepClicked() {
        //TODO Edit this step click
        startActivity(new Intent(this,DetailsActivity.class));
    }
}