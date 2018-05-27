package com.woodman.versionupdate;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author zqb
 * @version 1.0
 * @date 2018/5/11
 */

public class VersionUpdateManager {

    private static final String TAG = "VersionUpdateManager";
    private Context mContext;
    private VersionUpdateConfig mVersionUpdateConfig;
    private OkDownload mOkDownload;
    private DownloadTask mDownloadTask;
    private static final String DOWN_LOAD_TAG = "downLoadTag";
    private VersionUpdateDialog mVersionUpdateDialog;
    private DownLoadDialog mDownLoadDialog;
    private AppInfoEntity mAppInfoEntity;


    private VersionUpdateManager(){
        Log.d(TAG, "VersionUpdateManager");
    }
    private static class SingletonHolder{
        private static final VersionUpdateManager INSTANCE = new VersionUpdateManager();
    }
    public static VersionUpdateManager getInstance(){
        return SingletonHolder.INSTANCE;
    }


    public void init(Application context){
        Log.d(TAG, "init");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        OkGo.getInstance().init(context)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
        mOkDownload = OkDownload.getInstance();
    }


    /** 设置版本更新的配置
     * @param mVersionUpdateConfig
     */
    public VersionUpdateManager setVersionUpdateConfig(VersionUpdateConfig mVersionUpdateConfig){
        this.mVersionUpdateConfig = mVersionUpdateConfig;
        return this;
    }


    /**
     * 检查版本更新
     */
    public void checkVersionUpdate(Context mContext,onCheckVersionListen mOnCheckVersionListen) {
        this.mContext = mContext;
        Log.d(TAG, "execute");
        if (null != mOnCheckVersionListen) {
            this.mOnCheckVersionListen = mOnCheckVersionListen;
        }
        getAppVersionInfo();
    }

