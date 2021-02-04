package com.wawa.baselib.utils.socketio;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wawa.baselib.utils.socketio.listener.GameManagerListener;

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
    private Socket socket;
    private String wsURL;
    private static final String TAG="GameSocketManager";
    private GameManagerListener gameManagerListener=null;
    private ConcurrentMap<String, Callback> callbacks = new ConcurrentHashMap<String, Callback>();
    public void setGameManagerListener(GameManagerListener listener){
        this.gameManagerListener=listener;
    }
    private GameSocketManager() {

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
                Log.d(TAG,"onSocket_EVENT_CONNECT--"+args.length+args[0].toString());
                gameManagerListener.onConnect();
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_DISCONNECT--"+args.length+args[0].toString());
                String reason = (String) args[0];
                if (gameManagerListener != null) {
                    gameManagerListener.onDisconnect(reason);
                }
            }
        }).on(Socket.EVENT_CONNECT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_CONNECT_ERROR--"+args.length+args[0].toString());
                Throwable t = (Exception) args[0];
                if (gameManagerListener != null){
                    gameManagerListener.onConnectError(t);
                }

            }
        }).on(Socket.EVENT_ERROR,new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_ERROR--"+args.length+args[0].toString());
                Throwable t = (Exception) args[0];
                if (gameManagerListener != null){
                    gameManagerListener.onConnectError(t);
                }
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocket_EVENT_RECONNECT--"+args.length+args[0].toString());
            }
        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"onSocketEVENT_MESSAGE"+args.length);
                for (Object object : args){
                    Log.d(TAG,"onSocketEVENT_MESSAGE"+object.toString());
                    try {
                        JSONObject response=new JSONObject(object.toString());
                        String method = response.getString("method");
                        String msgId = response.getString("id");
                        int errorCode=response.getInt("errcode");
                        String errorMsg=response.getString("errmsg");
                        String data = response.getJSONObject("params").toString();
                        if (!TextUtils.isEmpty(method)){
                            Log.d(TAG,"onSocketEVENT_MESSAGE--method"+method);
                            switch (method){
                                case "":

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

    public void sendMessage(String jsonMsg,Callback cb){
        Log.d(TAG,"sendMessage--"+jsonMsg);
        if (socket == null || !socket.connected()){
            return;
        }
        JSONObject json=null;
        String msgId=null;
        try {
            json=new JSONObject(jsonMsg);
            if (json != null){
                msgId=json.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        addCallback(msgId,cb);
        try {
            socket.emit(Socket.EVENT_MESSAGE,json);
        }catch (Exception e){

        }
    }

    public static interface Callback{
        void onSuccess(String jsonStr);
        void onError(int errorCode,String errorMsg);
    }
}
