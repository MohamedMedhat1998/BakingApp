package com.andalus.abomed7at55.bakingapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andalus.abomed7at55.bakingapp.Adapters.IngredientsAdapter;
import com.andalus.abomed7at55.bakingapp.R;

import java.util.ArrayList;

/**
 * This class represents the Ingredients list fragment
 */

public class FragmentIngredients extends Fragment{

    public static final int FLAG_TABLET = 1;
    public static final int FLAG_NORMAL = 10;
    private int flag = FLAG_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Fragment","Created");
        View view = inflater.inflate(R.layout.fragment_ingredients,container,false);
        if(flag==FLAG_TABLET){
            //TODO Handle the case of tablets here
            RecyclerView recyclerView = view.findViewById(R.id.rv_ingredients);
            ArrayList<String> data = new ArrayList<>();
            data.add("A");
            data.add("B");
            data.add("C");
            data.add("D");
            IngredientsAdapter adapter = new IngredientsAdapter(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int mFlag) {
        this.flag = mFlag;
    }
}
