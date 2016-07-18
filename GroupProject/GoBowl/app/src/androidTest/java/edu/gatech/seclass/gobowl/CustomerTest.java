package edu.gatech.seclass.gobowl;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.



import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CustomerTest {

    //@Override
    protected void setUp()
    {

    }
    @Rule
    public ActivityTestRule<ManagerActivity> mActivityRule = new ActivityTestRule(ManagerActivity.class);

    @Test
    public void addCustomer() {
        onView(withText("Add Customer")).perform(click());
        onView(withId(R.id.fNameText)).perform(typeText("Andrew"),closeSoftKeyboard());
        onView(withId(R.id.lNameText)).perform(typeText("Mathews"),closeSoftKeyboard());
        onView(withId(R.id.emailText)).perform(typeText("andrew@m.com"),closeSoftKeyboard());
        onView(withText("Add Customer")).perform(click());
        /*inRoot(withDecorView(
                not(is(rule.getActivity().
                        getWindow().getDecorView())))).
                check(matches(isDisplayed()));*/
    }
}
