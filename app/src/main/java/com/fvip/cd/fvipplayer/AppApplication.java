package com.fvip.cd.fvipplayer;

import android.app.Application;


import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by cd on 2018/7/19.
 */

public class AppApplication extends Application {
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initJpush();
    }

    private void initJpush() {
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);//如果时正式版就改成false
        JAnalyticsInterface.init(this);
    }

    public static Application getContext() {
        return mApplication;
    }
}
