package com.andalus.abomed7at55.bakingapp.Recipes;

import java.util.ArrayList;

/**
 * This class represents a single recipe. It contains all the characteristics related to the recipe
 */

public class Recipe {

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
}
