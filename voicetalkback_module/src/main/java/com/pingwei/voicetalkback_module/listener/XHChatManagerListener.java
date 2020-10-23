package com.pingwei.voicetalkback_module.listener;

import com.anhangda.lamp.v3.bean.CoreDB;
import com.anhangda.lamp.v3.bean.HistoryBean;
import com.anhangda.lamp.v3.bean.MessageBean;
import com.anhangda.lamp.v3.rtc.AEvent;
import com.anhangda.lamp.v3.rtc.MLOC;
import com.starrtc.starrtcsdk.apiInterface.IXHChatManagerListener;
import com.starrtc.starrtcsdk.core.im.message.XHIMMessage;

import java.text.SimpleDateFormat;

public class XHChatManagerListener implements IXHChatManagerListener {
    @Override
    public void onReceivedMessage(XHIMMessage message) {

        HistoryBean historyBean = new HistoryBean();
        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        historyBean.setLastMsg(message.contentData);
        historyBean.setConversationId(message.fromId);
        historyBean.setNewMsgCount(1);
        MLOC.addHistory(historyBean,false);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(message.fromId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(message.contentData);
        messageBean.setFromId(message.fromId);
        MLOC.saveMessage(messageBean);

        AEvent.notifyListener(AEvent.AEVENT_C2C_REV_MSG,true,message);

    }

    @Override
    public void onReceivedSystemMessage(XHIMMessage message) {
        HistoryBean historyBean = new HistoryBean();
        historyBean.setType(CoreDB.HISTORY_TYPE_C2C);
        historyBean.setLastTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        historyBean.setLastMsg(message.contentData);
        historyBean.setConversationId(message.fromId);
        historyBean.setNewMsgCount(1);
        MLOC.addHistory(historyBean,false);

        MessageBean messageBean = new MessageBean();
        messageBean.setConversationId(message.fromId);
        messageBean.setTime(new SimpleDateFormat("MM-dd HH:mm").format(new java.util.Date()));
        messageBean.setMsg(message.contentData);
        messageBean.setFromId(message.fromId);
        MLOC.saveMessage(messageBean);

        AEvent.notifyListener(AEvent.AEVENT_REV_SYSTEM_MSG,true,message);
    }
}
