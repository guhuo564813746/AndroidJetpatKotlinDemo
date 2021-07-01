package com.wawa.wawaandroid_ep.view;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
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

    public int getLineMaxNumber(String text, TextPaint paint, int maxWidth) {
        if (null == text || "".equals(text)) {
            return 0;
        }
        StaticLayout staticLayout = new StaticLayout(text, paint, maxWidth, Layout.Alignment.ALIGN_NORMAL
                , 1.0f, 0, false);
        //获取第一行最后显示的字符下标
        return staticLayout.getLineEnd(0);
    }

}