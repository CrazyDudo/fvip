package com.fvip.cd.fvipplayer.api;


import com.fvip.cd.fvipplayer.bean.PlaylistBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ruandong on 2017/3/14.
 */

public interface PlatformListApi {

//    http://172.23.2.34:8080/CSMS/appAction
    @GET("text/viplist.json")
    Observable<PlaylistBean> getPlatform();
}
