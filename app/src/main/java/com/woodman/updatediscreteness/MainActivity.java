package com.woodman.updatediscreteness;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.woodman.versionupdate.VersionUpdateConfig;
import com.woodman.versionupdate.VersionUpdateManager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VersionUpdateConfig config = new VersionUpdateConfig();
        config.setCheckUpdateUrl("http://www.baidu.com");
        config.setShowDownLoadProgress(true);
        config.setDeleteFolder(true);
        config.setDownLoadUrl(Environment.getExternalStorageDirectory().getAbsolutePath());
        config.setDownLoadUrl("http://www.baidu.com");


        VersionUpdateManager.getInstance().setVersionUpdateConfig(config)
                .checkVersionUpdate(this, new VersionUpdateManager.onCheckVersionListen() {
                    @Override
                    public void updateComplete() {

                    }

                    @Override
                    public void forceClose() {
                        MainActivity.this.finish();
                    }

                    @Override
                    public void updateCancel() {

                    }

                    @Override
                    public void noUpdate() {

                    }

                    @Override
                    public void onError(String errorMsg) {

                    }
                });



    }

}
