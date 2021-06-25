package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.view.View;
import android.widget.PopupWindow;

public class ViewUtils {
    private Context mContext;
    public ViewUtils(Context context){
        this.mContext=context;
    }
    public PopupWindow initPopupWindow(View view , int width, int height, int anim){
        PopupWindow popupWindow=new PopupWindow(view,width,height);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        if (anim != -1){
            popupWindow.setAnimationStyle(anim);
        }
        return popupWindow;
    }

}