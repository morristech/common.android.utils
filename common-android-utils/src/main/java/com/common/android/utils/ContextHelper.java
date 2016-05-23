package com.common.android.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Jan Rabe on 30/09/15.
 */
public class ContextHelper {

    @Nullable
    private static Application application;

    @Nullable
    private static Context context;

    public ContextHelper() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void init(@NonNull final Application application) {
        ContextHelper.application = application;
        application.registerActivityLifecycleCallbacks(createActivityLifecycleCallbacks());
    }

    public static void setContext(@Nullable final Context context) {
        ContextHelper.context = context;
    }

    @Nullable
    public static Application getApplication() {
        return application;
    }

    @Nullable
    public static Context getContext() {
        return context;
    }

    @Nullable
    public static FragmentActivity getFragmentActivity() {
        return context instanceof FragmentActivity
                ? (FragmentActivity) context
                : null;
    }

    @Nullable
    public static Activity getActivity() {
        return context instanceof Activity
                ? (Activity) context
                : null;
    }

    private static Application.ActivityLifecycleCallbacks createActivityLifecycleCallbacks() {
        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setContext(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }
}
