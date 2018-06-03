package com.andalus.abomed7at55.bakingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.CheckBox;

import com.andalus.abomed7at55.bakingapp.Adapters.IngredientsAdapter;
import com.andalus.abomed7at55.bakingapp.Recipes.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class IngredientsDetailsActivity extends AppCompatActivity {

    private ArrayList<Ingredient> ingredientList;
    private ArrayList<String> data;

    @BindView(R.id.ch_display_in_widget)
    CheckBox displayInWidget;
    private SharedPreferences sharedPreferences;
    private boolean isPreference;

    @BindView(R.id.rv_ingredients)
    RecyclerView ingredientsRecyclerView;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = sharedPreferences.getString(getString(R.string.preferences_id), "");
        isPreference = id.equals(StepsActivity.getId());

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            e.printStackTrace();
        }
        ButterKnife.bind(this);

        if (isPreference) {
            displayInWidget.setChecked(true);
        } else {
            displayInWidget.setChecked(false);
        }

        if(savedInstanceState == null){
            ingredientList = StepsActivity.getExportableIngredients();
            data = getData(ingredientList);
            ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            IngredientsAdapter adapter = new IngredientsAdapter(getData(ingredientList));
            ingredientsRecyclerView.setAdapter(adapter);
        }else{
            data = savedInstanceState.getStringArrayList(getString(R.string.ingredients));
            ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            IngredientsAdapter adapter = new IngredientsAdapter(data);
            ingredientsRecyclerView.setAdapter(adapter);
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(getString(R.string.ingredients),data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == ActionBar.DISPLAY_HOME_AS_UP){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnCheckedChanged(R.id.ch_display_in_widget)
    void onDisplayInWidgetChanged() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (displayInWidget.isChecked()) {
            editor.putString(getString(R.string.preferences_id), StepsActivity.getId());
            editor.apply();
        } else {
            editor.putString(getString(R.string.preferences_id), "");
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sharedPreferences.getString(getString(R.string.preferences_id), "").isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.preferences_id), StepsActivity.getId());
            editor.apply();
        }
    }
}
