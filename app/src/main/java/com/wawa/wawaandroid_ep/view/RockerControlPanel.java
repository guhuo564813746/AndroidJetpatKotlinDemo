package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.util.AttributeSet;

import com.robotwar.app.R;

/**
 * @ProjectName: wawa-android
 * @Package: com.coinhouse777.wawa.custom.buyu
 * @ClassName: BuyuControlPanel
 * @Description:
 * @Author: Juice
 * @CreateDate: 2019/4/30 11:08
 * @UpdateUser: Juice
 * @UpdateDate: 2019/4/30 11:08
 * @UpdateRemark: 说明
 * @Version: 1.0
 */
public class RockerControlPanel extends RoomControlPanel {
    RockerView mBtnRocker;
    public RockerControlPanel(Context context){
        super(context);
    }

    public RockerControlPanel(Context context,AttributeSet attrset){
        super(context,attrset);
    }


    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        mBtnRocker=findViewById(R.id.btn_rocker);
        mBtnRocker.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_MOVE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_rocker_control_panel;
    }
    public RockerControlPanel setListener(RockerView.OnShakeListener listener) {
        mBtnRocker.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, listener);
        return this;
    }
}
