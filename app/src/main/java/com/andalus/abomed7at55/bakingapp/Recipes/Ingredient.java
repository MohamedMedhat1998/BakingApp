package com.andalus.abomed7at55.bakingapp.Recipes;

/**
 * This class represents a single ingredient of the recipe
 */
public class Ingredient{

    private String quantity;
    private String measure;
    private String ingredient;

    Ingredient(String quantity, String measure , String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
    /**
     * This method returns a full description of the ingredient
     * @return a string with a full description of the ingredient
     */
    public String getFullIngredient(){
        return quantity + " " + measure + " of " + ingredient;
    }
}
