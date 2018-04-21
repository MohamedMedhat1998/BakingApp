package com.andalus.abomed7at55.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andalus.abomed7at55.bakingapp.Adapters.IngredientsAdapter;
import com.andalus.abomed7at55.bakingapp.Recipes.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsDetailsActivity extends AppCompatActivity {

    private ArrayList<Ingredient> ingredientList;
    @BindView(R.id.rv_ingredients)
    RecyclerView ingredientsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);
        //TODO Support onSaveInstanceState

        ButterKnife.bind(this);

        ingredientList = StepsActivity.getExportableIngredients();
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        IngredientsAdapter adapter = new IngredientsAdapter(getData(ingredientList));
        ingredientsRecyclerView.setAdapter(adapter);
    }

    /**
     * This is a special method to extract the data from the ingredient list
     * @param dataSource array list of {@link Ingredient} objects
     * @return Array list of string
     */
    private ArrayList<String> getData(ArrayList<Ingredient> dataSource){
        ArrayList<String> result = new ArrayList<>();
        int n = dataSource.size();
        for(int i = 0 ; i < n ; i++){
            result.add(dataSource.get(i).getFullIngredient());
        }
        return result;
    }
}
