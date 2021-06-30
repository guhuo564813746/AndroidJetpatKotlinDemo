package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

/**
 * 作者：create by 张金 on 2021/6/29 10:02
 * 邮箱：564813746@qq.com
 */
public interface FishGameListener extends GameManagerListener{
    void onFishPrize(JSONObject msg);
    void onHook(JSONObject msg);
}
