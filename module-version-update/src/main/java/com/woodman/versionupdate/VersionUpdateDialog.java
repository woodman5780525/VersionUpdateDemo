package com.woodman.versionupdate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * 版本更新对话框
 * @author zqb
 * @version 1.0
 * @date 2018/5/12
 */

public class VersionUpdateDialog extends AlertDialog.Builder {

    private static  final String TAG = VersionUpdateDialog.class.getSimpleName();
    private View mView;
    private TextView mTvNewVersion;         //最新的版本
    private TextView mTvUpdateSize;         //更新的大小
    private TextView mTvUpdateContent;      //更新的内容
    private TextView mTvCanner;             //取消或退出
    private TextView mTvUpdate;             //更新按钮
    private AppInfoEntity mAppInfoEntity;  //更新的数据实体
    private AlertDialog mAlertDialog;      //当前对话框对象
    private Context mContext;


    public VersionUpdateDialog(@NonNull Context context,AppInfoEntity mAppInfoEntity) {
        super(context, R.style.UpdateDialog);
        this.mAppInfoEntity = mAppInfoEntity;
        this.mContext = context;
        mView = View.inflate(context,R.layout.dialog_update,null);
         initView();
        initData();
        setListen();
        setView(mView);
    }




    /**
     * 初始化视图
     /**
     */
    private void initView() {
        mTvNewVersion = mView.findViewById(R.id.dialog_update_tv_new_version );
        mTvUpdateSize = mView.findViewById(R.id.dialog_update_tv_apk_size );
        mTvUpdateContent = mView.findViewById(R.id.dialog_update_tv_update_content );
        mTvCanner = mView.findViewById(R.id.dialog_update_tv_canner );
        mTvUpdate = mView.findViewById(R.id.dialog_update_tv_update );
    }


    /**
     * 初始化数据
     */
    private void initData() {
        //取消点击外部关闭对话框
        this.setCancelable(false);
        //如果为强制退出更改按钮文本
        if(mAppInfoEntity.isForceUpdate()){

            mTvCanner.setText("退出");
        }else {
            mTvCanner.setText("取消");
        }
        //设置对话框显示的数据
        mTvNewVersion.setText(mAppInfoEntity.getNewVersion());
        mTvUpdateSize.setText(String.valueOf(mAppInfoEntity.getApkSize()));
        mTvUpdateContent.setText(mAppInfoEntity.getUpdateContent());

    }


    /**
     * 设置事件
     */
    private void setListen() {
        mTvCanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVersionUpdateListen != null){
                    if(mAppInfoEntity.isForceUpdate()){
                        Log.d(TAG,"exit");
                        mVersionUpdateListen.exit();
                    }else {
                        mVersionUpdateListen.canner();
                        Log.d(TAG,"canner");
                    }
                }
            }
        });

        mTvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVersionUpdateListen != null){
                    mVersionUpdateListen.update();
                    Log.d(TAG,"update");
                }
            }
        });
    }




    /**
     * 显示对话框
     */
    public void showDialog(){
        mAlertDialog = this.show();
        setWindowSize(mContext);
    }


    private void setWindowSize(Context context) {
        Window dialogWindow = mAlertDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenUtil.getWith(context) * 0.7f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }


    /**
     * 隐藏对话框
     */
    public void hideDialog(){
        mAlertDialog.dismiss();
    }


    interface versionUpdateListen{
        void canner();
        void update();
        void exit();
    }
    private versionUpdateListen mVersionUpdateListen;
    public void setmVersionUpdateListen(versionUpdateListen mVersionUpdateListen){
        this.mVersionUpdateListen = mVersionUpdateListen;
    }

}
