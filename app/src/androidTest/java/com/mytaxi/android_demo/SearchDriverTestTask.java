package com.mytaxi.android_demo;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.mytaxi.android_demo.IdlingResources.DropdownIdlingResource;
import com.mytaxi.android_demo.IdlingResources.MainActivityIdlingResource;
import com.mytaxi.android_demo.activities.AuthenticationActivity;
import com.mytaxi.android_demo.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class SearchDriverTestTask {

    private IdlingResource mainActivityIdlingResource;
    private IdlingResource driverSearchIdlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<AuthenticationActivity> mActivityTestRule = new ActivityTestRule<>(AuthenticationActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(
            android.Manifest.permission.ACCESS_FINE_LOCATION);                  // granting permission for user map location access during test


    /* This is a setUp method() before running a test
       - Sets up IdlingResources before each test
    */
    @Before
    public void setUpBeforeTest() {

        mainActivityIdlingResource = new MainActivityIdlingResource();
        driverSearchIdlingResource = new DropdownIdlingResource();
        Intents.init();                                                 //initialising intent

    }

    /* This is a Test method for Positive scenario TASK#1 & TASK#2
       - Logs in with the valid credentials
	   - Opens the Left side drawer
       - Verifies username matches the one provided on Login screen
       - in driver search field, searches for text "sa"
       - Selects 2nd result by name
       - Verifies driver name text on DRIVER PROFILE
       - Clicks on CALL button
    */
    @Test
    public void performLoginAndCallSearchedDriverTest(){
        //checking if Username input field is present & enabled before entering text
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).check(matches(isEnabled()));
        onView(withId(R.id.edt_username)).perform(click()) ;
        onView(withId(R.id.edt_username)).perform(typeText(Constants.USER_NAME));  //enter in username field

        //checking if password input field is present & enabled before entering text
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isEnabled()));
        onView(withId(R.id.edt_password)).perform(click()) ;
        onView(withId(R.id.edt_password)).perform(typeText(Constants.USER_PASSWORD), closeSoftKeyboard());  //enter in password field and close the keyboard


        // to check if LOGIN button is displayed and can be clicked
        onView(withId(R.id.btn_login)).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(withId(R.id.btn_login)).perform(click()) ; // click LOGIN to submit

        //Waiting for MainActivity to open up
        IdlingRegistry.getInstance().register(mainActivityIdlingResource);

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // To check Left Drawer based on that it is currently closed.
                .perform(DrawerActions.open());         // open drawer

        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource);  // ending wait for MainActivity

        // Verify that logged in USER is same for which credentials were provided during login
        onView(withId(R.id.nav_username)).check(matches(withText(Constants.USER_NAME)));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());       // closes the left drawer

        onView(withId(R.id.textSearch)).check(matches(isEnabled())).perform(click());        //check if enabled & click to search driver
        onView(withId(R.id.textSearch)).perform(typeText(Constants.STRING_TO_BE_SEARCHED)) ; //enter text "sa" search text

        //wait for drop-down to open up
        IdlingRegistry.getInstance().register(driverSearchIdlingResource);

        onView(withId(R.id.searchContainer)).check(matches(isDisplayed()));                  //verify dropdown searchContainer field is displayed on view

        
        //check & click if DRIVER NAME is present in dropdown
        onView(withText(Constants.DRIVER_NAME))
                .inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()))
                .perform(click());
				
		IdlingRegistry.getInstance().unregister(driverSearchIdlingResource);

        //Verify on driver profile screen DRIVER NAME is same as the one selected from dropdown
        onView(withId(R.id.textViewDriverName)).check(matches(withText(Constants.DRIVER_NAME)));

        //Click on 'Call' button
        onView(withId(R.id.fab)).perform(click());

        //validates that an intent for "dialer" activity has been sent after clicking "Call" button icon
        intended(toPackage("com.android.dialer"));


    }

    /* This is a @After method
      - Releases up resources, un-registering IdlingResources if remaining
    */
    @After
    public void tearDownAfterTest() {

        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource);
        IdlingRegistry.getInstance().unregister(driverSearchIdlingResource);
        Intents.release();
    }

}
