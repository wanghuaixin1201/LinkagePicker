package com.example.selfunction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.selfunction.databinding.ActivitySelLocationBinding;

import java.net.URLDecoder;

public class SelLocationActivity extends AppCompatActivity {

    private String address = "";
    private String latng;
    private String TAG = "SelLocationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySelLocationBinding binding = ActivitySelLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView tv_confrim = findViewById(R.id.tv_confrim);

        WebView mWebView = findViewById(R.id.web_view);

        String title = getIntent().getStringExtra("title");
        int bgColor = getIntent().getIntExtra("bgColor",getResources().getColor(R.color.blue));
        int textColor = getIntent().getIntExtra("textColor",getResources().getColor(R.color.white));

        binding.rlTitle.setBackgroundColor(bgColor);
        binding.tvConfrim.setTextColor(textColor);
        binding.tvTitle.setTextColor(textColor);
        if(null != title) {
            binding.tvTitle.setText(title);
        }

        String mUrl = "https://apis.map.qq.com/tools/locpicker?search=1&type=0&backurl=http://callback&key=QULBZ-6M6KO-5YZWR-SEYTJ-GNNS5-O6B3L&referer=myapp";

        WebSettings settings = mWebView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSupportMultipleWindows(true);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
        settings.setAllowFileAccess(false);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http://callback")) {
                    view.loadUrl(url);
                } else {
                    try {
                        Log.e(TAG,url);

                        //转utf-8编码
                        String decode = URLDecoder.decode(url, "UTF-8");
                        Log.e(TAG,decode);
                        //转uri，然后根据key取值
                        Uri uri = Uri.parse(decode);
                        //纬度在前，经度在后，以逗号分隔
                        latng = uri.getQueryParameter("latng");
                        String[] split = latng.split(",");
                        String lat = split[0];//纬度
                        String lng = split[1];//经度
                        //地址
                        address = uri.getQueryParameter("addr");
                        String name = uri.getQueryParameter("name");
                        address = address+name;
//                        uri.getQueryParameter("addr");
                        Log.e("地址", address);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
        mWebView.loadUrl(mUrl);

        tv_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == address){
                    Toast.makeText(SelLocationActivity.this, "请选择位置", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.isEmpty(address)){
                    Intent intent = new Intent();
                    Log.e("点击了",address);
                    intent.putExtra("address", address);
                    intent.putExtra("latng", latng);

                    setResult(200, intent);
                    finish();
                }else{
                    Toast.makeText(SelLocationActivity.this, "请选择位置", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}