package edu.gatech.seclass.gobowl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FrontPageTest {

    @Rule
    public ActivityTestRule<FrontScreenActivity> mActivityRule = new ActivityTestRule(FrontScreenActivity.class);

    @Test
    public void firstTest() {
        onView(withText("Manager")).check(matches(isDisplayed()));
    }
}