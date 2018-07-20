package com.fvip.cd.fvipplayer;

import android.app.Application;
/**
 * Created by cd on 2018/7/19.
 */

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
