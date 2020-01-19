package com.fvip.cd.fvipplayer.ui.video;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fvip.cd.fvipplayer.R;
import com.fvip.cd.fvipplayer.data.ApiManage;
import com.fvip.cd.fvipplayer.data.network.model.PlaylistBean;
import com.fvip.cd.fvipplayer.ui.adapter.ChannelListAdapter;
import com.fvip.cd.fvipplayer.ui.adapter.PlatformListAdapter;
import com.fvip.cd.fvipplayer.utils.ADFilterTool;
import com.fvip.cd.fvipplayer.utils.StringUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cd on 2018/7/19.
 */

public class VideoActivity extends AppCompatActivity implements VideoContract.View {
    private static final String TAG = "VideoActivity";
    private WebView webView;
    private String url = "https://v.qq.com/";//default url
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
    private ProgressBar progressBar;

    private VideoContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new VideoPresenter(this);
        presenter.requestVideoResource();
        initView();
    }

    private void initView() {
        progressBar = findViewById(R.id.progressbar);
        drawerLayout = findViewById(R.id.dl_layout);
        webView = findViewById(R.id.webview);
        lvLeft = findViewById(R.id.lv_left);
        lvRight = findViewById(R.id.lv_right);
        drawerLayout = findViewById(R.id.dl_layout);
        initWebView();
    }

    private void initLeftMenu(final List<PlaylistBean.PlatformlistBean> mListData) {
        mAdapter = new PlatformListAdapter(VideoActivity.this, R.layout.platform_list_item, mListData);
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
                Toast.makeText(VideoActivity.this, mListData.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initRightMenu(final List<PlaylistBean.ListBean> mListData) {
        channelListAdapter = new ChannelListAdapter(VideoActivity.this, R.layout.platform_list_item, mListData);
        lvRight.setDivider(null);
        lvRight.setAdapter(channelListAdapter);
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {//如果此时抽屉窗口打开，就给他关闭
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                if (StringUtil.getCount(platformVideoUrl, "http") > 1) {
                    platformVideoUrl = platformVideoUrl.substring(platformVideoUrl.indexOf("=") + 1);
                }
                playVIP(mListData.get(position).getUrl(), platformVideoUrl);
                Toast.makeText(VideoActivity.this, mListData.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    private void initWebView() {
        webSetting = webView.getSettings();
        webSetting.setDefaultTextEncodingName("utf-8");
        webSetting.setJavaScriptEnabled(true);  //必须保留
        webSetting.setDomStorageEnabled(true);//保留,否则无法播放优酷视频网页
        webView.setWebChromeClient(new MyWebChromeClient());//重写一下
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

    @Override
    public void onLoadSuccess(PlaylistBean playlistBean) {
        mLeftListData = playlistBean.getPlatformlist();
        mRightListData = playlistBean.getList();
        initLeftMenu(mLeftListData);
        initRightMenu(mRightListData);
        Log.d(TAG, "onSuccess: " + mLeftListData.toString());
    }

    @Override
    public void onLoadError(String error) {
        //read from local
        Toast.makeText(this, "Loading error:" + error, Toast.LENGTH_SHORT).show();

        String fromAssets = getFromAssets("viplist_backup.json");

        Gson gson = new Gson();

        PlaylistBean playlistBean = gson.fromJson(fromAssets, PlaylistBean.class);

        setResData(playlistBean);

    }


   public void setResData(PlaylistBean playlistBean ) {
       mLeftListData = playlistBean.getPlatformlist();
       mRightListData = playlistBean.getList();
       initLeftMenu(mLeftListData);
       initRightMenu(mRightListData);
       Toast.makeText(this, "load from local", Toast.LENGTH_SHORT).show();
   }


    /**
     * 读取assets文件
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);//加载完网页进度条消失
            } else {
                progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                progressBar.setProgress(newProgress);//设置进度值
            }

        }
    }



    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading: current page===" + url);
            platformVideoUrl = url;
//            Toast.makeText(VideoActivity.this, "shouldOverrideUrlLoading: ==" + url, Toast.LENGTH_SHORT).show();
//这里进行url拦截

 /*            if (url != null && url.contains("vip")) {
                Toast.makeText(VideoActivity.this, "url 拦截", Toast.LENGTH_SHORT).show();
                Toast.makeText(VideoActivity.this, "url 拦截", Toast.LENGTH_SHORT).show();
                return true;
            }
*/
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            view.getSettings().setJavaScriptEnabled(true);
//            super.onPageFinished(view, url);
//            view.loadUrl("javascript:function setTop(){document.querySelector('.ad-footer').style.display=\"none\";}setTop();");

//            super.onPageFinished(view, url);
            view.loadUrl("javascript:document.getElementById('imPage').style.display='none';");
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            url = url.toLowerCase();
            if (!ADFilterTool.hasAd(VideoActivity.this, url)) {
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