    /**
     * 获取APP版本的信息
     */
    private void getAppVersionInfo() {
        Log.d(TAG, "getAppVersionInfo");
        OkGo.<String>get(mVersionUpdateConfig.getCheckUpdateUrl())
                .tag("getAppVersionInfo")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(TAG, "getAppVersionInfo onSuccess " + response.toString());
                        analyzeAppInfoData(response.toString());
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(TAG, "getAppVersionInfo onError " + response.toString());
                        mOnCheckVersionListen.onError(response.body());
                    }
                });
    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void analyzeAppInfoData(String json) {
        Log.d(TAG, "analyzeAppInfoData");
        Gson gson = new Gson();
       // AppInfoEntity appInfoEntity = gson.fromJson(json, AppInfoEntity.class);
        AppInfoEntity entity = new AppInfoEntity();
        entity.setForceUpdate(true);
        entity.setApkSize(50.6f);
        entity.setApkDownLoadUrl("http://www.baidu.com");
        entity.setNewVersion("1.4.2");
        entity.setUpdateContent("这是更新的文本我内容，这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容，这是更新的文本我内容" +
                "这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容" +
                "这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容" +
                "这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容这是更新的文本我内容");

        mAppInfoEntity = entity;
        Log.d(TAG,entity.toString());
        isUpdate(entity);
    }


    /**
     * 是否需要更新
     * @param appInfoEntity true == yes
     */
    private void isUpdate(AppInfoEntity appInfoEntity) {
        Log.d(TAG, "isUpdate");
        //转换本地的版本号
        String[] localVersion = getLocalVersion().split("\\.");
        StringBuilder localVersionBuilder = new StringBuilder();
        for (String item : localVersion) {
            localVersionBuilder.append(item);
        }
        int formatLocalVersion = Integer.parseInt(localVersionBuilder.toString());
        //转换服务器返回的版本号
        String[] newsVersion = appInfoEntity.getNewVersion().split("\\.");
        StringBuilder newsVersionBuilder = new StringBuilder();
        for (String item : newsVersion) {
            newsVersionBuilder.append(item);
        }
        int formatNewsVersion = Integer.parseInt(newsVersionBuilder.toString());
        //如果当前版本小于服务器版本则显示版本更新对话框
        if(formatLocalVersion < formatNewsVersion){
            appInfoEntity.setNeedUpdate(true);
            showVersionUpdateDialog(appInfoEntity);
            Log.d(TAG, "需要更新");
        }else {
            appInfoEntity.setNeedUpdate(false);
            if(null != mOnCheckVersionListen){
                mOnCheckVersionListen.noUpdate();
            }
            Log.d(TAG, "不需要更新");
        }
    }


    /**
     * 显示版本更新对话框
     *
     * @param appInfoEntity
     */
    private void showVersionUpdateDialog(final AppInfoEntity appInfoEntity) {
        mVersionUpdateDialog = new VersionUpdateDialog(mContext, appInfoEntity);
        mVersionUpdateDialog.showDialog();
        mVersionUpdateDialog.setmVersionUpdateListen(new VersionUpdateDialog.versionUpdateListen() {
            @Override
            public void canner() {
                if (mOnCheckVersionListen != null) {
                    mOnCheckVersionListen.updateCancel();
                }
                //关闭版本更新对话框
                mVersionUpdateDialog.hideDialog();
            }
            @Override
            public void update() {
                //开始更新
                downLoadApp();
                //关闭版本更新对话框
                mVersionUpdateDialog.hideDialog();
            }
            @Override
            public void exit() {
                if (mOnCheckVersionListen != null) {
                    mOnCheckVersionListen.forceClose();
                }
                //关闭版本更新对话框
                mVersionUpdateDialog.hideDialog();
            }
        });
    }


    /**
     * 获取本地版本
     * @return 版本号
     */
    public String getLocalVersion() {
        Log.d(TAG, "getLocalVersion");
        try {
            PackageInfo packageInfo = mContext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mOnCheckVersionListen.onError("PackageManager NameNotFoundException");
        }
        return null;
    }




    /**
     * 下载APP
     */
    private void downLoadApp() {
        Log.d(TAG, "downLoadApp");
        GetRequest<File> request = OkGo.<File>get("http://193.112.103.230:8080/resource/app/news.apk");
        mDownloadTask = OkDownload.request(DOWN_LOAD_TAG, request)
                .save()
                .folder(Environment.getExternalStorageDirectory().getAbsolutePath())
                .register(mDownloadListener);
        mDownloadTask.start();

    }


    DownloadListener mDownloadListener = new DownloadListener(DOWN_LOAD_TAG) {
        @Override
        public void onStart(Progress progress) {
            Log.d(TAG, "onStart" + progress);
            if (mVersionUpdateConfig.isShowDownLoadProgress()) {
                showDownLoadDialog();
            }
            if (mVersionUpdateConfig.isShowNotification()) {
                showNoticeNotification();
            }
        }

        @Override
        public void onProgress(Progress progress) {
            Log.d(TAG, "downloadProgress " + progress.fraction * 100);
            mDownLoadDialog.setProgress((int) (progress.fraction * 100));
        }

        @Override
        public void onError(Progress progress) {
            Log.d(TAG, "onError" + progress);
            mDownLoadDialog.hideDialog();
            mOnCheckVersionListen.onError("下载出错");
        }

        @Override
        public void onFinish(File file, Progress progress) {
            Log.d(TAG, "onFinish" + progress);
            mOnCheckVersionListen.updateComplete();
            mDownLoadDialog.hideDialog();
            //安装
            installApp(file);
        }
        @Override
        public void onRemove(Progress progress) {
            Log.d(TAG, "onRemove" + progress);
        }
    };


    /**
     * 安装APP
     */
    public void installApp(File file){
        Log.d(TAG,"installApp");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(mContext, "com.woodman.updatediscreteness.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        mContext.startActivity(intent);

    }



    /**
     * 显示通知栏
     */
    private void showNoticeNotification() {
        Log.d(TAG, "showNoticeNotification");

    }


    /**
     * 显示下载对话框
     */
    private void showDownLoadDialog() {
        Log.d(TAG, "showDownLoadDialog");
        mDownLoadDialog = new DownLoadDialog(mContext);
        mDownLoadDialog.showDialog();
        mDownLoadDialog.setDownLoadDialogListen(new DownLoadDialog.downLoadDialogListen() {
            @Override
            public void canner() {
                Log.d(TAG, "canner");
                mDownloadTask.remove(mVersionUpdateConfig.isDeleteFolder());
               if(mAppInfoEntity.isForceUpdate()){
                   mOnCheckVersionListen.forceClose();
               }else {
                   mOnCheckVersionListen.updateCancel();
               }
                mDownLoadDialog.hideDialog();
            }

            @Override
            public void backgroup() {
                Log.d(TAG, "backgroup");
                mDownLoadDialog.hideDialog();
            }
        });
    }


    /**
     * 释放资源
     */
    public void releaseResource() {
        Log.d(TAG, "releaseResource");
        //根据设置是否需要删除下载的文件
        mOkDownload.removeAll(mVersionUpdateConfig.isDeleteFolder());
    }


    /**
     * 版本检查回调
     */
    public interface onCheckVersionListen {

        /**
         * 更新完成
         */
        void updateComplete();

        /**
         * 强行关闭
         */
        void forceClose();

        /**
         * 更新取消
         */
        void updateCancel();

        /**
         * 不需要更新
         */
        void noUpdate();

        void onError(String errorMsg);

    }

    private onCheckVersionListen mOnCheckVersionListen;

    /**
     * app下载进度监听
     */
    interface onDownLoadListen {
        void progress(int progress);
        void onError();
    }
    private onDownLoadListen mOnDownLoadListen;

}
