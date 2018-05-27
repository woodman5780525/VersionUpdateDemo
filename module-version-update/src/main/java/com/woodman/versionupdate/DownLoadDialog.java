package com.woodman.versionupdate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

/**
 * @author zqb
 * @version 1.0
 * @date 2018/5/11
 */

public class DownLoadDialog extends AlertDialog.Builder {

    private View mView;
    private NumberProgressBar mNumberProgressBar;
    private TextView mTvCannerDownLoad;
    private TextView mTvBackGroupDownLoad;
    private  AlertDialog mDialog;


    public DownLoadDialog(@NonNull Context context) {
        super(context);
        mView = View.inflate(context,R.layout.dialog_down_load,null);
        initView();
        this.setCancelable(false);
        setListen();
        setView(mView);
    }

    private void initView() {
        mNumberProgressBar = mView.findViewById(R.id.dialog_down_load);
        mTvCannerDownLoad = mView.findViewById(R.id.dialog_btn_canner_down_load);
        mTvBackGroupDownLoad = mView.findViewById(R.id.dialog_btn_backgroup_down_load);
    }

    /**
     * 设置监听
     */
    private void setListen() {
        //取消下载
        mTvCannerDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDownLoadDialogListen != null){
                    mDownLoadDialogListen.canner();
                }
            }
        });
        //进入后台
        mTvBackGroupDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDownLoadDialogListen != null){
                    mDownLoadDialogListen.backgroup();
                }
            }
        });
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress){
        mNumberProgressBar.setProgress(progress);
    }


    /**
     *隐藏对话框
     */
    public void hideDialog(){
        mDialog.dismiss();
    }


    /**
     * 显示对话框
     */
    public void showDialog(){
        mDialog = this.show();
    }

    interface downLoadDialogListen{
        /**
         * 取消
         */
        void canner();

        /**
         * 后台下载
         */
        void backgroup();

    }
    private downLoadDialogListen mDownLoadDialogListen;
    public void setDownLoadDialogListen(downLoadDialogListen mDownLoadDialogListen){
        this.mDownLoadDialogListen = mDownLoadDialogListen;
    }
}
