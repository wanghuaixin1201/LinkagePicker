package com.example.selfunction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActicity extends Activity {
    private QRCodeView mQRCodeView;
    private Activity activity;
    public String scanCode = "";
    String TAG = "ScanActicity";
    private Button btCloseFlashLight;
    private Button btOpenFlashLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.changeToScanQRCodeStyle(); //扫二维码
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {

            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("二维码扫描结果", "result:" + result);
                //扫描得到结果震动一下表示
                vibrate();
                scanCode = result;
                Intent intent = getIntent();
                intent.putExtra("codes",result);
                setResult(1001,intent);
                finish();

                //获取结果后三秒后，重新开始扫描
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mQRCodeView.startSpot();
//                    }
//                }, 3000);
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
            }
        });

        //开始扫码
        mQRCodeView.startSpot();

        findViewById(R.id.start_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.startSpot();
            }
        });

        btCloseFlashLight = (Button)findViewById(R.id.close_flashlight);
        btOpenFlashLight = (Button) findViewById(R.id.open_flashlight);
        btCloseFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.closeFlashlight();
                btCloseFlashLight.setVisibility(View.GONE);
                btOpenFlashLight.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.stop_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.stopSpot();
            }
        });


        btOpenFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.openFlashlight();
                btOpenFlashLight.setVisibility(View.GONE);
                btCloseFlashLight.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        //强制手机摄像头镜头朝向前边
        //mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect(); //显示扫描方框
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    //震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
