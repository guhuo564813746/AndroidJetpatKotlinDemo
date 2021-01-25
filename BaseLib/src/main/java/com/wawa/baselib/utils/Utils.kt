package com.wawa.baselib.utils

import android.content.Context
import android.content.SharedPreferences

/**
 *作者：create by 张金 on 2021/1/6 11:03
 *邮箱：564813746@qq.com
 */
class Utils {
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
        /* Share 保存-- end*/

    }
}
