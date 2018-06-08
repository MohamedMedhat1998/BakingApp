package com.andalus.abomed7at55.bakingapp.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.R;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;

/**
 * This class represents the details (video with instruction) fragment
 */

public class FragmentVideoWithInstructions extends Fragment {

    public static final int FLAG_NORMAL = 1;
    public static final int FLAG_TABLET = 10;
    private int mFlag = FLAG_NORMAL;
    private Step selectedStep;

    private TextView tvStepDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //TODO add the functionality of the details activity here too
        Log.d("State","Created");
        View view = inflater.inflate(R.layout.fragment_video_with_instruction,container,false);

        if(mFlag == FLAG_TABLET){
            tvStepDescription = view.findViewById(R.id.tv_view_full_description);
            tvStepDescription.setText(selectedStep.getDescription());
        }

        return view;
    }

    public void setFlag(int flag) {
        mFlag = flag;
    }

    public void setSelectedStep(Step selectedStep) {
        this.selectedStep = selectedStep;
    }

    /**
     * Use this method to update the content of the fragment instead of recreating it
     * @param stepDescription the new description
     */
    public void updateContent(String stepDescription){
        tvStepDescription.setText(stepDescription);
        //TODO develop this method
    }
}
