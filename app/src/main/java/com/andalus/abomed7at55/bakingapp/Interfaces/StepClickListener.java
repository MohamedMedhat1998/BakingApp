package com.andalus.abomed7at55.bakingapp.Interfaces;

import com.andalus.abomed7at55.bakingapp.Recipes.Step;

/**
 * This class handles the click event on the steps objects in the recycler view
 */

public interface StepClickListener {
    void onStepClicked(Step selectedStep);
}
