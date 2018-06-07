package com.andalus.abomed7at55.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;

import com.andalus.abomed7at55.bakingapp.Adapters.StepsAdapter;
import com.andalus.abomed7at55.bakingapp.Interfaces.StepClickListener;
import com.andalus.abomed7at55.bakingapp.Recipes.Ingredient;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;
import com.andalus.abomed7at55.bakingapp.UI.FragmentIngredients;
import com.andalus.abomed7at55.bakingapp.UI.FragmentVideoWithInstructions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsActivity extends AppCompatActivity implements StepClickListener {

    private ArrayList<Recipe> recipes;
    private static Recipe selectedRecipe;
    private static ArrayList<Ingredient> exportableIngredients;
    private ArrayList<Step> stepsList;
    private boolean isTablet = false;

    @BindView(R.id.rv_steps_list)
    RecyclerView stepListRecyclerView;
    @BindView(R.id.btn_steps_list_ingredients)
    Button btnIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(MainActivity.getExportableRecipes().get(Integer.parseInt(getIntent().getExtras().getString(getString(R.string.recipeId))) - 1).getName());
        }catch (Exception e){
            e.printStackTrace();
        }
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

        setUpDefaultScreenForTablets();

    }

    /**
     * This method is responsible for making the app suitable for tablets
     */
    private void setUpDefaultScreenForTablets(){
        if(findViewById(R.id.fragment_video_instructions_in_details_activity)!= null){
            isTablet = true;
            FragmentIngredients fragmentIngredients = new FragmentIngredients();

            fragmentIngredients.setFlag(FragmentIngredients.FLAG_TABLET);
            fragmentIngredients.setData(selectedRecipe.getDataAsArrayListOfStrings());

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_video_instructions_in_details_activity,fragmentIngredients);
            fragmentTransaction.commit();

        }
    }

    @OnClick(R.id.btn_steps_list_ingredients)
    void openIngredientsList(){
        if(!isTablet){
            startActivity(new Intent(this,IngredientsDetailsActivity.class));
        }else{
            //TODO fix this error (find another way to know whether the fragment is displayed or not)
            if(findViewById(R.id.fragment_ingredient_layout)!= null){
                setUpDefaultScreenForTablets();
            }
        }
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
        if(!isTablet){
            Intent i = new Intent(this,DetailsActivity.class);
            i.putExtra(getString(R.string.keySelectedStep),selectedStep);
            startActivity(i);
        }else{
            FragmentVideoWithInstructions fragmentVideoWithInstructions = new FragmentVideoWithInstructions();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.recipeSteps),stepsList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == ActionBar.DISPLAY_HOME_AS_UP){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is used to retrieve the id of the selected recipe
     *
     * @return String id of the selected recipe
     */
    public static String getId() {
        return selectedRecipe.getId();
    }
}
