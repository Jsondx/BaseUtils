package com.ldx.baseutils.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Suning
 * @date 2018/4/9
 */

public class ActivityManger {
    private static List<Activity> activityList = new ArrayList<>();

    public static void addac(Activity activity) {
        activityList.add(activity);
    }

    public static void removeac(Activity activity) {
        activityList.remove(activity);
    }

    public static void finshApp() {
        for (Activity ac : activityList) {
            ac.finish();
        }
    }

}
