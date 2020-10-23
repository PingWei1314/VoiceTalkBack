package com.pingwei.export_voicetalkback;

import android.app.Activity;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.pingwei.export_voicetalkback.router.VoiceTalkRouterTable;


/**
 * 购物车组件服务工具类
 * 其他组件直接使用此类即可：页面跳转、获取服务。
 * @author hufeiyang
 */
public class VoiceTalkServiceUtil {

    /**
     * 跳转到购物车页面
     * @param context
     * @param url
     * @param userid
     * @param requestCode
     */
    public static void navigateCartPage(Context context,String userid, String url,int requestCode){
        ARouter.getInstance()
                .build(VoiceTalkRouterTable.PATH_PAGE_CART)
                .withString("userid",userid)
                .withString("url",url)
                .withInt("requestCode",requestCode)
                .navigation((Activity) context,requestCode);
    }


}
