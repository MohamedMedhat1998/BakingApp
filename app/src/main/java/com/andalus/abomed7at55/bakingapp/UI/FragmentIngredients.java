package com.andalus.abomed7at55.bakingapp.UI;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.andalus.abomed7at55.bakingapp.Adapters.IngredientsAdapter;
import com.andalus.abomed7at55.bakingapp.MainActivity;
import com.andalus.abomed7at55.bakingapp.R;
import com.andalus.abomed7at55.bakingapp.RecipeWidget;
import com.andalus.abomed7at55.bakingapp.StepsActivity;

import java.util.ArrayList;

/**
 * This class represents the Ingredients list fragment
 */

public class FragmentIngredients extends Fragment{

    public static final int FLAG_TABLET = 1;
    public static final int FLAG_NORMAL = 10;
    private int flag = FLAG_NORMAL;
    private ArrayList<String> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Fragment","Created");
        final View view = inflater.inflate(R.layout.fragment_ingredients,container,false);
        if(flag==FLAG_TABLET){
            RecyclerView recyclerView = view.findViewById(R.id.rv_ingredients);
            IngredientsAdapter adapter = new IngredientsAdapter(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(adapter);

            final CheckBox displayInWidget = view.findViewById(R.id.ch_display_in_widget);
            updateCheckBox(view.getContext(),displayInWidget);

            displayInWidget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (displayInWidget.isChecked()) {
                        updateEverything(view.getContext());
                    } else {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.preferences_id), "");
                        editor.apply();
                    }
                }
            });

        }
        return view;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int mFlag) {
        this.flag = mFlag;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    /**
     * This method is used to update the checkbox in this fragment to be consistent with the shared
     * preferences value
     * @param context the context of the fragment
     * @param checkBox the target checkbox
     */
    private void updateCheckBox(Context context,CheckBox checkBox){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String id = sharedPreferences.getString(getString(R.string.preferences_id), "");
        boolean isPreference = id.equals(StepsActivity.getId());

        if (isPreference) {
            checkBox.setChecked(true);
            updateEverything(context);
        } else {
            checkBox.setChecked(false);
        }
    }

    /**
     * This method is used to update the content of the app widget
     * @param context The context of the fragment
     */
    private void updateEverything(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.preferences_id), StepsActivity.getId());
        editor.putString(getString(R.string.preferences_label),
                MainActivity.getExportableRecipes().get(Integer.parseInt(StepsActivity.getId()) - 1).getName());
        editor.putString(getString(R.string.preferences_body),
                MainActivity.getExportableRecipes().get(Integer.parseInt(StepsActivity.getId()) - 1).getOneTextIngredients());
        editor.apply();
        AppWidgetManager myAppWidgetManager = AppWidgetManager.getInstance(context);
        int ids[] = myAppWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
        RecipeWidget.customUpdate(context,
                myAppWidgetManager,
                ids,
                sharedPreferences.getString(getString(R.string.preferences_label), getString(R.string.default_widget_label)),
                sharedPreferences.getString(getString(R.string.preferences_body), getString(R.string.default_widget_body)));
    }
}
