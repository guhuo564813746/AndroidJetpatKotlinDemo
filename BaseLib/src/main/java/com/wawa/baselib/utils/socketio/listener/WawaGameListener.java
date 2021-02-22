package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

public interface WawaGameListener extends GameManagerListener{
    /**
     * 游戏结果
     * @param data 业务参数
     */
    public void onGameResult(JSONObject data);

}