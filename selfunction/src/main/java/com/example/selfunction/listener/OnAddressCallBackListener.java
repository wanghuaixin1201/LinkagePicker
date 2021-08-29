package com.example.selfunction.listener;


/**
 * @ProjectName: Caocao
 * @Package: com.caocao.client.ui.me.address
 * @ClassName: OnAddressCallBackListener
 * @Description: java类作用描述
 * @Author: XuYu
 * @CreateDate: 2020/8/12 16:57
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/8/12 16:57
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface OnAddressCallBackListener {
    void onAddress(String province,String provinceId, String city,String cityId, String area,String areaId);
}
