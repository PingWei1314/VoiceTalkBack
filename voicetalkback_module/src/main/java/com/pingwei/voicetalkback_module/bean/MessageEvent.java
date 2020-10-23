package com.pingwei.voicetalkback_module.bean;

public class MessageEvent {

    private String message;
    private int type;

    public MessageEvent(int type, String message) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
