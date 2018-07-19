package com.fvip.cd.fvipplayer.utils;

import android.content.Context;
import android.content.res.Resources;

import com.fvip.cd.fvipplayer.R;

/***************************************************************************************************
 *                                  Copyright (C), Nexgo Inc.                                      *
 *                                    http://www.nexgo.cn                                          *
 ***************************************************************************************************
 * usage           : ${filename}
 * Version         : 1
 * Author          : ruandong
 * Date            : 7/19/2018
 * Modify          : create file
 **************************************************************************************************/
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
