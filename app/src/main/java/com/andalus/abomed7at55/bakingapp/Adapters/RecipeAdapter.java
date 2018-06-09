package com.andalus.abomed7at55.bakingapp.Adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Interfaces.RecipeClickListener;
import com.andalus.abomed7at55.bakingapp.R;
import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This is the adapter of the recipe recycler view
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    //The data
    private ArrayList<Recipe> recipes;

    private RecipeClickListener mRecipeClickListener;

    public RecipeAdapter(ArrayList<Recipe> recipeData,RecipeClickListener clickListener){
        recipes = recipeData;
        mRecipeClickListener = clickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_menu_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.tvLabel.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_recipe_menu_label)
        TextView tvLabel;
        @BindView(R.id.recipe_item_container)
        ConstraintLayout constraintLayout;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
        @OnClick(R.id.recipe_item_container)
        void itemClicked(){
            mRecipeClickListener.onRecipeClicked(recipes.get(getAdapterPosition()).getId());
        }
    }
}
