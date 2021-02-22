package com.wawa.baselib.utils.socketio;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wawa.baselib.utils.socketio.listener.BroadcastListener;
import com.wawa.baselib.utils.socketio.listener.EpGameListener;
import com.wawa.baselib.utils.socketio.listener.GameManagerListener;
import com.wawa.baselib.utils.socketio.listener.OnlineGameListener;
import com.wawa.baselib.utils.socketio.listener.WawaGameListener;
import com.wawa.baselib.utils.socketio.listener.WcgameListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

/**
 * 作者：create by 张金 on 2021/2/4 10:04
 * 邮箱：564813746@qq.com
 */
public class GameSocketManager {
    public static int messageId=0;
    private Socket socket;
    private String wsURL;
    private static final String TAG="GameSocketManager";
    private GameManagerListener gameManagerListener=null;
    private ConcurrentMap<String, Callback> callbacks = new ConcurrentHashMap<String, Callback>();
    public GameSocketManager setGameManagerListener(GameManagerListener listener){
        this.gameManagerListener=listener;
        return this;
    }

    private GameSocketManager() {

    }
    public static int generateId(){
        if (messageId >= Integer.MAX_VALUE){
            messageId=0;
        }
        messageId++;
        return messageId;
    }

    public static synchronized GameSocketManager getInstance() {
        return SocketManagerHolder.instance;
    }

    private static class SocketManagerHolder {
        private static final GameSocketManager instance = new GameSocketManager();
    }

