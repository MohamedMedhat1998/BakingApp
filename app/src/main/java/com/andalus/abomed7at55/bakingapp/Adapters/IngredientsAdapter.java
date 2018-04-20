package com.andalus.abomed7at55.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This is the adapter of the ingredient recycler view
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private ArrayList<String> mData;

    public IngredientsAdapter(ArrayList<String> data){
        mData = data;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.ingredient_item_in_the_recycler_view,parent,false);
        return new IngredientsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.textViewIngredientDescription.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_ingredient_full_description)
        TextView textViewIngredientDescription;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
