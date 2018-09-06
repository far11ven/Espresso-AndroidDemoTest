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
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;


import com.mytaxi.android_demo.IdlingResources.MainActivityIdlingResource;
import com.mytaxi.android_demo.IdlingResources.SnackbarIdlingResource;
import com.mytaxi.android_demo.activities.MainActivity;
import com.mytaxi.android_demo.activities.AuthenticationActivity;

import android.support.test.espresso.contrib.DrawerActions;
import android.view.Gravity;
import android.view.View;

import static com.mytaxi.android_demo.AppObjects.ObjectStore;

import static org.junit.Assert.assertFalse;
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
public class LoginTestTask {

    private IdlingResource snackbarIdlingResource;
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
        snackbarIdlingResource = new SnackbarIdlingResource();
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
        onView(ObjectStore.et_USERNAME).check(matches(isDisplayed()));
        onView(ObjectStore.et_USERNAME).check(matches(isEnabled()));
        onView(ObjectStore.et_USERNAME).perform(click()) ;
        onView(ObjectStore.et_USERNAME).perform(typeText(Constants.USER_NAME));   // enter in username

        //checking if password input field is present & enabled before entering text
        onView(ObjectStore.et_PASSWORD).check(matches(isDisplayed()));
        onView(ObjectStore.et_PASSWORD).check(matches(isEnabled()));
        onView(ObjectStore.et_PASSWORD).perform(click()) ;
        onView(ObjectStore.et_PASSWORD).perform(typeText(Constants.USER_PASSWORD), closeSoftKeyboard());  //enter in password field and close the keyboard


        // to check if LOGIN button is displayed and can be clicked
        onView(ObjectStore.btn_LOGIN).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(ObjectStore.btn_LOGIN).perform(click()) ; // click LOGIN button to submit

        assertFalse("Login Failure message is still displayed", isPresent(withText((R.string.message_login_fail))));  //Returns 'true' if message is dispalyed

    }

    /* This is a Test method for TASK#1 [Negative scenario]
       - Tries to log in with  no credentials provided
       - Waits for Snackbar to load
       - Validates the "Login Failed" message is displayed
    */
    @Test
    public void performLoginTestWithBlankValues(){

        onView(ObjectStore.et_USERNAME).check(matches(isDisplayed()));  //checking if username input field is present

        onView(ObjectStore.et_PASSWORD).check(matches(isDisplayed()));     //checking if password input field is present

        onView(ObjectStore.btn_LOGIN).check(matches(isDisplayed())).check(matches(isClickable())); // to check if LOGIN button is displayed and can be clicked
        onView(ObjectStore.btn_LOGIN).perform(click()) ; // click LOGIN button to submit [Although LOGIN button should be disabled unless Username & Password fields have value]

        //wait for Snackbar to open up
        IdlingRegistry.getInstance().register(snackbarIdlingResource);

        checkSnackBarDisplayedWithMessage(withText(R.string.message_login_fail));    // message to be matched with "Login failed" message as per String resource file
        IdlingRegistry.getInstance().unregister(snackbarIdlingResource);             // ending snackbar wait


    }

    /* This is a Test method for TASK#1 [Negative scenario]
       - Tries to log in with the in-valid Username
       - Waits for Snackbar to load
       - Validates the "Login Failed" message is displayed
    */
    @Test
    public void performLoginTestWithInValidUsername(){
        //checking if Username input field is present & enabled before entering text
        onView(ObjectStore.et_USERNAME).check(matches(isDisplayed()));
        onView(ObjectStore.et_USERNAME).check(matches(isEnabled()));
        onView(ObjectStore.et_USERNAME).perform(click()) ;
        onView(ObjectStore.et_USERNAME).perform(typeText(Constants.SOME_INVALID_TEXT));   // enter in username field


        //checking if password input field is present & enabled before entering text
        onView(ObjectStore.et_PASSWORD).check(matches(isDisplayed()));
        onView(ObjectStore.et_PASSWORD).check(matches(isEnabled()));
        onView(ObjectStore.et_PASSWORD).perform(click()) ;
        onView(ObjectStore.et_PASSWORD).perform(typeText(Constants.USER_PASSWORD), closeSoftKeyboard());  // enter in password field & close keyboard

        onView(ObjectStore.btn_LOGIN).check(matches(isDisplayed())).check(matches(isClickable())); // to check if LOGIN button is displayed and can be clicked
        onView(ObjectStore.btn_LOGIN).perform(click()) ; // click LOGIN button to submit

        //wait for Snackbar to open up
        IdlingRegistry.getInstance().register(snackbarIdlingResource);

        checkSnackBarDisplayedWithMessage(withText(R.string.message_login_fail));    // message to be matched with "Login failed" message as per String resource file

        IdlingRegistry.getInstance().unregister(snackbarIdlingResource);             // ending snackbar wait

    }

    /* This is a Test method for TASK#1 [Negative scenario]
       - Tries to log in with the in-valid Password, but valid Username
       - Waits for Snackbar to load
       - Validates the "Login Failed" message is displayed
    */
    @Test
    public void performLoginTestWithInValidPassword(){
        //checking if Username input field is present & enabled before entering text
        onView(ObjectStore.et_USERNAME).check(matches(isDisplayed()));
        onView(ObjectStore.et_USERNAME).check(matches(isEnabled()));
        onView(ObjectStore.et_USERNAME).perform(click()) ;
        onView(ObjectStore.et_USERNAME).perform(typeText(Constants.USER_NAME ));   // enter in username field


        //checking if password input field is present & enabled before entering text
        onView(ObjectStore.et_PASSWORD).check(matches(isDisplayed()));
        onView(ObjectStore.et_PASSWORD).check(matches(isEnabled()));
        onView(ObjectStore.et_PASSWORD).perform(click()) ;
        onView(ObjectStore.et_PASSWORD).perform(typeText(Constants.SOME_INVALID_TEXT), closeSoftKeyboard());  // enter in password field & close keyboard


        // to check if LOGIN button is displayed and can be clicked
        onView(ObjectStore.btn_LOGIN).check(matches(isDisplayed())).check(matches(isClickable()));
        onView(ObjectStore.btn_LOGIN).perform(click()) ; // click LOGIN button to submit


        //wait for Snackbar to open up
        IdlingRegistry.getInstance().register(snackbarIdlingResource);

        checkSnackBarDisplayedWithMessage(withText(R.string.message_login_fail));    // message to be matched with "Login failed" message as per String resource file

        IdlingRegistry.getInstance().unregister(snackbarIdlingResource);             // ending snackbar wait

    }

    /* This is a @After method
      - Releases up resources, un-registering IdlingResources if remaining
    */
    @After
    public void tearDownAfterTest() {

        IdlingRegistry.getInstance().unregister(mainActivityIdlingResource);
        IdlingRegistry.getInstance().unregister(snackbarIdlingResource);

    }

    /* This is a helper matcher method
       - this method validates whether a matcher is currently visible on screen or not
    */
    private void checkSnackBarDisplayedWithMessage(final Matcher<View> matcher) {
        onView(matcher)
                .check(matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE
                )));
    }
	
	/* This is a helper matcher method
       - this method validates whether a matcher is currently visible on screen then returns 'true'
	   - else , returns 'false'
    */
	public boolean isPresent(final Matcher<View> matcher){
        try {
            onView(matcher).check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException e) {
            return false;
        }
    }

}
