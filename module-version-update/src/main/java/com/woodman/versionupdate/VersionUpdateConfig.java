package com.woodman.versionupdate;

/**
 * 用于配置版本更新的一些参数
 * @author zqb
 * @version 1.0
 * @date 2018/5/11
 */

public class VersionUpdateConfig {

    /**
     * 下载的地址
     */
    private String downLoadUrl;

    /**
     * 检查更新的地址
     */
    private String checkUpdateUrl;


    /**
     * 是否显示通知栏
     */
    private boolean isShowNotification;

    /**
     * 是否显示下载的进度
     */
    private boolean isShowDownLoadProgress;


    /**
     * App存储的地址
     */
    private String appSaveUrl;


    /**
     * 是否删除文件（下载后的文件）
     */
    private boolean isDeleteFolder;

    public boolean isDeleteFolder() {
        return isDeleteFolder;
    }

    public void setDeleteFolder(boolean deleteFolder) {
        isDeleteFolder = deleteFolder;
    }

    public String getAppSaveUrl() {
        return appSaveUrl;
    }

    public void setAppSaveUrl(String appSaveUrl) {
        this.appSaveUrl = appSaveUrl;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public String getCheckUpdateUrl() {
        return checkUpdateUrl;
    }

    public void setCheckUpdateUrl(String checkUpdateUrl) {
        this.checkUpdateUrl = checkUpdateUrl;
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    public void setShowNotification(boolean showNotification) {
        isShowNotification = showNotification;
    }

    public boolean isShowDownLoadProgress() {
        return isShowDownLoadProgress;
    }

    public void setShowDownLoadProgress(boolean showDownLoadProgress) {
        isShowDownLoadProgress = showDownLoadProgress;
    }
}
