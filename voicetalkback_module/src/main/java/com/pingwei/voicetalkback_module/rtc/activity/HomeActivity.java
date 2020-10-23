package com.pingwei.voicetalkback_module.rtc.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.pingwei.export_voicetalkback.router.VoiceTalkRouterTable;
import com.pingwei.voicetalkback_module.KeepLiveService;
import com.pingwei.voicetalkback_module.R;
import com.pingwei.voicetalkback_module.bean.MessageEvent;
import com.pingwei.voicetalkback_module.bean.SuperRoomInfo;
import com.pingwei.voicetalkback_module.rtc.MLOC;
import com.pingwei.voicetalkback_module.serverAPI.InterfaceUrls;
import com.pingwei.voicetalkback_module.utils.Constant;
import com.starrtc.starrtcsdk.api.XHClient;
import com.starrtc.starrtcsdk.api.XHConstants;
import com.starrtc.starrtcsdk.apiInterface.IXHResultCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
@Route(path = VoiceTalkRouterTable.PATH_PAGE_CART)
public class HomeActivity extends AppCompatActivity {
    private ArrayList<SuperRoomInfo> mDatas=new ArrayList<>();
    private String userid;
    private int requestCode;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);
        userid = getIntent().getStringExtra("userid");
        url=getIntent().getStringExtra("url");
        requestCode=getIntent().getIntExtra("requestCode",-1);
        MLOC.setHostUrl(url);
        Intent intent = new Intent(HomeActivity.this, KeepLiveService.class);
        if (userid!=null) {
            intent.putExtra("userid",userid);
        }else {
            userid=new Date().getTime()+"";
            intent.putExtra("userid",userid);
        }
        startService(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getType()== Constant.LOGIN_SUCCESS) {
            queryAllList();
        }
    }

    private void queryAllList(){
        if(MLOC.AEventCenterEnable){
            InterfaceUrls.demoQueryList(MLOC.LIST_TYPE_SUPER_ROOM_ALL);
        }else{
            XHClient.getInstance().getSuperRoomManager().queryList("",MLOC.LIST_TYPE_SUPER_ROOM_ALL,new IXHResultCallback() {
                @Override
                public void success(final Object data) {
                    String[] res = (String[]) data;
                    JSONArray array = new JSONArray();
                    for (int i=0;i<res.length;i++){
                        String info = res[i];
                        try {
                            info = URLDecoder.decode(info,"utf-8");
                            JSONObject jsonObject = new JSONObject(info);
                            array.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    mDatas.clear();
                    int pos=-1;
                    try {
//                    JSONArray array = (JSONArray) data;
                        for(int i = 0;i<array.length();i++){
                            SuperRoomInfo info = new SuperRoomInfo();
                            JSONObject obj = array.getJSONObject(i);
                            info.creator = obj.getString("creator");
                            info.id = obj.getString("id");
                            info.name = obj.getString("name");
                            mDatas.add(info);
                            if (info.name.equals(userid)) {
                                Constant.isExist=true;
                                pos=i;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Constant.isExist) {
                        Intent intent = new Intent(HomeActivity.this, SuperRoomActivity.class);
                        intent.putExtra(SuperRoomActivity.LIVE_NAME,mDatas.get(pos).name);
                        intent.putExtra(SuperRoomActivity.CREATER_ID,mDatas.get(pos).creator);
                        intent.putExtra(SuperRoomActivity.LIVE_ID,mDatas.get(pos).id);
                        startActivityForResult(intent,Constant.REQUEST_CODE);
                    }else {
                        XHConstants.XHSuperRoomType type = XHConstants.XHSuperRoomType.XHSuperRoomTypeGlobalPublic;
                        Intent intent = new Intent(HomeActivity.this, SuperRoomActivity.class);
                        intent.putExtra(SuperRoomActivity.LIVE_TYPE,type);
                        intent.putExtra(SuperRoomActivity.LIVE_NAME,userid);
                        intent.putExtra(SuperRoomActivity.CREATER_ID,MLOC.userId);
                        startActivityForResult(intent,Constant.REQUEST_CODE);
                    }
                }

                @Override
                public void failed(String errMsg) {
                    MLOC.d("VideoMettingListActivity",errMsg);
                    mDatas.clear();

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== Constant.REQUEST_CODE&&resultCode== Activity.RESULT_OK) {
            setResult(requestCode);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}