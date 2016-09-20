package ch.epfl.sweng.onebeat.test.activities;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.onebeat.Activities.MainActivity;

/**
 * Created by tharvik on 28.11.15.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> signInActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void launchActivity() {
    }

}
