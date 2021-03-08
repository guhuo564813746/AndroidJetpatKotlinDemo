package com.wawa.wawaandroid_ep.utils.stringutils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.wawa.wawaandroid_ep.R;
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean;

public class TextRender {

    private static ForegroundColorSpan sColorSpan1;
    private static ForegroundColorSpan sColorSpan2;
    private static ForegroundColorSpan sColorSpan3;

    static {
        sColorSpan1 = new ForegroundColorSpan(0xfffd8678);//系统消息头红色
        sColorSpan2 = new ForegroundColorSpan(0xffffff00);//系统消息体黄色
        sColorSpan3 = new ForegroundColorSpan(0xff00ffff);//用户名颜色
    }


    /**
     * 渲染消息体
     *
     * @param bean
     * @return
     */
    public static SpannableStringBuilder createChat(Context context, GameRoomChatDataBean.MsgListBean bean) {
        int type = bean.getType();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (type == GameRoomChatDataBean.CONTENT_TEXT) {
            String res = context.getString(R.string.live_room_message) + "：" + bean.getBody().getText();
            builder.append(res);
            int index = res.indexOf("：") + 1;
            builder.setSpan(sColorSpan1, 0, index, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(sColorSpan2, index, res.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
//            String res = bean.get() + "：" + bean.getContent();
//            builder.append(res);
//            int index = res.indexOf("：") + 1;
//            builder.setSpan(sColorSpan3, 0, index, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return builder;
    }


    /*public static SpannableStringBuilder createEnterRoom(UserBean bean) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String res = bean.getUser_nicename() + "：" + getString(R.string.enter_room);
        builder.append(res);
        int index = res.indexOf("：") + 1;
        builder.setSpan(sColorSpan2, 0, index, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }*/


    /**
     * 发弹幕
     */
    /*public static SpannableStringBuilder createDanmu(DanMuBean bean) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String content = bean.getContent();
        int action = bean.getAction();
        Drawable drawable = null;
        if (action == 1) {//成功
            drawable = ContextCompat.getDrawable(App.getInstance(), R.mipmap.icon_grab_success);
        } else if (action == 0) {//失败
            drawable = ContextCompat.getDrawable(App.getInstance(), R.mipmap.icon_grab_failed);
        } else if (action == 2) {//上机
            drawable = ContextCompat.getDrawable(App.getInstance(), R.mipmap.icon_grab_shangji);
        } else if (action == 3) {//下机
            drawable = ContextCompat.getDrawable(App.getInstance(), R.mipmap.icon_grab_xiaji);
        }
        drawable.setBounds(0, 0, DpUtil.dp2px(18), DpUtil.dp2px(18));
        int length = content.length();
        content += "    ";
        builder.append(content);
        if (action == 1) {
            builder.setSpan(sColorSpan2, 0, length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        builder.setSpan(new VerticalImageSpan(drawable), length, length + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new VerticalImageSpan(drawable), length + 1, length + 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new VerticalImageSpan(drawable), length + 2, length + 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        builder.setSpan(new VerticalImageSpan(drawable), length + 3, length + 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return builder;
    }*/


}
