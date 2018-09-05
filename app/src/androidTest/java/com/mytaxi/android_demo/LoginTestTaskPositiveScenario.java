package com.mytaxi.android_demo;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.filters.LargeTest;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import com.mytaxi.android_demo.IdlingResources.MainActivityIdlingResource;
import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.activities.AuthenticationActivity;

import android.support.test.espresso.contrib.DrawerActions;
import android.view.Gravity;
import android.view.View;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTestTaskPositiveScenario {

    private IdlingResource mainActivityIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);                       // defining main activity rule

    @Rule
    public ActivityTestRule<AuthenticationActivity> mActivityTestRule
            = new ActivityTestRule<>(AuthenticationActivity.class);             // defining Authentication activity rule

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);                  // granting permission for user map location access during test


    /* This is a setUp method() before running a test
       - Sets up IdlingResources before each test
    */
    @Before
    public void setUpBeforeTest(){
        mainActivityIdlingResource = new MainActivityIdlingResource();

    }

    /* This is a Test method for TASK#1 [Positive scenario]
       - Logs in with the valid credentials
       - Waits for MainActivity to load
       - Opens the Left side drawer
       - Verifies username matches the one provided on Login screen
    */
    @Test
    public void performLoginTestWithValidCredentials(){

        //checking if Username input field is present & enabled before entering text
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).check(matches(isEnabled()));
        onView(withId(R.id.edt_username)).perform(click()) ;
        onView(withId(R.id.edt_username)).perform(typeText(Constants.USER_NAME));   // enter in username

        //checking if password input field is present & enabled before entering text
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isEnabled()));
        onView(withId(R.id.edt_password)).perform(click()) ;
        onView(withId(R.id.edt_password)).perform(typeText(Constants.USER_PASSWORD), closeSoftKeyboard());  //enter in password field and close the keyboard


        // to check if LOGIN button is displayed and can be clicked
        onView(withId(R.id.btn_login)).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(withId(R.id.btn_login)).perform(click()) ; // click LOGIN button to submit

        //Waiting for MainActivity to open up
        IdlingRegistry.getInstance().register(mainActivityIdlingResource);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // To check Left Drawer based on that it is currently closed.
                .perform(DrawerActions.open());         // open drawer

        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource);   // ending wait for MainActivity

        // Verify that logged in USER is same for which credentials were provided during login
        onView(withId(R.id.nav_username)).check(matches(withText(Constants.USER_NAME)));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());       // close drawer

    }


    /* This is a @After method
      - Releases up resources, un-registering IdlingResources if remaining
    */
    @After
    public void tearDownAfterTest() {

        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource);

    }

}
