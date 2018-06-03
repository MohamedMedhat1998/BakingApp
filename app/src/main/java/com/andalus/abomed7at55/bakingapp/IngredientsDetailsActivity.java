package com.andalus.abomed7at55.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
    @BindView(R.id.rv_ingredients)
    RecyclerView ingredientsRecyclerView;

    private AppWidgetManager myAppWidgetManager;
    private int ids[];

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        String id = sharedPreferences.getString(getString(R.string.preferences_id), "");
        boolean isPreference = id.equals(StepsActivity.getId());

        myAppWidgetManager = AppWidgetManager.getInstance(this);
        ids = myAppWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(MainActivity.getExportableRecipes().get(Integer.parseInt(StepsActivity.getId()) - 1).getName());
        }catch (Exception e){
            e.printStackTrace();
        }
        ButterKnife.bind(this);

        if (isPreference) {
            displayInWidget.setChecked(true);
            updateEverything();
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
        if (displayInWidget.isChecked()) {
            updateEverything();
        } else {
            editor.putString(getString(R.string.preferences_id), "");
            editor.apply();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sharedPreferences.getString(getString(R.string.preferences_id), "").isEmpty()) {
            updateEverything();
        }
    }

    /**
     * This method primary updates the text to be displayed in the widget
     */
    private void updateEverything() {
        editor.putString(getString(R.string.preferences_id), StepsActivity.getId());
        editor.putString(getString(R.string.preferences_label),
                MainActivity.getExportableRecipes().get(Integer.parseInt(StepsActivity.getId()) - 1).getName());
        editor.putString(getString(R.string.preferences_body),
                MainActivity.getExportableRecipes().get(Integer.parseInt(StepsActivity.getId()) - 1).getOneTextIngredients());
        editor.apply();
        RecipeWidget.customUpdate(getApplicationContext(),
                myAppWidgetManager,
                ids,
                sharedPreferences.getString(getString(R.string.preferences_label), getString(R.string.default_widget_label)),
                sharedPreferences.getString(getString(R.string.preferences_body), getString(R.string.default_widget_body)));
    }
}
