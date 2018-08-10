package com.fvip.cd.fvipplayer.utils;
/**
 * Created by cd on 2018/08/08.
 */
public class StringUtil {
    public static int getCount(String str, String target) {
        int count = 0;
        int index = 0;
        while (((index = str.indexOf(target)) != -1)) {
            str = str.substring(index + 1);
            count++;
        }
        return count;
    }
}
