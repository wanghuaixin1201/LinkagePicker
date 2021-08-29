package com.example.selfunction.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.selfunction.utils.*;
import com.example.selfunction.view.*;
import com.example.selfunction.listener.*;
import com.example.selfunction.view.*;
import com.example.selfunction.bean.*;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.*;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.SizeUtils;
import com.example.selfunction.R;
import com.example.selfunction.ScanActicity;
import com.example.selfunction.SelLocationActivity;
import com.example.selfunction.view.TimeRangePickerDialog;
import com.lcw.library.imagepicker.ImagePicker;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * create by wanghuaixin
 * 开放工具类
 */
public class iSelectPicker{

    private Activity mContext;
    private String TAG = "iSelectPicker";
    public static OptionsPickerView oneOptions;
    public static OptionsPickerView twoOptions;
    public static OptionsPickerView ThreeOptions;

    private ArrayList<String> address1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> address2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> address3Items = new ArrayList<>();

    private OptionsPickerView pvNoLinkOptions;
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();

    private AddressDataBean addressDataBean;

    public iSelectPicker(@NonNull Activity context,@NonNull int permission_RequestCode){
        mContext = context;
        initPermission(permission_RequestCode);
    }

    private void initPermission(int permission_RequestCode) {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(mContext, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(mContext, permissions, permission_RequestCode);
        }
    }

