package com.fvip.cd.fvipplayer.api;


import com.fvip.cd.fvipplayer.bean.PlaylistBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cd on 2018/7/19.
 */


public interface PlatformListApi {

//    http://172.23.2.34:8080/CSMS/appAction
    @GET("text/viplist.json")
    Observable<PlaylistBean> getPlatform();
}
