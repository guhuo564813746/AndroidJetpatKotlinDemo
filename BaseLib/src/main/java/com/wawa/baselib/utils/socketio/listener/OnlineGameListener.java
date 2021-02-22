package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

public interface OnlineGameListener extends GameManagerListener{
    public void onOnlineRoomPlayerChanged(JSONObject data);
    public void onOnlineRoomUserChanged(JSONObject data);
}