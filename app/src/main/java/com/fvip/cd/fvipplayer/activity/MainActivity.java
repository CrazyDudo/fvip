package com.fvip.cd.fvipplayer.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fvip.cd.fvipplayer.R;
import com.fvip.cd.fvipplayer.adapter.ChannelListAdapter;
import com.fvip.cd.fvipplayer.adapter.PlatformListAdapter;
import com.fvip.cd.fvipplayer.api.ApiManage;
import com.fvip.cd.fvipplayer.bean.PlaylistBean;
import com.fvip.cd.fvipplayer.utils.ADFilterTool;
import com.fvip.cd.fvipplayer.utils.StringUtil;
import com.fvip.cd.fvipplayer.webview.ProgressbarWebView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cd on 2018/7/19.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressbarWebView webView;
    private String url = "https://v.qq.com/";
    private List<PlaylistBean.PlatformlistBean> mLeftListData = new ArrayList<>();
    private List<PlaylistBean.ListBean> mRightListData = new ArrayList<>();
    private String platformVideoUrl = "";
    private WebSettings webSetting;
    private DrawerLayout drawerLayout;
    private ListView lvLeft;
    private ListView lvRight;
    private PlatformListAdapter mAdapter;
    private ChannelListAdapter channelListAdapter;
    private String furl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {
        ApiManage.getInstance()
                .getSwitchService()
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
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(PlaylistBean playlistBean) {
                        mLeftListData = playlistBean.getPlatformlist();
                        mRightListData = playlistBean.getList();
                        initLeftMenu(mLeftListData);
                        initRightMenu(mRightListData);
                        Log.d(TAG, "onSuccess: " + mLeftListData.toString());
                    }
                });
    }

    private void initView() {
        drawerLayout = findViewById(R.id.dl_layout);
        webView = findViewById(R.id.webview);
        lvLeft = findViewById(R.id.lv_left);
        lvRight = findViewById(R.id.lv_right);
        drawerLayout = findViewById(R.id.dl_layout);
        initWebView();
    }

    private void initLeftMenu(final List<PlaylistBean.PlatformlistBean> mListData) {
        mAdapter = new PlatformListAdapter(MainActivity.this, R.layout.platform_list_item, mListData);
        lvLeft.setDivider(null);
        lvLeft.setAdapter(mAdapter);
        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {//如果此时抽屉窗口打开，就给他关闭
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                loadUrl(mListData.get(position).getUrl());
                Toast.makeText(MainActivity.this, mListData.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initRightMenu(final List<PlaylistBean.ListBean> mListData) {
        channelListAdapter = new ChannelListAdapter(MainActivity.this, R.layout.platform_list_item, mListData);
        lvRight.setDivider(null);
        lvRight.setAdapter(channelListAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {//如果此时抽屉窗口打开，就给他关闭
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                if (StringUtil.getCount(platformVideoUrl, "http")>1) {
                    platformVideoUrl = platformVideoUrl.substring(platformVideoUrl.indexOf("=") + 1);
                }
                playVIP(mListData.get(position).getUrl(), platformVideoUrl);
                Toast.makeText(MainActivity.this, mListData.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    private void initWebView() {
        webSetting = webView.getSettings();
        webSetting.setDefaultTextEncodingName("utf-8");
        webSetting.setJavaScriptEnabled(true);  //必须保留
        webSetting.setDomStorageEnabled(true);//保留,否则无法播放优酷视频网页
//        webView.setWebChromeClient(new WebChromeClient());//重写一下
        webView.setWebViewClient(new MyWebViewClient());
        loadUrl(url);
    }

    private void loadUrl(String url) {
        webView.loadUrl(url);
        Log.d(TAG, "loadUrl: " + url);
    }

    private void playVIP(String channelUrl, String url) {
        furl = channelUrl + url;
        loadUrl(furl);
        Log.d(TAG, "playVIP====" + furl);
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading: current page===" + url);
            platformVideoUrl = url;
//            Toast.makeText(MainActivity.this, "shouldOverrideUrlLoading: ==" + url, Toast.LENGTH_SHORT).show();
//这里进行url拦截

 /*            if (url != null && url.contains("vip")) {
                Toast.makeText(MainActivity.this, "url 拦截", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "url 拦截", Toast.LENGTH_SHORT).show();
                return true;
            }
*/
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            view.getSettings().setJavaScriptEnabled(true);
//            super.onPageFinished(view, url);
            view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            url = url.toLowerCase();
            if (!ADFilterTool.hasAd(MainActivity.this, url)) {
                return super.shouldInterceptRequest(view, url);
            } else {
                return new WebResourceResponse(null, null, null);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
