package com.pingwei.voicetalkback_module.interfaces;

public interface ICallback {
    abstract  void callback(boolean reqSuccess, String statusCode, String data);
}
