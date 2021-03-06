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

        //??????????????????
        picker = new iSelectPicker(MainActivity.this,8868);
        //?????????????????????
        picker.initAddressData();

        //???????????????????????????
        binding.refreshLayout.setOnRefreshLoadMoreListener(this);

        //??????????????????
        binding.tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                for(int i =1;i<6;i++){
                    list.add("???"+i+"???");
                }
                picker.showOneDiaLog("??????????????????",list, new OnOneCallBackListener() {
                    @Override
                    public void onOne(String value) {
                        binding.tvOne.setText(value);
                    }
                });
            }
        });

        //??????????????????
        binding.tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = new ArrayList<>();
                List<List<String>> list2 = new ArrayList<>();
                for(int i =1;i<8;i++){
                    list.add("??????"+i);
                    List<String> str = new ArrayList<>();
                    for(int j =1;j<10;j++) {
                        str.add("??????" + j);
                    }
                    list2.add(str);
                }
                picker.showTwoDiaLog("??????????????????",list,list2, new OnTwoCallBackListener() {
                    @Override
                    public void onTwo(String pCate, String cate) {
                        binding.tvTwo.setText(pCate + "-" + cate);
                    }
                });
            }
        });

        //??????????????????????????????????????????????????????????????????????????????copy??????????????????????????????????????????
        binding.tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.showAddress("??????????????????", new OnAddressCallBackListener() {
                    @Override
                    public void onAddress(String province, String provinceId, String city, String cityId, String area, String areaId) {
                        binding.tvThree.setText(province + city + area);
                        Log.e("?????????ID",provinceId+"-"+cityId+"-"+areaId);
                    }
                });
            }
        });

        //??????????????????
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

                picker.showNoLinkOptionsPicker("??????????????????",list,list2,list3, new OnNoLinkCallBackListener() {
                    @Override
                    public void onData(String one, String two, String three) {
                        binding.tvFour.setText(one + "-" + two + "-" + three);
                    }
                });
            }
        });

        //????????????????????????????????????????????????
        binding.tvFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkConnected = Utils.isNetworkConnected(MainActivity.this);
                if(!networkConnected){
                    Toast.makeText(MainActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                picker.selLocation("????????????", getResources().getColor(R.color.blue), getResources().getColor(R.color.white),9000);
            }
        });

        //???????????????
        binding.tvSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> img = new ArrayList<>();
                picker.selectImageAndVideoPicker("???????????????",true,true,false,true,6,img,1000);
            }
        });

        //???????????????
        binding.tvSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * @param REQUEST_CODE_VIDEOS ??????????????????request????????????onActicityResult????????????????????????
                 * @param REQUEST_CODE_CAMERA ??????????????????request????????????onActicityResult????????????????????????
                 */
                picker.showSelectVideoDialog("???????????????",REQUEST_CODE_VIDEOS,REQUEST_CODE_CAMERA);
            }
        });

        //????????????
        binding.tvEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.openScan(1);
            }
        });

        //???????????????
        binding.tvNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar today = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
                int year = today.get(Calendar.YEAR); //????????????-???
                int month = today.get(Calendar.MONTH); //????????????-??????????????????-1
                int day = today.get(Calendar.DATE); //????????????-???

                //???????????????????????????????????? true:?????? false:??????
                boolean[] showType = {true, true, true, false, false, false};
                picker.showTime("???????????????", "yyyy-MM-dd", year, month, day, 2060, 1, 28, showType, new OnTimeCallBackListener() {
                    @Override
                    public void onTime(String date, long time) {
                        Log.e("long??????",time+"");
                        binding.tvNine.setText(date);
                    }
                });
            }
        });

        //?????????????????????
        binding.tvTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //??????00:00 - 23:59?????????????????????times????????? - ??????????????????
                picker.showTimeInterval("?????????????????????", "00:00 - 23:59", new OnTimeIntervalCallBackListener() {
                    @Override
                    public void onTime(String time) {
                        binding.tvTen.setText(time);
                    }
                });
            }
        });

        //?????????????????????
        binding.tvEleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //??????????????????logo
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

    //??????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 8868) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //???????????????????????????????????????
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        Log.e("MainActivity", "???????????????");
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //????????????
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            if (imagePaths.size() > 0) {
                for (String aa : imagePaths) {
                    Log.e("????????????", aa);
                }

                ImageListAdapter nadaptor = new ImageListAdapter(MainActivity.this, imagePaths, imagePaths.size());
                GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 3);
                //?????????????????????
                binding.rvImg.setLayoutManager(manager);
                binding.rvImg.setAdapter(nadaptor);
            }
        }
        //????????????
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
        //??????????????????
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                // ????????????
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

        //????????????
        if (resultCode == 1001 && requestCode == 1) {
            Bundle bundle = data.getExtras();
            String codes = bundle.getString("codes");
            Log.e("????????????", codes);
            binding.tvEight.setText("???????????????"+codes);
            SharedPreferences sp = getSharedPreferences("saveInfo", MODE_PRIVATE);//?????????sharedprefrences
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("codesign", codes);
            editor.commit();
        }

        //??????????????????
        if (requestCode == 9000 && resultCode == 200) {
            String address = data.getStringExtra("address");
            String latng = data.getStringExtra("latng");
            String[] split = latng.split(",");
            if (null != address) {
                binding.tvFive.setText(address);
                for (int i = 0; i < split.length; i++) {
                    Log.e("?????????", split[i]);
                    if (i == 0) {
                        if (null != split[i]) {
                            String latitude = split[i];//??????
                        }
                    } else if (i == 1) {
                        if (null != split[i]) {
                            String longitude = split[i];//??????
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        Log.e(TAG,"??????page???"+page);
        binding.refreshLayout.finishLoadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page=1;
        Log.e(TAG,"??????page???"+page);
        binding.refreshLayout.finishRefresh();
    }
}