package com.pingwei.voicetalkback_module.interfaces;

public interface IEventListener {
    abstract public void dispatchEvent(String aEventID, boolean success, Object eventObj);
}
