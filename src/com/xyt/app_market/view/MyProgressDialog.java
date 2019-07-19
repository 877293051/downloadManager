package com.xyt.app_market.view;

import com.xyt.app_market.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProgressDialog extends ProgressDialog {

    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mTextView;
    private String loadingTip;
    private int resid;

    /**
     * 
     * @param context 上下文对象
     * @param content 显示文字提示信息内容
     * @param id 动画id
     */
    public MyProgressDialog(Context context, String content, int resid) {
        super(context);
        this.loadingTip = content;
        this.resid = resid;
        //点击提示框外面是否取消提示框
        setCanceledOnTouchOutside(false);
        //点击返回键是否取消提示框
        setCancelable(false);
        setIndeterminate(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        
        mTextView = (TextView) findViewById(R.id.id_dialog_text);
        mImageView = (ImageView) findViewById(R.id.id_dialog_image);
        
        mImageView.setBackgroundResource(resid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mTextView.setText(loadingTip);
    }
}