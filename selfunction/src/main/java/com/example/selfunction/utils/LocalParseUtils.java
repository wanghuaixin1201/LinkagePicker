package com.example.selfunction.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.selfunction.R;
import com.example.selfunction.bean.*;
import com.example.selfunction.listener.*;

import java.util.ArrayList;
import java.util.List;

//初代版本工具类（已废弃）
public class LocalParseUtils {
    public static LocalParseUtils sInstance;
    public static OptionsPickerView oneOptions;
    public static OptionsPickerView twoOptions;
    public static OptionsPickerView ThreeOptions;
    private OptionsPickerView pvNoLinkOptions;

    private List<String> singleDataBeanList = new ArrayList<>();
    private ArrayList<String> twoSelOneData = new ArrayList<>();
    private ArrayList<ArrayList<SelectBean>> twoSelTwoData = new ArrayList<>();
    private ArrayList<ArrayList<String>> twoSelThreeData = new ArrayList<>();
    private AddressDataBean addressDataBean;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private GoodCateBean cateBean;
    private ArrayList<String> numData = new ArrayList<>();
    private ArrayList<String> letterData = new ArrayList<>();
    private ArrayList<String> englishData = new ArrayList<>();

    public LocalParseUtils(Context context) {

    }

    public static LocalParseUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LocalParseUtils.class) {
                if (sInstance == null) {
                    sInstance = new LocalParseUtils(context);
                }
            }
        }
        return sInstance;
    }

    //一级联动选择
    public void showOneDiaLog(Context context,OnOneCallBackListener listener){
        List<OneBean> list = new ArrayList<>();
        for(int i =1;i<6;i++){
            OneBean oneBean = new OneBean();
            oneBean.setId(i);
            oneBean.setName("第"+i+"个");
            list.add(oneBean);
        }

        List<String> data = new ArrayList<>();
        for (OneBean aa:list){
            data.add(aa.getName());
        }

        oneOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String tx = data.get(options1);

                if(null != list){
                    for(OneBean aa:list){
                        if(data.get(options1).equals(aa.getName())){
                            String pid = aa.getId()+""; //一级分类id
                            Log.e("分类ID",pid);
                        }
                    }
                }

                listener.onOne(tx);
            }
        })
                .setTitleText("一级类目选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(context.getResources().getColor(R.color.white))
                .setTitleColor(context.getResources().getColor(R.color.black))
                .setCancelColor(context.getResources().getColor(R.color.hint))
                .setSubmitColor(context.getResources().getColor(R.color.blue))
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(context.getResources().getColor(R.color.popup)) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddGoodActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

//        oneOptions.setSelectOptions(0,0);
        oneOptions.setPicker(data);//一级选择器
        oneOptions.show();
    }

    /**
     * 二级联动选择
     *
     * @param listener
     */
    public void showTwoDiaLog(Context context, OnTwoCallBackListener listener) {
        cateBean = com.alibaba.fastjson.JSON.parseObject(CateData.cateJson, GoodCateBean.class);

        List<GoodCateBean.Data> data = cateBean.getData();
        if (null != data) {
            singleDataBeanList = new ArrayList<>();
            for (GoodCateBean.Data aa : data) {
                ArrayList<String> names = new ArrayList<>();
                singleDataBeanList.add(aa.getName());
                twoSelOneData.add(aa.getName());
                for (GoodCateBean.Children bb : aa.getChildren()) {
                    ArrayList<SelectBean> options2Items_01 = new ArrayList<>();
                    SelectBean selectBean = new SelectBean();
                    selectBean.setId(bb.getId()+"");
                    selectBean.setPid(bb.getPid()+"");
                    selectBean.setName(bb.getName()+"");
                    options2Items_01.add(selectBean);
                    twoSelTwoData.add(options2Items_01);

                    names.add(bb.getName());
                }
                twoSelThreeData.add(names);
            }
            showCate(context,listener);
        }
    }

    private void showCate(Context context,OnTwoCallBackListener listener) {

        twoOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是两个级别的选中位置
                String tx = twoSelOneData.get(options1)+twoSelThreeData.get(options1).get(options2);
                List<GoodCateBean.Data> data = cateBean.getData();
                if(null != data){
                    for(GoodCateBean.Data aa:data){
                        String name = aa.getName();
                        int id1 = aa.getId();
                        List<GoodCateBean.Children> children = aa.getChildren();
                        for(GoodCateBean.Children bb:children){
                            String name1 = bb.getName();
                            int id2 = bb.getId();
                            if(twoSelOneData.get(options1).equals(name) && twoSelThreeData.get(options1).get(options2).equals(name1)){
                                String pid = id1+""; //一级分类id
                                String cateid = id2+""; //二级分类id
                                Log.e("分类ID",pid+"--"+cateid);
                            }
                        }
                    }
                }

                listener.onTwo(twoSelOneData.get(options1),twoSelThreeData.get(options1).get(options2));
            }
        })
                .setTitleText("二级类目选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(context.getResources().getColor(R.color.white))
                .setTitleColor(context.getResources().getColor(R.color.black))
                .setCancelColor(context.getResources().getColor(R.color.hint))
                .setSubmitColor(context.getResources().getColor(R.color.blue))
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(context.getResources().getColor(R.color.popup)) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2;
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

//        twoOptions.setSelectOptions(0,0);
        twoOptions.setPicker(twoSelOneData, twoSelThreeData);//二级选择器
        twoOptions.show();
    }

    //初始化数据
    public void initAddressData(Context context) {
        String JsonData = new GetJsonDataUtil().getJson(context, "province.json");//获取assets目录下的json文件数据

        addressDataBean = JSONObject.parseObject(JsonData, AddressDataBean.class);
        Message msg = handler.obtainMessage(2);
        handler.sendMessage(msg);
    }

    //三级联动选择
    public void showAddress(Context context,OnAddressCallBackListener listener) {
        ThreeOptions = new com.bigkoo.pickerview.builder.OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)+ options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3);
                String provinceId = "";//省ID
                String cityId = "";//市ID
                String areaId = "";//区县ID
                for(AddressDataBean.Cityjson aa:addressDataBean.getCityjson()){
                    for(AddressDataBean.Children bb:aa.getChildren()){
                        for(AddressDataBean.Childrens cc:bb.getChildren()){
                            if(options1Items.get(options1).equals(aa.getLabel()) && options2Items.get(options1).get(options2).equals(bb.getLabel()) && options3Items.get(options1).get(options2).get(options3).equals(cc.getLabel())){
                                provinceId = aa.getValue();//省ID
                                cityId = bb.getValue();//市ID
                                areaId = cc.getValue();//区县ID
                            }
                        }
                    }
                }
                listener.onAddress(options1Items.get(options1),provinceId,options2Items.get(options1).get(options2),cityId,options3Items.get(options1).get(options2).get(options3),areaId);
            }
        })
                .setTitleText("三级地址选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0,0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(context.getResources().getColor(R.color.white))
                .setTitleColor(context.getResources().getColor(R.color.black))
                .setCancelColor(context.getResources().getColor(R.color.hint))
                .setSubmitColor(context.getResources().getColor(R.color.blue))
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(context.getResources().getColor(R.color.popup)) //设置外部遮罩颜色
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

//        pvOptions.setSelectOptions(0,0);
        ThreeOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器
        ThreeOptions.show();
    }

    // 不联动的多级选项
    public void showNoLinkOptionsPicker(Context context, OnNoLinkCallBackListener listener) {
        numData.add("1");
        numData.add("2");
        numData.add("3");

        letterData.add("A");
        letterData.add("B");
        letterData.add("C");

        englishData.add("one");
        englishData.add("two");
        englishData.add("three");
        englishData.add("four");
        englishData.add("five");
        englishData.add("six");
        pvNoLinkOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String str = "numData:" + numData.get(options1)
                        + "\nletterData:" + letterData.get(options2)
                        + "\nenglishData:" + englishData.get(options3);

//                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

                listener.onData(numData.get(options1),letterData.get(options2),englishData.get(options3));
            }
        }).setTitleText("非联动选择")
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .setItemVisibleCount(5)
//                 .setSelectOptions(0, 0, 0)
                .build();
        pvNoLinkOptions.setNPicker(numData, letterData, englishData);

        pvNoLinkOptions.show();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    for(AddressDataBean.Cityjson aa:addressDataBean.getCityjson()){
                        options1Items.add(aa.getLabel());

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
                        options2Items.add(cityList);
                        options3Items.add(areaDataList);
                    }
                    break;
            }
        }
    };

}
