package com.mytaxi.android_demo.IdlingResources;

import android.app.Activity;
import android.os.SystemClock;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;


/*
   - This class defines IdlingResource till MainActivity is displayed on current view
*/
public class MainActivityIdlingResource implements IdlingResource {
    private ResourceCallback resourceCallback;
    private boolean isIdle;

    @Override
    public String getName() {

        return MainActivityIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle) return true;

        Activity activity = getCurrentActivity();
        if (activity == null) return false;
        idlingCheckTimeOut();
        
        String ActivityClassName = activity.getLocalClassName();

        isIdle = ActivityClassName.contains("MainActivity");            //returns 'true' if MainActivity has opened up & is present on view

        if (isIdle) {
            resourceCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    private Activity getCurrentActivity() {
        final Activity[] activity = new Activity[1];
        java.util.Collection<Activity> activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
        activity[0] = Iterables.getOnlyElement(activities);
        return activity[0];
    }

    private void idlingCheckTimeOut()
    {
        SystemClock.sleep(500);      //Wait for 500ms to toll for current activity
    }

}