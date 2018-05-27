package com.woodman.versionupdate;

/**
 * @author zqb
 * @version 1.0
 * @date 2018/5/11
 */

public class AppInfoEntity {

    private String newVersion;      //最新的版本
    private boolean isNeedUpdate;   //是否需要更新
    private String apkDownLoadUrl;  //APK下载的地址
    private boolean forceUpdate;    //是否强制更新
    private float apkSize;          //APK大小
    private String updateContent;   //更新的内容


    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }


    public boolean isNeedUpdate() {
        return isNeedUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        isNeedUpdate = needUpdate;
    }

    public String getApkDownLoadUrl() {
        return apkDownLoadUrl;
    }

    public void setApkDownLoadUrl(String apkDownLoadUrl) {
        this.apkDownLoadUrl = apkDownLoadUrl;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public float getApkSize() {
        return apkSize;
    }

    public void setApkSize(float apkSize) {
        this.apkSize = apkSize;
    }

    @Override
    public String toString() {
        return "AppInfoEntity{" +
                "newVersion='" + newVersion + '\'' +
                ", isNeedUpdate=" + isNeedUpdate +
                ", apkDownLoadUrl='" + apkDownLoadUrl + '\'' +
                ", forceUpdate=" + forceUpdate +
                ", apkSize=" + apkSize +
                ", updateContent='" + updateContent + '\'' +
                '}';
    }
}
