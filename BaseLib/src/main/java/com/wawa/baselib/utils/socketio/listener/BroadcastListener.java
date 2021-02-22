package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

public interface BroadcastListener extends GameManagerListener{
    public void broadcastMsg(JSONObject msg);
}