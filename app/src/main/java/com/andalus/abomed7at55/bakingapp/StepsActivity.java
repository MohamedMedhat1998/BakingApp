package com.andalus.abomed7at55.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.widget.Button;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Adapters.StepsAdapter;
import com.andalus.abomed7at55.bakingapp.Interfaces.StepClickListener;
import com.andalus.abomed7at55.bakingapp.Recipes.Ingredient;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsActivity extends AppCompatActivity implements StepClickListener {

    private ArrayList<Recipe> recipes;
    private static Recipe selectedRecipe;
    private static ArrayList<Ingredient> exportableIngredients;
    private ArrayList<Step> stepsList;

    @BindView(R.id.rv_steps_list)
    RecyclerView stepListRecyclerView;
    @BindView(R.id.btn_steps_list_ingredients)
    Button btnIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        if(savedInstanceState == null){
            recipes = MainActivity.getExportableRecipes();
            selectedRecipe = recipes.get(convertId(getIntent().getExtras().getString(getString(R.string.recipeId))));
            exportableIngredients = selectedRecipe.getIngredients();
            stepsList = selectedRecipe.getSteps();
            stepListRecyclerView.setNestedScrollingEnabled(false);
            StepsAdapter adapter = new StepsAdapter(stepsList,this);
            stepListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            stepListRecyclerView.setAdapter(adapter);
        }else{
            stepsList = savedInstanceState.getParcelableArrayList(getString(R.string.recipeSteps));
            StepsAdapter adapter = new StepsAdapter(stepsList,this);
            stepListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            stepListRecyclerView.setAdapter(adapter);
        }


    }

    @OnClick(R.id.btn_steps_list_ingredients)
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

    /**
     * This method is used to retrieve all the steps of the selected recipe
     * @return steps ArrayList
     */
    public static ArrayList<Step> getExportableSteps(){
        return selectedRecipe.getSteps();
    }

    /**
     * This method is used to get the ingredients of the selected recipe
     * @return ingredients ArrayList
     */
    public static ArrayList<Ingredient> getExportableIngredients() {
        return exportableIngredients;
    }

    @Override
    public void onStepClicked(Step selectedStep) {
        Intent i = new Intent(this,DetailsActivity.class);
        i.putExtra(getString(R.string.keySelectedStep),selectedStep);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.recipeSteps),stepsList);
    }
}
