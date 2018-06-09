package com.andalus.abomed7at55.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static final String INGREDIENT_FRAGMENT_TAG = "ingredient_Tag";
    private static final String VIDEO_FRAGMENT_TAG = "video_Tag";
    private static final String IS_TABLET_KEY = "is_tablet_state_key";
    private static final String DISPLAYED_FRAGMENT_KEY = "current_fragment";
    private static final String RECIPE_KEY = "recipe_key";
    private static final String LAST_SEEN_STEP_KEY = "last_seen_step_key";
    private final static String ROTATION_ID_KEY = "id_on_rotation";

    private static final int TABLET = 5;
    private static final int NOT_TABLET = 10;
    private static final int FRAGMENT_INGREDIENTS = 15;
    private static final int FRAGMENT_DETAILS = 20;

    private ArrayList<Recipe> recipes;
    private static Recipe selectedRecipe;
    private static ArrayList<Ingredient> exportableIngredients;
    private ArrayList<Step> stepsList;
    private boolean isTablet = false;
    private int isTabletState = NOT_TABLET;
    private int currentDisplayedFragment;
    private Step lastSeenStep;
    private static long currentPosition;
    private String rotationId;

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

            setUpDefaultScreenForTablets();
        }else{
            isTabletState = savedInstanceState.getInt(IS_TABLET_KEY);
            if(isTabletState == TABLET){
                isTablet = true;
            }
            stepsList = savedInstanceState.getParcelableArrayList(getString(R.string.recipeSteps));
            StepsAdapter adapter = new StepsAdapter(stepsList,this);
            stepListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            stepListRecyclerView.setAdapter(adapter);
            if(isTablet){
                selectedRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
                currentDisplayedFragment = savedInstanceState.getInt(DISPLAYED_FRAGMENT_KEY);
                lastSeenStep = savedInstanceState.getParcelable(LAST_SEEN_STEP_KEY);

                currentPosition = savedInstanceState.getLong(getString(R.string.play_position));
                Log.d("Current Pos Steps Act",currentPosition + "");

                rotationId = savedInstanceState.getString(ROTATION_ID_KEY);

                if(currentDisplayedFragment == FRAGMENT_INGREDIENTS){
                    setUpDefaultScreenForTablets();
                }else if(currentDisplayedFragment == FRAGMENT_DETAILS){
                    Log.d("LOADED FROM","FRAGMENT_DETAILS");
                    Log.d("Last Seen Step",lastSeenStep.getShortDescription());
                    displayDetailsFragment(lastSeenStep);
                }
            }
        }
        //TODO save play position - play/pause state

    }

    /**
     * This method is responsible for making the app suitable for tablets
     */
    private void setUpDefaultScreenForTablets(){
        if(findViewById(R.id.fragment_details_container_tablet)!= null){
            isTablet = true;
            isTabletState = TABLET;
            FragmentIngredients fragmentIngredients = new FragmentIngredients();

            fragmentIngredients.setFlag(FragmentIngredients.FLAG_TABLET);
            fragmentIngredients.setData(selectedRecipe.getDataAsArrayListOfStrings());

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_details_container_tablet,fragmentIngredients,INGREDIENT_FRAGMENT_TAG);
            currentDisplayedFragment = FRAGMENT_INGREDIENTS;
            fragmentTransaction.commit();

        }
    }

    @OnClick(R.id.btn_steps_list_ingredients)
    void openIngredientsList(){
        if(!isTablet){
            startActivity(new Intent(this,IngredientsDetailsActivity.class));
        }else{
            FragmentIngredients fragmentIngredients =
                    (FragmentIngredients) getSupportFragmentManager().findFragmentByTag(INGREDIENT_FRAGMENT_TAG);
            if(fragmentIngredients == null || !fragmentIngredients.isVisible()){
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
            displayDetailsFragment(selectedStep);
        }
    }

    /**
     * This method displays the fragment of the details. The content of the displayed fragment is based on the passed step
     * @param selectedStep the step from which the fragment will get its content
     */
    private void displayDetailsFragment(Step selectedStep){
        Log.d("Location","Start");
        FragmentVideoWithInstructions tempFragment =
                (FragmentVideoWithInstructions) getSupportFragmentManager().findFragmentByTag(VIDEO_FRAGMENT_TAG);
        if(tempFragment != null){
            Log.d("Location","NOT Null");
            if(tempFragment.isVisible()){
                tempFragment.setPlayPosition(currentPosition);
                tempFragment.setIdOnRotation(rotationId);
                tempFragment.updateContent(selectedStep);
                Log.d("Location","Visible");
            }
        }else {
            Log.d("Location","else");
            FragmentVideoWithInstructions fragmentVideoWithInstructions = new FragmentVideoWithInstructions();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentVideoWithInstructions.setFlag(FragmentVideoWithInstructions.FLAG_TABLET);
            fragmentVideoWithInstructions.setSelectedStep(selectedStep);
            fragmentVideoWithInstructions.setPlayPosition(currentPosition);
            fragmentVideoWithInstructions.setIdOnRotation(rotationId);

            fragmentTransaction.replace(R.id.fragment_details_container_tablet,fragmentVideoWithInstructions, VIDEO_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
        currentDisplayedFragment = FRAGMENT_DETAILS;
        lastSeenStep = selectedStep;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.recipeSteps),stepsList);
        if(isTablet){
            outState.putInt(IS_TABLET_KEY,isTabletState);
            outState.putInt(DISPLAYED_FRAGMENT_KEY,currentDisplayedFragment);
            outState.putParcelable(RECIPE_KEY,selectedRecipe);
            outState.putParcelable(LAST_SEEN_STEP_KEY,lastSeenStep);
            outState.putLong(getString(R.string.play_position),currentPosition);
            outState.putString(ROTATION_ID_KEY,lastSeenStep.getId());
            Log.d("current pos sv st act",currentPosition + "");
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            super.onSaveInstanceState(outState);
        }
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

    public static void setCurrentPosition(long currentPlayPosition) {
        currentPosition = currentPlayPosition;
    }
}
