package com.woodman.updatediscreteness;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.woodman.versionupdate.VersionUpdateManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author zqb
 * @version 1.0
 * @date 2018/5/11
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VersionUpdateManager.getInstance().init(this);
    }
}
