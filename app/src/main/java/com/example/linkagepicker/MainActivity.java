package com.example.linkagepicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.linkagepicker.R;
import com.example.linkagepicker.databinding.ActivityMainBinding;
import com.example.selfunction.WebEditActivity;
import com.example.selfunction.adapter.ImageListAdapter;
import com.example.selfunction.listener.OnAddressCallBackListener;
import com.example.selfunction.listener.OnNoLinkCallBackListener;
import com.example.selfunction.listener.OnOneCallBackListener;
import com.example.selfunction.listener.OnTimeCallBackListener;
import com.example.selfunction.listener.OnTimeIntervalCallBackListener;
import com.example.selfunction.listener.OnTwoCallBackListener;
import com.example.selfunction.utils.ScreenUtils;
import com.example.selfunction.utils.Utils;
import com.example.selfunction.utils.iSelectPicker;
import com.lcw.library.imagepicker.ImagePicker;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JZVideoPlayerStandard;

public class MainActivity extends AppCompatActivity implements OnRefreshLoadMoreListener {

    private ActivityMainBinding binding;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ArrayList<String> videoPaths = new ArrayList<>();
    private int REQUEST_CODE_VIDEOS = 2000;
    private int REQUEST_CODE_CAMERA = 1201;
    private int page = 1;
    private String TAG = "MainActivity";

    private iSelectPicker picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ScreenUtils.setStatusBarTransparent(getWindow());

        //初始化工具类
        picker = new iSelectPicker(MainActivity.this,8868);
        //初始化地址数据
        picker.initAddressData();

        //上拉、下拉绑定事件
        binding.refreshLayout.setOnRefreshLoadMoreListener(this);

