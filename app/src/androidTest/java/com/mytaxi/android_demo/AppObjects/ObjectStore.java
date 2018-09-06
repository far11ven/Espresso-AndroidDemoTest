package com.mytaxi.android_demo;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import org.hamcrest.Matcher;
import android.support.test.espresso.matcher.ViewMatchers;


/*
   - This class defines all the application elements used
*/
public class ObjectStore {

    public final static Matcher<View> tv_USERNAME = withId(R.id.edt_username);
    public final static Matcher<View> tv_PASSWORD = withId(R.id.edt_password);
    public final static Matcher<View> btn_LOGIN = withId(R.id.btn_login);


}
