package com.wawa.wawaandroid_ep.view.popgame;

import android.view.View;

/**
 * 作者：create by 张金 on 2021/4/26 17:45
 * 邮箱：564813746@qq.com
 */
public class PopGameItemBean {
    public static final int GAME_ID_CHARGE=1;
    public static final int GAME_ID_AUTO_IC=2;
    public static final int GAME_ID_OWN=3;
    public static final int GAME_ID_OWNING=4;
    public static final int GAME_ID_LOCK=5;
    public static final int GAME_ID_DS=6;
    public static final int GAME_ID_SETTING=7;
    public static final int GAME_ID_INFO=8;
    public static final int GAME_ID_KF=9;
    public static final int GAME_ID_FEEDBACK=10;
    public static final int GAME_ID_CHAT=11;
    public int gameSetId;
    public int imgRes;
    public String title;
    public boolean enableTab=false;
    public int visible= View.VISIBLE;
}
