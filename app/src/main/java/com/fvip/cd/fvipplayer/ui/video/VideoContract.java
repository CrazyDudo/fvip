package com.fvip.cd.fvipplayer.ui.video;

import com.fvip.cd.fvipplayer.data.network.model.PlaylistBean;
import com.fvip.cd.fvipplayer.ui.BasePresenter;
import com.fvip.cd.fvipplayer.ui.BaseView;

public interface VideoContract {

    interface Presenter extends BasePresenter {
        void requestVideoResource();
    }

    interface View extends BaseView<Presenter> {
        void onLoadSuccess(PlaylistBean playlistBean);

        void onLoadError(String error);
    }
}

