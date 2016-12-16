package com.scholar.dollar.android.dollarscholarbenlewis;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.widget.TextView;

import com.scholar.dollar.android.dollarscholarbenlewis.activities.MainActivity;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.close;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by benjaminlewis on 12/12/16.
 */
@RunWith(AndroidJUnit4.class)
public class AppNavigationTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testNavDrawOpensOnFilterIconClick() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer

        String navigateUpDesc = mActivityTestRule.getActivity()
                .getString(android.support.v7.appcompat.R.string.abc_action_bar_up_description);
        onView(withContentDescription(navigateUpDesc)).perform(click());

        // Check if drawer is open
        onView(withId(R.id.drawer))
                .check(matches(isOpen(Gravity.START))); // Left drawer is open open.

    }



    @Test
    public void testNavStateSpinner() {
        // Open Drawer to click on navigation.
        Random random = new Random();
        int randInd = random.nextInt(49) + 1;
        String[] states = mActivityTestRule.getActivity().getResources().getStringArray(R.array.states);
        String state = states[randInd];
        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer
        onView(withId(R.id.nav_spinner)).perform(click());
        onData(allOf(is(CoreMatchers.instanceOf(String.class)), is(state))).perform(click());
        onView(withId(R.id.drawer))
                .check(matches(isOpen(Gravity.START))) // Left Drawer should be closed.
                .perform(close()); // Open Drawer
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(state)));
        //check if toolbar title is back to dollar scholar
        onView(withId(R.id.drawer))
                .check(matches(isClosed(Gravity.START))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer
        onView(withId(R.id.nav_spinner)).perform(click());
        onData(allOf(is(CoreMatchers.instanceOf(String.class)), is(states[0]))).perform(click());
        onView(withId(R.id.drawer))
                .check(matches(isOpen(Gravity.START))) // Left Drawer should be closed.
                .perform(close()); // Open Drawer
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(mActivityTestRule.getActivity().getString(R.string.app_name))));



    }



}