    public void connect(String wsURL){
        if (this.gameManagerListener == null) {
            throw new NullPointerException("gameManagerListener is null");
        }
        this.wsURL = wsURL;
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            IO.Options opts = new IO.Options();
            opts.transports = new String[]{WebSocket.NAME};
            socket = IO.socket(this.wsURL, opts);
        } catch (URISyntaxException e) {
            Log.d(TAG,"onConnectError--");
            gameManagerListener.onConnectError(e);
        }
        socket.on(Socket.EVENT_CONNECT,new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_CONNECT--");
                gameManagerListener.onConnect();
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_DISCONNECT--");
                String reason = (String) args[0];
                JSONObject json=null;
                try {
                    json=new JSONObject(reason);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (gameManagerListener != null) {
                    gameManagerListener.onDisconnect(json);
                }
            }
        }).on(Socket.EVENT_CONNECT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_CONNECT_ERROR--");
                Throwable t = (Exception) args[0];
                if (gameManagerListener != null){
                    gameManagerListener.onConnectError(t);
                }

            }
        }).on(Socket.EVENT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_ERROR--");
                Throwable t = (Exception) args[0];
                if (gameManagerListener != null){
                    gameManagerListener.onConnectError(t);
                }
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_RECONNECT--");
            }
        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocketEVENT_MESSAGE");
                for (Object object : args){
                    Log.d(TAG,"onSocketEVENT_MESSAGE"+object.toString());
                    try {
                        JSONObject response=new JSONObject(object.toString());
                        String method = response.getString("method");
                        String msgId = response.getString("id");
                        int errorCode=response.getInt("errcode");
                        String errorMsg=response.getString("errmsg");
                        JSONObject data = response.getJSONObject("params");
                        if (!TextUtils.isEmpty(method)){
                            Log.d(TAG,"onSocketEVENT_MESSAGE--method"+method);
                            switch (method){
                                case "on_msg_notify":
                                    if (gameManagerListener != null) {
                                        gameManagerListener.onIMNotify(response);
                                    }
                                    break;
                                case "on_marquee_msg_notify":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onMarqueeMsgNotify(response);
                                    }
                                    break;
                                case "on_room_user_amount_changed":
                                    if (gameManagerListener !=null){
                                        gameManagerListener.onRoomUserAmountChanged(response);
                                    }
                                    break;
                                case "on_online_room_player_changed":
                                    if (gameManagerListener != null){
                                        ((OnlineGameListener)gameManagerListener).onOnlineRoomPlayerChanged(response);
                                    }
                                    break;
                                case "on_online_room_user_changed":
                                    if (gameManagerListener != null){
                                        ((OnlineGameListener)gameManagerListener).onOnlineRoomUserChanged(response);
                                    }
                                    break;
                                case "on_game_start":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameStart(response);
                                    }
                                    break;
                                case "on_game_over":
                                    if (gameManagerListener !=null){
                                        gameManagerListener.onGameOver(response);
                                    }
                                    break;
                                case "on_live_stream_changed":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onLiveStreamChanged(response);
                                    }
                                    break;
                                case "on_game_reconnect":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameReconnect(response);
                                    }
                                    break;
                                case "room_queue_kick_off":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onRoomQueueKickOff();
                                    }
                                    break;
                                case "on_room_kick_off":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onRoomKickOff();
                                    }
                                    break;
                                case "room_queue_status":
                                    if (gameManagerListener != null){
                                        int type=data.getInt("type");
                                        int queueNo=data.getInt("queueNo");
                                        int position=data.getInt("position");
                                        gameManagerListener.onRoomQueueStatus(type==0,queueNo,position);
                                    }
                                    break;
                                case "game_ready":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameReady(data.getInt("time_left"));
                                    }
                                    break;
                                case "on_game_countdown":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameCountdown(data);
                                    }
                                    break;
                                case "game_result":
                                    if (gameManagerListener != null){
                                        ((WawaGameListener)gameManagerListener).onGameResult(data);
                                    }
                                    break;
                                case "on_lock_start":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameLockStart(data);
                                    }
                                    break;
                                case "on_lock_end":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameLockEnd(data);
                                    }
                                    break;
                                case "on_lock_countdown":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onGameLockCountDowned(data);
                                    }
                                    break;
                                case "on_broadcast_msg":
                                    if (gameManagerListener != null){
                                        ((BroadcastListener)gameManagerListener).broadcastMsg(data);
                                    }
                                    break;
                                case "djiep_on_game_result":
                                    if (gameManagerListener != null){
                                        ((EpGameListener)gameManagerListener).onEpGameOver(data);
                                    }
                                    break;
                                case "djiep_on_event":
                                    if (gameManagerListener != null){
                                        ((EpGameListener)gameManagerListener).onEpEvent(data);
                                    }
                                    break;
                                case "on_game_lease_start":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onOwnGameStart(data);
                                    }
                                    break;
                                case "on_game_lease_result":
                                    if (gameManagerListener != null){
                                        gameManagerListener.onOwnGameOver(data);
                                    }
                                    break;
                                case "on_game_wc_join_start":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcgameStatus(data);
                                    }
                                    break;
                                case "on_game_wc_start":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameStart(data);
                                    }
                                    break;
                                case "on_game_wc_rank":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameRank(data);
                                    }
                                    break;
                                case "on_game_wc_info":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameInfo(data);
                                    }
                                    break;
                                case "on_game_wc_countdown":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameCountDown(data);
                                    }
                                    break;
                                case "on_game_wc_result":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameResult(data);
                                    }
                                    break;
                                case "on_game_wc_over":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameOver(data);
                                    }
                                    break;
                                case "on_game_wc_clearing":
                                    if (gameManagerListener != null){
                                        ((WcgameListener)gameManagerListener).onWcGameClearing(data);
                                    }
                                    break;
                            }
                        }else if (!TextUtils.isEmpty(msgId)){
                            Callback callback=callbacks.get(msgId);
                            if (callback != null){
                                callbacks.remove(msgId);
                                if (errorCode==0){
                                    callback.onSuccess(data);
                                }else {
                                    callback.onError(errorCode,errorMsg);
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        });
        socket.connect();
    }

    public void disConnect(){
        Log.d(TAG,"disConnect--");
        if (socket != null){
            socket.close();
            socket=null;
        }
    }

    private void addCallback(String id,Callback cb){
        if (cb == null){
            return;
        }
        if (callbacks.get(id) != null){
            callbacks.remove(id);
        }
        this.callbacks.putIfAbsent(id,cb);
    }

    public void sendMessage(JSONObject jsonMsg,Callback cb){
        Log.d(TAG,"sendMessage--"+jsonMsg);
        if (socket == null || !socket.connected() || jsonMsg == null){
            return;
        }
        String msgId=null;
        try {
            if (jsonMsg != null){
                msgId=jsonMsg.getString("id");
                if (!TextUtils.isEmpty(msgId)){
                    addCallback(msgId,cb);
                    socket.emit(Socket.EVENT_MESSAGE,jsonMsg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static interface Callback{
        void onSuccess(JSONObject jsonStr);
        void onError(int errorCode,String errorMsg);
    }
}
