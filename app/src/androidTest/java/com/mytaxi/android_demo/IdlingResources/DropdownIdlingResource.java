package com.mytaxi.android_demo.IdlingResources;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import com.mytaxi.android_demo.R;

/*
   - This class defines IdlingResource for DropDown display after value has been entered
*/
public class DropdownIdlingResource implements IdlingResource {
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

        isIdle = activity.findViewById(R.id.searchContainer).isShown();

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

}