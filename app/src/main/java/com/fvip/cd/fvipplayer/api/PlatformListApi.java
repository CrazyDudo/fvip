package com.fvip.cd.fvipplayer.api;


import com.fvip.cd.fvipplayer.bean.PlaylistBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cd on 2018/7/19.
 */

public interface PlatformListApi {
    //    https://iodefog.github.io/text/mviplistmm.json
    @GET("text/mviplistmm.json")
    Observable<PlaylistBean> getPlatform();

}
