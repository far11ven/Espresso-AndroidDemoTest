package com.mytaxi.android_demo;

import android.os.SystemClock;
import com.mytaxi.android_demo.Constants;


/*
   - This class defines all the constants used in the test for credentials & other strings
*/
public class PollingService {

    public static void idlingCheckTimeOut()
    {
        SystemClock.sleep(Constants.POLLING_EVERY_TIMEOUT);      //Wait for 1000ms to toll for current activity
    }


}
