package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;


/**
 * @ProjectName: wawa-android
 * @Package: com.coinhouse777.wawa.custom.buyu
 * @ClassName: RoomControlPanel
 * @Description:
 * @Author: Juice
 * @CreateDate: 2019/4/30 11:00
 * @UpdateUser: Juice
 * @UpdateDate: 2019/4/30 11:00
 * @UpdateRemark: 说明
 * @Version: 1.0
 */
public abstract class RoomControlPanel extends RelativeLayout {
    public RoomControlPanel(Context context){
        super(context);
        init(context,null);
    }

    public RoomControlPanel(Context context, AttributeSet attrs){
        super(context,attrs);
        init(context,attrs);
    }

    public RoomControlPanel(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }

    protected void init(Context context,AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(getLayoutId(), this, true);
        //View view = inflate(context, getLayoutId(),this);
        setOnTouchListener(null);
        setOnClickListener(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    protected abstract int getLayoutId();

}
