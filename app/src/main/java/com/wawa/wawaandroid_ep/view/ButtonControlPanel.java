package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
public class ButtonControlPanel extends RoomControlPanel {
    protected OnTouchListener listener;
    View mBtnBg;
    View mBtnUp;
    Button mBtnDown;
    Button mBtnLeft;
    Button mBtnRight;
    protected ButtonListener mOnTouchListener;

    public ButtonControlPanel(Context context){
        super(context);
    }

    public ButtonControlPanel(Context context,AttributeSet attrset){
        super(context,attrset);
    }

    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        mBtnUp=findViewById(R.id.btn_up);
        mBtnDown=findViewById(R.id.btn_down);
        mBtnBg=findViewById(R.id.btn_bg);
        mBtnLeft=findViewById(R.id.btn_left);
        mBtnRight=findViewById(R.id.btn_right);
        mOnTouchListener = new ButtonListener();
        mBtnUp.setOnTouchListener(mOnTouchListener);
        mBtnDown.setOnTouchListener(mOnTouchListener);
        mBtnLeft.setOnTouchListener(mOnTouchListener);
        mBtnRight.setOnTouchListener(mOnTouchListener);
        mBtnUp.setAlpha(0);
        mBtnDown.setAlpha(0);
        mBtnLeft.setAlpha(0);
        mBtnRight.setAlpha(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOnTouchListener = null;
        this.listener = null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_button_control_panel;
    }
    public ButtonControlPanel setListener(OnTouchListener listener) {
        this.listener = listener;
        return this;
    }
    public interface OnTouchListener {
        boolean onTouch(View var1, RockerView.Direction direction, MotionEvent var2);
    }
    public final class ButtonListener implements View.OnClickListener, View.OnTouchListener {
        @Override
        public void onClick(View v) {
            Log.e("ButtonControlPanel", "button ---> click");
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    switch (v.getId()) {
                        case R.id.btn_left:
                            mBtnBg.setBackgroundResource(R.mipmap.icon_direction_z);
                            break;
                        case R.id.btn_right:
                            mBtnBg.setBackgroundResource(R.mipmap.icon_direction_y);
                            break;
                        case R.id.btn_up:
                            mBtnBg.setBackgroundResource(R.mipmap.icon_direction_s);
                            break;
                        case R.id.btn_down:
                            mBtnBg.setBackgroundResource(R.mipmap.icon_direction_x);
                            break;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    mBtnBg.setBackgroundResource(R.mipmap.icon_direction_normal);
                    break;
                }
            }
            switch (v.getId()) {
                case R.id.btn_left:
                    if (listener != null) {
                        return listener.onTouch(v, RockerView.Direction.DIRECTION_LEFT, e);
                    }
                    break;
                case R.id.btn_right:
                    if (listener != null) {
                        return listener.onTouch(v, RockerView.Direction.DIRECTION_RIGHT, e);
                    }
                    break;
                case R.id.btn_up:
                    if (listener != null) {
                        return listener.onTouch(v, RockerView.Direction.DIRECTION_UP, e);
                    }
                    break;
                case R.id.btn_down:
                    if (listener != null) {
                        return listener.onTouch(v, RockerView.Direction.DIRECTION_DOWN, e);
                    }
                    break;
            }
            return true;
        }
    }
}
