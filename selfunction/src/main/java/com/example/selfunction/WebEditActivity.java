package com.example.selfunction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SizeUtils;
import com.example.selfunction.databinding.ActivityWebEditBinding;
import com.example.selfunction.utils.GlideLoader;
import com.example.selfunction.view.RichEditor;
import com.lcw.library.imagepicker.ImagePicker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class WebEditActivity extends AppCompatActivity {

    private ActivityWebEditBinding binding;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private int REQUEST_CODE_CAMERA = 1201;
    private ArrayList<String> videoPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.ivSelPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.getInstance()
                        .setTitle("图片选择器")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(false)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImagePaths(new ArrayList<>())
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(WebEditActivity.this, 1000);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode

            }
        });

        binding.ivSelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectVideoDialog();
            }
        });

        //富文本框监听事件
        binding.richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.e("富文本内容",text);
                Message message = handler.obtainMessage(1);
                handler.sendMessage(message);
//                binding.richEditor.setHtml(text);//传入富文本框H5数据显示

            }
        });

        WebSettings webSettings=binding.webView.getSettings();
        //允许webview对文件的操作
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

        initEditor();
    }

    private void initEditor() {
        //输入框显示字体的大小
        binding.richEditor.setEditorFontSize(14);
        //输入框显示字体的颜色
        binding.richEditor.setEditorFontColor(getResources().getColor(R.color.black));
        //输入框背景设置
        binding.richEditor.setEditorBackgroundColor(Color.WHITE);
        //输入框文本padding
        binding.richEditor.setPadding(0, 0, 0, 0);
        //输入提示文本
        binding.richEditor.setPlaceholder("请输入详情内容");
        binding.richEditor.setEditorHeight(SizeUtils.dp2px(100));

        binding.richEditor.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<RichEditor.Type> types) {
                ArrayList<String> flagArr = new ArrayList<>();
                for (int i = 0; i < types.size(); i++) {
                    flagArr.add(types.get(i).name());
                }

            }
        });

        binding.richEditor.setImageClickListener(new RichEditor.ImageClickListener() {
            @Override
            public void onImageClick(String imageUrl) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (imagePaths.size() > 0) {
                Log.e("选择路径", imagePaths.get(0));

                String imgUrl = imagePaths.get(0);

                binding.richEditor.focusEditor();
                binding.richEditor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.richEditor != null) {
                            binding.richEditor.scrollToBottom();
                        }
                    }
                }, 200);
                binding.richEditor.insertImage(imgUrl + "", "wanghuaixin");

            }
        }

        if (requestCode == 2000 && resultCode == RESULT_OK) {
            videoPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (videoPaths.size() > 0) {

                String videoUrl = videoPaths.get(0);
                if(null == videoUrl){
                    return;
                }
                binding.richEditor.focusEditor();
                binding.richEditor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.richEditor != null) {
                            binding.richEditor.scrollToBottom();
                        }
                    }
                }, 200);
                binding.richEditor.insertVideo(videoUrl,"100%","auto");
            }
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                // 视频路径
                @SuppressLint("Range") String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                videoPaths = new ArrayList<>();
                videoPaths.add(filePath);

                String videoUrl = videoPaths.get(0);
                if(null == videoUrl){
                    return;
                }
                binding.richEditor.focusEditor();
                binding.richEditor.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.richEditor != null) {
                            binding.richEditor.scrollToBottom();
                        }
                    }
                }, 200);
                binding.richEditor.insertVideo(videoUrl,"100%","auto");

                cursor.close();
            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String html = binding.richEditor.getHtml();
                    if (null != html) {
                        Log.e("打印数据呀", html + "--");
                        binding.etHtml.setText(html);
                        binding.webView.loadDataWithBaseURL("", html.replaceAll("src=\"","src=\"file://"), "text/html","utf-8", "");
                    }
                    break;
            }
        }
    };

    public void showSelectVideoDialog() {
        Dialog bottomDialog = new Dialog(WebEditActivity.this, R.style.BottomDialog);//自定义样式
        View contentView = LayoutInflater.from(WebEditActivity.this).inflate(R.layout.dialog_bottom_picture, null);
        bottomDialog.setContentView(contentView);
        Button choosePhoto = (Button) contentView.findViewById(R.id.picture_gllary);
        Button takePhoto = (Button) contentView.findViewById(R.id.picture_camera);
        Button cancel = (Button) contentView.findViewById(R.id.picture_cancle);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.hide();
                ImagePicker.getInstance()
                        .setTitle("视频选择器")//设置标题
                        .showCamera(false)//设置是否显示拍照按钮
                        .showImage(false)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(WebEditActivity.this, 2000);
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.hide();
                // 拍摄视频
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                // 录制视频最大时长15s
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.hide();
            }
        });

        //<editor-fold desc="距离下，左右边框距离，根据需求可删除或者增大">
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(16f);
        params.bottomMargin = SizeUtils.dp2px(8f);
        contentView.setLayoutParams(params);
        //</editor-fold>

        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    private void detailWebView(String content) {
        String newContent = getNewContent(content);
        Log.e("数据",newContent);
        binding.webView.loadData(newContent, "text/html; charset=utf-8", "utf-8");
        // 设置支持Javascript
        binding.webView.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色
        binding.webView.setBackgroundColor(0);
    }

    private static String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "200px");
        }
        return doc.toString();
    }
}