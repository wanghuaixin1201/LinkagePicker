package com.example.selfunction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.selfunction.databinding.ActivityQrcodeBinding;
import com.example.selfunction.utils.ImageSaveUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SavePhotoActivity extends AppCompatActivity {

    private ActivityQrcodeBinding binding;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //当前为腾讯网logo，加载异常可换为其它线上图片
        img = "https://mat1.gtimg.com/pingjs/ext2020/qqindex2018/dist/img/qq_logo_2x.png";
        if (null == img) {
            img = "";
        }

        if (null != img) {
            Glide.with(SavePhotoActivity.this).load(img).error(getResources().getDrawable(R.mipmap.ic_launcher)).dontAnimate().into(binding.ivImg);

            binding.tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returnBitMap(img);
                }
            });
        }

        //REQUEST_GPS为自定义int型静态常量；private final int REQUEST_GPS = 1;
        ActivityCompat.requestPermissions(SavePhotoActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 208);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //保存图片的方法，自定义
        } else {
            //Permission Denied
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Uri uri = ImageSaveUtil.saveAlbum(SavePhotoActivity.this, bitmap, Bitmap.CompressFormat.PNG, 100, true);
                    if (null != uri) {
                        Toast.makeText(SavePhotoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SavePhotoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    Bitmap bitmap;

    public Bitmap returnBitMap(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Log.e("bitmap优质", bitmap + "");
                    Message msg = handler.obtainMessage(1);
                    handler.sendMessage(msg);
                    //这是一个一步请求，不能直接返回获取，要不然永远为null
                    //在这里得到BitMap之后记得使用Hanlder或者EventBus传回主线程，不过现在加载图片都是用框架了，很少有转化为Bitmap的需求
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }
}