        //一级联动选择
        binding.tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                for(int i =1;i<6;i++){
                    list.add("第"+i+"个");
                }
                picker.showOneDiaLog("一级联动选择",list, new OnOneCallBackListener() {
                    @Override
                    public void onOne(String value) {
                        binding.tvOne.setText(value);
                    }
                });
            }
        });

        //二级联动选择
        binding.tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                List<List<String>> list2 = new ArrayList<>();
                for(int i =1;i<8;i++){
                    list.add("父级"+i);
                    List<String> str = new ArrayList<>();
                    for(int j =1;j<10;j++) {
                        str.add("子级" + j);
                    }
                    list2.add(str);
                }
                picker.showTwoDiaLog("二级联动选择",list,list2, new OnTwoCallBackListener() {
                    @Override
                    public void onTwo(String pCate, String cate) {
                        binding.tvTwo.setText(pCate + "-" + cate);
                    }
                });
            }
        });

        //三级联动一般用于地址选择，这里数据是写死的，正常使用copy代码换为接口返回数据直接使用
        binding.tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.showAddress("三级联动选择", new OnAddressCallBackListener() {
                    @Override
                    public void onAddress(String province, String provinceId, String city, String cityId, String area, String areaId) {
                        binding.tvThree.setText(province + city + area);
                        Log.e("省市区ID",provinceId+"-"+cityId+"-"+areaId);
                    }
                });
            }
        });

        //非联动选择器
        binding.tvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> list = new ArrayList<>();
                ArrayList<String> list2 = new ArrayList<>();
                ArrayList<String> list3 = new ArrayList<>();

                list.add("1");
                list.add("2");
                list.add("3");

                list2.add("A");
                list2.add("B");
                list2.add("C");

                list3.add("one");
                list3.add("two");
                list3.add("three");
                list3.add("four");
                list3.add("five");
                list3.add("six");

                picker.showNoLinkOptionsPicker("非联动选择器",list,list2,list3, new OnNoLinkCallBackListener() {
                    @Override
                    public void onData(String one, String two, String three) {
                        binding.tvFour.setText(one + "-" + two + "-" + three);
                    }
                });
            }
        });

        //地图选址（注：需确保手机已联网）
        binding.tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkConnected = Utils.isNetworkConnected(MainActivity.this);
                if(!networkConnected){
                    Toast.makeText(MainActivity.this, "请连接网络", Toast.LENGTH_SHORT).show();
                    return;
                }
                picker.selLocation("地图选址", getResources().getColor(R.color.blue), getResources().getColor(R.color.white),9000);
            }
        });

        //图片选择器
        binding.tvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> img = new ArrayList<>();
                picker.selectImageAndVideoPicker("图片选择器",true,true,false,true,6,img,1000);
            }
        });

        //视频选择器
        binding.tvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * @param REQUEST_CODE_VIDEOS 相册请求回调request值，用于onActicityResult页面回调接收数据
                 * @param REQUEST_CODE_CAMERA 录制请求回调request值，用于onActicityResult页面回调接收数据
                 */
                picker.showSelectVideoDialog("视频选择器",REQUEST_CODE_VIDEOS,REQUEST_CODE_CAMERA);
            }
        });

        //打开扫码
        binding.tvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.openScan(1);
            }
        });

        //时间选择器
        binding.tvNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar today = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
                int year = today.get(Calendar.YEAR); //开始时间-年
                int month = today.get(Calendar.MONTH); //开始时间-月，月份无需-1
                int day = today.get(Calendar.DATE); //开始时间-日

                //设置年月日时分秒是否显示 true:显示 false:隐藏
                boolean[] showType = {true, true, true, false, false, false};
                picker.showTime("时间选择器", "yyyy-MM-dd", year, month, day, 2060, 1, 28, showType, new OnTimeCallBackListener() {
                    @Override
                    public void onTime(String date, long time) {
                        Log.e("long日期",time+"");
                        binding.tvNine.setText(date);
                    }
                });
            }
        });

        //时间区间选择器
        binding.tvTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //默认00:00 - 23:59，可修改（注：times参数里 - 必须有空格）
                picker.showTimeInterval("时间区间选择器", "00:00 - 23:59", new OnTimeIntervalCallBackListener() {
                    @Override
                    public void onTime(String time) {
                        binding.tvTen.setText(time);
                    }
                });
            }
        });

        //保存图片到本地
        binding.tvEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当前为腾讯网logo
                String img = "https://mat1.gtimg.com/pingjs/ext2020/qqindex2018/dist/img/qq_logo_2x.png";
                picker.savePhoto(img);
            }
        });

        binding.tvTwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WebEditActivity.class));
            }
        });
    }

    //权限请求回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 8868) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Log.e("MainActivity", "权限未申请");
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片回调
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (imagePaths.size() > 0) {
                for (String aa : imagePaths) {
                    Log.e("选择路径", aa);
                }

                ImageListAdapter nadaptor = new ImageListAdapter(MainActivity.this, imagePaths, imagePaths.size());
                GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 3);
                //设置布局管理者
                binding.rvImg.setLayoutManager(manager);
                binding.rvImg.setAdapter(nadaptor);
            }
        }
        //视频回调
        if (requestCode == REQUEST_CODE_VIDEOS && resultCode == RESULT_OK) {
            videoPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (videoPaths.size() > 0) {

                binding.vvVideo.setUp(videoPaths.get(0),
                        cn.jzvd.JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                        "");
                binding.vvVideo.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(MainActivity.this).load(videoPaths.get(0)).error(getResources().getDrawable(R.mipmap.ic_launcher)).dontAnimate().into(binding.vvVideo.thumbImageView);
                binding.vvVideo.setVisibility(View.VISIBLE);
            }
        }
        //视频录制回调
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                // 视频路径
                @SuppressLint("Range") String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));

                videoPaths = new ArrayList<>();
                videoPaths.add(filePath);

                binding.vvVideo.setUp(videoPaths.get(0),
                        JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                        "");
                binding.vvVideo.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(MainActivity.this).load(videoPaths.get(0)).error(getResources().getDrawable(R.mipmap.ic_launcher)).dontAnimate().into(binding.vvVideo.thumbImageView);
                binding.vvVideo.setVisibility(View.VISIBLE);

                cursor.close();
            }
        }

        //扫码回调
        if (resultCode == 1001 && requestCode == 1) {
            Bundle bundle = data.getExtras();
            String codes = bundle.getString("codes");
            Log.e("扫描结果", codes);
            binding.tvEight.setText("扫描结果："+codes);
            SharedPreferences sp = getSharedPreferences("saveInfo", MODE_PRIVATE);//初始化sharedprefrences
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("codesign", codes);
            editor.commit();
        }

        //地图选址回调
        if (requestCode == 9000 && resultCode == 200) {
            String address = data.getStringExtra("address");
            String latng = data.getStringExtra("latng");
            String[] split = latng.split(",");
            if (null != address) {
                binding.tvFive.setText(address);
                for (int i = 0; i < split.length; i++) {
                    Log.e("经纬度", split[i]);
                    if (i == 0) {
                        if (null != split[i]) {
                            String latitude = split[i];//纬度
                        }
                    } else if (i == 1) {
                        if (null != split[i]) {
                            String longitude = split[i];//经度
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        Log.e(TAG,"当前page："+page);
        binding.refreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        Log.e(TAG,"当前page："+page);
        binding.refreshLayout.finishRefresh();
    }
}