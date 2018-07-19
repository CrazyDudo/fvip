package com.fvip.cd.fvipplayer;

import android.app.Application;

public class AppApplication extends Application {
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static Application getContext() {
        return mApplication;
    }
}
