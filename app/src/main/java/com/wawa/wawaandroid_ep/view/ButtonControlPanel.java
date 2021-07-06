package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.content.res.TypedArray;
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
    private int mainBtnBg;
    private int leftBtnBg;
    private int rightBtnBg;
    private int upBtnBg;
    private int downBtnBg;
    private static final int DEFAULT=0;
    private static final int FISHING=1;
    private static final int CAMERA= 2;
    private int controlorType=DEFAULT;
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
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonControlAttr);
        controlorType=typedArray.getInteger(R.styleable.ButtonControlAttr_controlType,DEFAULT);
        switch (controlorType){
            case DEFAULT:
                mainBtnBg=R.mipmap.icon_direction_normal;
                leftBtnBg=R.mipmap.icon_direction_z;
                rightBtnBg=R.mipmap.icon_direction_y;
                upBtnBg=R.mipmap.icon_direction_s;
                downBtnBg=R.mipmap.icon_direction_x;
                break;
            case FISHING:
                mainBtnBg=R.mipmap.im_fishgame_controlor_n_bg;
                leftBtnBg=R.mipmap.im_fishgame_controlor_left;
                rightBtnBg=R.mipmap.im_fishgame_controlor_right;
                upBtnBg=R.mipmap.im_fishgame_controlor_up;
                downBtnBg=R.mipmap.im_fishgame_controlor_down;
                break;
            case CAMERA:
                mainBtnBg=R.mipmap.im_fishgame_camera_n_bg;
                leftBtnBg=R.mipmap.im_fishgame_camera_left;
                rightBtnBg=R.mipmap.im_fishgame_camera_right;
                upBtnBg=R.mipmap.im_fishgame_camera_up;
                downBtnBg=R.mipmap.im_fishgame_camera_down;
                break;
        }

        findViewById(R.id.btn_bg).setBackgroundResource(mainBtnBg);
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
                            mBtnBg.setBackgroundResource(leftBtnBg);
                            break;
                        case R.id.btn_right:
                            mBtnBg.setBackgroundResource(rightBtnBg);
                            break;
                        case R.id.btn_up:
                            mBtnBg.setBackgroundResource(upBtnBg);
                            break;
                        case R.id.btn_down:
                            mBtnBg.setBackgroundResource(downBtnBg);
                            break;
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    mBtnBg.setBackgroundResource(mainBtnBg);
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
