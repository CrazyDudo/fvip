package com.fvip.cd.fvipplayer.ui.video;

import android.util.Log;

import com.fvip.cd.fvipplayer.data.ApiManage;
import com.fvip.cd.fvipplayer.data.network.model.PlaylistBean;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoPresenter implements VideoContract.Presenter {
    private static final String TAG = "VideoPresenter";
    private VideoContract.View view;

    public VideoPresenter(VideoContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void requestVideoResource() {
        ApiManage.getInstance()
                .getVideoResourcesService()
                .getPlatform()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PlaylistBean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onLoadError(e.getMessage());
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(PlaylistBean playlistBean) {
                        view.onLoadSuccess(playlistBean);
                        Log.d(TAG, "onNext: ");
                    }
                });

    }

    @Override
    public void start() {

    }
}
