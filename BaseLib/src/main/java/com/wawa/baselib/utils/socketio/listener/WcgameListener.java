package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

public interface WcgameListener {
    public void onWcgameStatus(JSONObject response);
    public void onWcGameStart(JSONObject response);
    public void onWcGameRank(JSONObject response);
    public void onWcGameResult(JSONObject response);
    public void onWcGameInfo(JSONObject response);
    public void onWcGameCountDown(JSONObject response);
    public void onWcGameOver(JSONObject response);
    void onWcGameClearing(JSONObject response);
}