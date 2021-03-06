package com.andalus.abomed7at55.bakingapp.Recipes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * This class represents a single recipe. It contains all the characteristics related to the recipe
 */

public class Recipe implements Parcelable{

    private String id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private String servings;
    private String image;

    public Recipe(String id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, String servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        servings = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * This is a special method to extract the data from the ingredient list
     * @return Array list of string
     */
    public ArrayList<String> getDataAsArrayListOfStrings(){
        ArrayList<String> result = new ArrayList<>();
        int n = getIngredients().size();
        for(int i = 0 ; i < n ; i++){
            result.add(getIngredients().get(i).getFullIngredient());
        }
        return result;
    }

    public String getOneTextIngredients() {
        StringBuilder full = new StringBuilder();
        int n = ingredients.size();
        for (int i = 0; i < n; i++) {
            full.append(ingredients.get(i).getFullIngredient()).append("\n");
        }
        return full.toString();
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(servings);
        parcel.writeString(image);
    }
}
