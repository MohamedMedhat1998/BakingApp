package com.andalus.abomed7at55.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.andalus.abomed7at55.bakingapp.Recipes.Recipe;
import com.andalus.abomed7at55.bakingapp.Recipes.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_view_full_description)
    TextView tvShowViewFullDescription;

    private Step currentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //TODO Support onSaveInstanceState
        ButterKnife.bind(this);

        currentStep = getIntent().getExtras().getParcelable(getString(R.string.keySelectedStep));

        tvShowViewFullDescription.setText(currentStep.getDescription());

    }
}
