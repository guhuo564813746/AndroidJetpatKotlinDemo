package com.wawa.baselib.utils.socketio.listener;

import org.json.JSONObject;

/**
 * 作者：create by 张金 on 2021/2/4 10:13
 * 邮箱：564813746@qq.com
 */
public interface GameManagerListener {
    /**
     * 连接成功
     */
    public void onConnect();

    /**
     * 连接失败
     * @param t 异常
     */
    public void onConnectError(Throwable t) ;

    /**
     * 连接断开
     * @param reason 原因
     */
    public void onDisconnect(JSONObject reason);

    /**
     * 断开连接
     */
    public void websocketClosed();

    /**
     * 收到群聊消息
     * @param data 消息
     */
    public void onIMNotify(JSONObject jsondata);
    public void onMarqueeMsgNotify(JSONObject jsondata);

    /**
     * 房间用户数变更
     * @param data 房间用户信息
     */
    public void onRoomUserAmountChanged(JSONObject jsondata);

    /**
     * 游戏开始通知
     * @param data 房间用户信息
     */
    public void onGameStart(JSONObject jsondata);

    /**
     * 游戏开始通知
     * @param data 房间用户信息
     */
    public void onGameOver(JSONObject jsondata);

    /**
     * 游戏重连
     * @param data 业务参数
     */
    public void onGameReconnect(JSONObject jsondata);

    /**
     * 推流状态变更
     * @param data
     */
    public void onLiveStreamChanged(JSONObject jsondata);

    /**
     * 房间预约排队状态
     * @param isPublic 全局消息
     * @param queueNo 排队总人数
     * @param position 当前用户队伍位置
     */
    public void onRoomQueueStatus(boolean isPublic, int queueNo, int position);

    /**
     * 被踢出房间
     */
    public void onRoomKickOff();

    /**
     * 游戏准备就绪
     * @param timeLeft 等会投币剩余时间
     */
    public void onGameReady(int timeLeft);

    /**
     * 游戏倒计时
     * @param timeLeft 游戏剩余时间
     */
    public void onGameCountdown(JSONObject jsondata);

    /**
     * 被踢出排队队列
     */
    public void onRoomQueueKickOff();

    /**
     * 加入房间成功
     */
    public void joinRoomSuccess();

    /**
     * 进入房间失败
     * @param errcode 错误码
     * @param errmsg 错误消息
     */
    public void joinRoomError(int errcode, String errmsg);

    /*
     *锁机开始
     * */
    public void onGameLockStart(JSONObject jsondata);

    /*
     * 锁机结束
     * */
    public void onGameLockEnd(JSONObject jsondata);

    /*
     * 锁机倒计时
     * */
    public void onGameLockCountDowned(JSONObject jsondata);

    /*
     * 包机开始
     * */
    public void onOwnGameStart(JSONObject jsondata);

    /*
     * 包机结束
     * */
    public void onOwnGameOver(JSONObject jsondata);
}
