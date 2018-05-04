package com.andalus.abomed7at55.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Interfaces.StepClickListener;
import com.andalus.abomed7at55.bakingapp.R;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This is the adapter of the recycler view of the steps list
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{
    //The data
    private ArrayList<Step> stepData;
    private StepClickListener clickListener;
    public StepsAdapter(ArrayList<Step> data , StepClickListener stepClickListener){
        stepData = data;
        clickListener = stepClickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.step_list_item,parent,false);
        return new StepViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.btnShortDescriptionInTheList.setText(stepData.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepData.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.btn_step_short_description_in_the_list)
        Button btnShortDescriptionInTheList;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.btn_step_short_description_in_the_list)
        void onStepClicked(){
            clickListener.onStepClicked(stepData.get(getAdapterPosition()));
        }
    }
}
