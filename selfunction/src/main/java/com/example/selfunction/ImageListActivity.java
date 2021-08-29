package com.example.selfunction;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class ImageListActivity extends Activity {

    private ImageView iv_pic;
    private int index;
    private float touchDowmX;
    private float touchUpX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagelist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }

        String img = getIntent().getStringExtra("img");


        //获取imageSwitch
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        //设置视图工厂
        if(null != img) {
            Glide.with(ImageListActivity.this).load(img).error(getResources().getDrawable(R.mipmap.ic_launcher)).dontAnimate().into(iv_pic);
        }
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
