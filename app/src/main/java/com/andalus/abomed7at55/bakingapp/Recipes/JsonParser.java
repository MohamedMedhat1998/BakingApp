package com.andalus.abomed7at55.bakingapp.Recipes;

import android.content.Context;

import com.andalus.abomed7at55.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class is used to generate {@link Recipe} ArrayList based on a given Json String
 */

public class JsonParser {

    private Context context;
    private ArrayList<Recipe> recipesPackage;
    //recipe properties
    private String idRecipe;
    private String nameRecipe;
    private ArrayList<Ingredient> ingredientsRecipe;
    private ArrayList<Step> stepsRecipe;
    private String servingsRecipe;
    private String imageRecipe;

    //Ingredient properties
    private String quantityIngredient;
    private String measureIngredient;
    private String ingredientIngredient;

    //Step properties
    private String idStep;
    private String shortDescriptionStep;
    private String descriptionStep;
    private String videoURLStep;
    private String thumbnailURLStep;


    /**
     * The normal constructor
     * @param context the context of the calling activity
     */
    public JsonParser(Context context){
        this.context = context;
    }

    /**
     * This method is used to extract recipe object from a given json string
     * @param jsonString the string formatted as json
     * @return the recipe ArrayList
     * @throws JSONException if the given json string is incorrect
     */
    public ArrayList<Recipe> getRecipeArrayList(String jsonString) throws JSONException {

        recipesPackage = new ArrayList<>();

        Recipe recipeEntry;
        Ingredient ingredientEntry;
        Step stepEntry;

        JSONArray recipeArray = new JSONArray(jsonString);
        int recipeArraySize = recipeArray.length();

        JSONArray ingredientArray;
        int ingredientArraySize;

        JSONArray stepArray;
        int stepArraySize;

        JSONObject jsonRecipe;
        JSONObject jsonIngredient;
        JSONObject jsonStep;
        for(int i = 0 ; i < recipeArraySize ; i++){
            ingredientsRecipe = new ArrayList<>();
            stepsRecipe = new ArrayList<>();

            jsonRecipe = recipeArray.getJSONObject(i);
            idRecipe = jsonRecipe.getString(context.getString(R.string.recipeId));
            nameRecipe = jsonRecipe.getString(context.getString(R.string.recipeName));

            ingredientArray = jsonRecipe.getJSONArray(context.getString(R.string.recipeIngredients));
            ingredientArraySize = ingredientArray.length();
            for(int j = 0 ; j < ingredientArraySize ; j++){
                jsonIngredient = ingredientArray.getJSONObject(j);
                quantityIngredient = jsonIngredient.getString(context.getString(R.string.ingredientQuantity));
                measureIngredient = jsonIngredient.getString(context.getString(R.string.ingredientMeasure));
                ingredientIngredient = jsonIngredient.getString(context.getString(R.string.ingredientIngredient));
                ingredientEntry = new Ingredient(quantityIngredient,measureIngredient,ingredientIngredient);
                ingredientsRecipe.add(ingredientEntry);
            }

            stepArray = jsonRecipe.getJSONArray(context.getString(R.string.recipeSteps));
            stepArraySize = stepArray.length();
            for (int z = 0 ; z < stepArraySize ; z++){
                jsonStep = stepArray.getJSONObject(z);
                idStep = jsonStep.getString(context.getString(R.string.stepId));
                shortDescriptionStep = jsonStep.getString(context.getString(R.string.stepShortDescription));
                descriptionStep = jsonStep.getString(context.getString(R.string.stepDescription));
                videoURLStep = jsonStep.getString(context.getString(R.string.stepVideoURL));
                thumbnailURLStep = jsonStep.getString(context.getString(R.string.stepThumbnailURL));
                stepEntry = new Step(idStep,shortDescriptionStep,descriptionStep,videoURLStep,thumbnailURLStep);
                stepsRecipe.add(stepEntry);
            }

            servingsRecipe = jsonRecipe.getString(context.getString(R.string.recipeServings));
            imageRecipe = jsonRecipe.getString(context.getString(R.string.recipeImage));
            recipeEntry = new Recipe(idRecipe,nameRecipe,ingredientsRecipe,stepsRecipe,servingsRecipe,imageRecipe);
            recipesPackage.add(recipeEntry);
        }
        return recipesPackage;
    }
}
