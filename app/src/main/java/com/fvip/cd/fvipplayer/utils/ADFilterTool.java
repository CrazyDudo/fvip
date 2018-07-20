package com.fvip.cd.fvipplayer.utils;

import android.content.Context;
import android.content.res.Resources;

import com.fvip.cd.fvipplayer.R;
/**
 * Created by cd on 2018/7/19.
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url){
        Resources res= context.getResources();
        String[]adUrls =res.getStringArray(R.array.adBlockUrl);
        for(String adUrl :adUrls){
            if(url.contains(adUrl)){
                return true;
            }
        }
        return false;
    }

}
