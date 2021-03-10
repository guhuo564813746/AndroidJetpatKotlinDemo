package com.wawa.baselib.utils

import android.content.Context
import android.content.SharedPreferences

/**
 *作者：create by 张金 on 2021/1/6 11:03
 *邮箱：564813746@qq.com
 */
class SharePreferenceUtils {
    companion object{

        @JvmStatic
        fun doAction(){
            doSomethings()
        }

        /*
        * Share保存--start
        * */
        private val USER_DATA: String="USER_DATA"


        val TOKEN: String="TOKEN"
        val UID: String ="UID"
        const val BGM="bgm"//背景音乐
        const val BGM_LIVE = "live_bgm"//直播背景音乐
        const val BGM_GAME = "bgm_game"//游戏音效
        const val BGM_KEY = "bgm_key"//按键音效
        val PLAYER_OPENGL = "PLAYER_OPENGL"
        val VIDEO_PLAYER = "VIDEO_PLAYER"
        val WIFI_SET="WIFI_SET"
        val LOCALE_LAN="LOCALE_LAN"
        public lateinit var  sPUser: SharedPreferences
        public fun initSp(context: Context){
            sPUser=context?.getSharedPreferences(USER_DATA,Context.MODE_PRIVATE) ?: return
        }

        fun saveToken(token: String){
            with(sPUser.edit()){
                putString(TOKEN,token)
                apply()
            }
        }

        fun readToken(): String?{
            return sPUser.getString(TOKEN,"")
        }

        fun saveUid(uid: String){
            with(sPUser.edit()){
                putString(UID,uid)
                apply()
            }
        }
        fun readUid(): String?{
            return sPUser.getString(UID,"")
        }

        fun saveSwitch(tag:String,switch: Boolean){
            with(sPUser.edit()){
                putBoolean(tag,switch)
                apply()
            }
        }

        fun getSwitch(tag: String): Boolean{
            return sPUser.getBoolean(tag,false)
        }

        fun saveStr(tag: String,content: String?){
            with(sPUser.edit()){
                putString(tag,content)
                apply()
            }
        }

        fun getStr(tag: String): String?{
            return sPUser.getString(tag,(if(tag.equals(LOCALE_LAN)) LanguageUtils.getCurLanguage() else ""))
        }

        /* Share 保存-- end*/

    }
}