    /**
     * @param title 标题
     * @param data 数据集合
     * @param listener 回调事件
     */
    //一级联动选择
    public void showOneDiaLog(@NonNull String title,@NonNull List<String> data, @NonNull OnOneCallBackListener listener){

        oneOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String tx = data.get(options1);

                listener.onOne(tx);
            }
        })
                .setTitleText(title)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(mContext.getResources().getColor(R.color.white))
                .setTitleColor(mContext.getResources().getColor(R.color.black))
                .setCancelColor(mContext.getResources().getColor(R.color.hint))
                .setSubmitColor(mContext.getResources().getColor(R.color.blue))
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(mContext.getResources().getColor(R.color.popup)) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddGoodActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        oneOptions.setPicker(data);//一级选择器
        oneOptions.show();
    }

    /**
     * 二级联动选择
     *
     * @param title 标题
     * @param list 一级分类
     * @param list2 二级分类
     * @param listener 回调事件
     */
    public void showTwoDiaLog(@NonNull String title, List<String> list,List<List<String>> list2,OnTwoCallBackListener listener) {
        twoOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是两个级别的选中位置
                String tx = list.get(options1)+list2.get(options1).get(options2);

                listener.onTwo(list.get(options1),list2.get(options1).get(options2));
            }
        })
                .setTitleText(title)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(mContext.getResources().getColor(R.color.white))
                .setTitleColor(mContext.getResources().getColor(R.color.black))
                .setCancelColor(mContext.getResources().getColor(R.color.hint))
                .setSubmitColor(mContext.getResources().getColor(R.color.blue))
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(mContext.getResources().getColor(R.color.popup)) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2;
//                        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        twoOptions.setPicker(list, list2);//二级选择器
        twoOptions.show();
    }

    //初始化地址数据
    public void initAddressData() {
        String JsonData = new GetJsonDataUtil().getJson(mContext, "province.json");//获取assets目录下的json文件数据

        addressDataBean = JSONObject.parseObject(JsonData, AddressDataBean.class);
        Message msg = handler.obtainMessage(2);
        handler.sendMessage(msg);
    }

    /**
     * @param title 标题
     * @param listener 回调事件
     */
    //三级联动选择
    public void showAddress(@NonNull String title,@NonNull OnAddressCallBackListener listener) {
        ThreeOptions = new com.bigkoo.pickerview.builder.OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = address1Items.get(options1)+ address2Items.get(options1).get(options2) + address3Items.get(options1).get(options2).get(options3);

                String areaId = "";
                String cityId = "";
                String provinceId = "";

                for(AddressDataBean.Cityjson aa:addressDataBean.getCityjson()){
                    for(AddressDataBean.Children bb:aa.getChildren()){
                        for(AddressDataBean.Childrens cc:bb.getChildren()){
                            if(address1Items.get(options1).equals(aa.getLabel()) && address2Items.get(options1).get(options2).equals(bb.getLabel()) && address3Items.get(options1).get(options2).get(options3).equals(cc.getLabel())){
                                //省ID
                                provinceId = aa.getValue();
                                //市ID
                                cityId = bb.getValue();
                                //区县ID
                                areaId = cc.getValue();
                            }
                        }
                    }
                }
                listener.onAddress(address1Items.get(options1),provinceId, address2Items.get(options1).get(options2),cityId, address3Items.get(options1).get(options2).get(options3),areaId);
            }
        })
                .setTitleText(title)
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0,0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(mContext.getResources().getColor(R.color.white))
                .setTitleColor(mContext.getResources().getColor(R.color.black))
                .setCancelColor(mContext.getResources().getColor(R.color.hint))
                .setSubmitColor(mContext.getResources().getColor(R.color.blue))
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(mContext.getResources().getColor(R.color.popup)) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        //滑动位置实时回调
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        ThreeOptions.setPicker(address1Items, address2Items, address3Items);//三级选择器
        ThreeOptions.show();
    }

    /**
     * @param title 标题
     * @param list 一级数据
     * @param list2 二级数据
     * @param list3 三级数据
     * @param listener 回调事件
     */
    // 不联动的多级选项
    public void showNoLinkOptionsPicker(@NonNull String title,@NonNull ArrayList<String> list,@NonNull ArrayList<String> list2,@NonNull ArrayList<String> list3,@NonNull OnNoLinkCallBackListener listener) {

        pvNoLinkOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = "numData:" + list.get(options1)
                        + "\nletterData:" + list2.get(options2)
                        + "\nenglishData:" + list3.get(options3);

                listener.onData(list.get(options1),list2.get(options2),list3.get(options3));
            }
        }).setTitleText(title)
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .setItemVisibleCount(5)
                .setSelectOptions(0, 0, 0)
                .build();
        pvNoLinkOptions.setNPicker(list, list2, list3);

        pvNoLinkOptions.show();
    }

    /**
     * @param title 标题
     * @param bgColor 选择地址页面标题栏背景颜色
     * @param textColor 选择地址页面标题栏字体颜色
     * @param requestCode 请求回调request值，用于onActicityResult页面回调接收数据
     */
    public void selLocation(String title,int bgColor,int textColor,@NonNull int requestCode){
        Intent intent = new Intent(mContext, SelLocationActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("bgColor",bgColor);
        intent.putExtra("textColor",textColor);
        mContext.startActivityForResult(intent, requestCode);
    }

    /**
     * @param title 标题
     * @param requestCode 相册请求回调request值，用于onActicityResult页面回调接收数据
     * @param requestCode2 录制请求回调request值，用于onActicityResult页面回调接收数据
     */
    public void showSelectVideoDialog(@NonNull String title,int requestCode,@NonNull int requestCode2) {
        Dialog bottomDialog = new Dialog(mContext, R.style.BottomDialog);//自定义样式
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_bottom_picture, null);
        bottomDialog.setContentView(contentView);
        Button choosePhoto = (Button) contentView.findViewById(R.id.picture_gllary);
        Button takePhoto = (Button) contentView.findViewById(R.id.picture_camera);
        Button cancel = (Button) contentView.findViewById(R.id.picture_cancle);
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.hide();
                ArrayList<String> video = new ArrayList<>();
                selectImageAndVideoPicker(title,false,false,true,true,1,video,requestCode);
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
                mContext.startActivityForResult(intent, requestCode2);
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
        params.width = mContext.getResources().getDisplayMetrics().widthPixels - SizeUtils.dp2px(16f);
        params.bottomMargin = SizeUtils.dp2px(8f);
        contentView.setLayoutParams(params);
        //</editor-fold>

        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    /**
     * @param requestCode 请求回调request值，用于onActicityResult页面回调接收数据
     */
    public void openScan(@NonNull int requestCode){
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 请求授权
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA}, 1);
        }else{
            Intent intent = new Intent(mContext, ScanActicity.class);
            mContext.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * @param title 标题
     * @param dateFormat 日期回调格式
     * @param startYear 开始时间-年
     * @param startMonth 开始时间-月
     * @param startDay 开始时间-日
     * @param endYear 结束时间-年
     * @param endMonth 结束时间-月
     * @param endDay 结束时间-日
     * @param showType //设置年月日时分秒是否显示 true:显示 false:隐藏
     * @param listener //回调监听事件
     */
    public void showTime(@NonNull String title, @NonNull String dateFormat, @NonNull int startYear, @NonNull int startMonth, @NonNull int startDay, @NonNull int endYear, @NonNull int endMonth, @NonNull int endDay, @NonNull boolean[] showType, @NonNull OnTimeCallBackListener listener) {


        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();//控件起始时间
        //注：（1）年份可以随便设置 (2)月份是从0开始的（0代表1月 11月代表12月），即设置0代表起始时间从1月开始
        //(3)日期必须从1开始，因为2月没有30天，设置其他日期时，2月份会从设置日期开始显示导致出现问题
        startDate.set(startYear, startMonth, startDay);//该控件从今日开始
        Calendar endDate = Calendar.getInstance();//控件截止时间
        endDate.set(endYear, endMonth, endDay);//该控件到2060年2月28日结束
        try {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            String start1 = format.format(new Date());
            Log.e("msg", start1);
            Date date = formatter.parse(start1);
            selectedDate.setTime(date);//指定控件初始值显示哪一天
        } catch (Exception e) {

        }
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            public void onTimeSelect(final Date date, View v) {
                String choiceTime = formatter.format(date);//日期 String

                long startl = date.getTime();//日期 long

                listener.onTime(choiceTime,startl);
            }
        }).setTitleText(title)
                .setDate(selectedDate)//设置系统时间为当前时间
                .setRangDate(startDate, endDate)//设置控件日期范围 也可以不设置默认1900年到2100年
                .setType(showType)
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(mContext.getResources().getColor(R.color.blue))//设置分割线颜色
                .isCyclic(false)//是否循环显示日期 例如滑动到31日自动转到1日 有个问题：不能实现日期和月份联动
                .build();
        pvTime.show();
    }

    /**
     * @param title 标题
     * @param times 时间区间
     * @param listener 回调监听事件
     */
    public void showTimeInterval(String title, String times, OnTimeIntervalCallBackListener listener){
        //默认00:00 - 23:59，可修改
        TimeRangePickerDialog dialog = new TimeRangePickerDialog(mContext, times, new TimeRangePickerDialog.ConfirmAction() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick(String startAndEndTime) {
                //选择时间回调
//                String timeData = startAndEndTime.replace(" ", "");去除空格
                listener.onTime(startAndEndTime);
            }
        },title);

        dialog.show();
    }

    /**
     * @param title 设置标题
     * @param showCamera 设置是否显示拍照按钮
     * @param showImage 设置是否展示图片
     * @param showVideo 设置是否展示视频
     * @param singleType 设置图片视频不能同时选择
     * @param maxCount 设置最大选择图片数目(默认为1，单选)
     * @param imagePaths 保存上一次选择图片的状态，如果不需要可以忽略
     * @param requestCode 请求回调request值，用于onActicityResult页面回调接收数据
     */
    public void selectImageAndVideoPicker(@NonNull String title,@NonNull boolean showCamera,@NonNull boolean showImage,@NonNull boolean showVideo,@NonNull boolean singleType,@NonNull int maxCount,@NonNull ArrayList<String> imagePaths,@NonNull int requestCode){
        ImagePicker.getInstance()
                .setTitle(title)
                .showCamera(showCamera)
                .showImage(showImage)
                .showVideo(showVideo)
                .setSingleType(singleType)
                .setMaxCount(maxCount)
                .setImagePaths(imagePaths)
                .setImageLoader(new GlideLoader())
                .start(mContext, requestCode);
    }

    /**
     * @param imgUrl 图片地址
     */
    public void savePhoto(String imgUrl){
        returnBitMap(imgUrl);
    }

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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Uri uri = ImageSaveUtil.saveAlbum(mContext, bitmap, Bitmap.CompressFormat.PNG, 100, true);
                    if (null != uri) {
                        Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    for(AddressDataBean.Cityjson aa:addressDataBean.getCityjson()){
                        address1Items.add(aa.getLabel());

                        ArrayList<String> cityList = new ArrayList<>();

                        ArrayList<ArrayList<String>> areaDataList = new ArrayList<>();
                        for(AddressDataBean.Children bb:aa.getChildren()){
                            cityList.add(bb.getLabel());

                            ArrayList<String> areaList = new ArrayList<>();
                            for(AddressDataBean.Childrens cc:bb.getChildren()){
                                areaList.add(cc.getLabel());
                            }
                            areaDataList.add(areaList);
                        }
                        address2Items.add(cityList);
                        address3Items.add(areaDataList);
                    }
                    break;
            }
        }
    };
}
