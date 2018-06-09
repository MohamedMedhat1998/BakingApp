package com.andalus.abomed7at55.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MyTest {

    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void myTest(){
        onView(withId(R.id.tv_app_title))
                .check(matches(withText("Baking App")));
        onView(withId(R.id.recipe_recycler_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.recipe_recycler_view))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.progressBar))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.tv_main_no_connection))
                .check(matches(not(isDisplayed())));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
