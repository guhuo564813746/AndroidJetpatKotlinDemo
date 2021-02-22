package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

public interface EpGameListener extends GameManagerListener{
    public void onEpGameOver(JSONObject msg);
    void onEpEvent(JSONObject msg);
}