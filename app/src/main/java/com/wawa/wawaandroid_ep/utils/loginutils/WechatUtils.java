package com.wawa.wawaandroid_ep.utils.loginutils;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wawa.wawaandroid_ep.BuildConfig;
import com.wawa.wawaandroid_ep.R;

/**
 * 作者：create by 张金 on 2020/12/30 18:07
 * 邮箱：564813746@qq.com
 */
public class WechatUtils {
    private Context mContext;
    private IWXAPI mApi;
    private String appId;
    public WechatUtils(Context context){
        this.mContext=context;
        appId= BuildConfig.WX_APPID;
        mApi = WXAPIFactory.createWXAPI(mContext, appId);
        mApi.registerApp(appId);
    }

    public void wxLogin(){
        // send oauth request
        if (!mApi.isWXAppInstalled()){
            Toast.makeText(mContext, mContext.getString(R.string.tx_pls_install_wx_tips), Toast.LENGTH_SHORT).show();
        }else {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            mApi.sendReq(req);
        }
    }

}
