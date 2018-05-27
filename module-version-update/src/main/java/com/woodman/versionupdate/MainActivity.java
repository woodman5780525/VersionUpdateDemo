package com.woodman.versionupdate;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        VersionUpdateConfig config = new VersionUpdateConfig();
        config.setCheckUpdateUrl("http://www.baidu.com");
        config.setShowDownLoadProgress(true);
        config.setDeleteFolder(true);
        config.setDownLoadUrl(Environment.getExternalStorageDirectory().getAbsolutePath());
        config.setDownLoadUrl("http://www.baidu.com");










    }

}
