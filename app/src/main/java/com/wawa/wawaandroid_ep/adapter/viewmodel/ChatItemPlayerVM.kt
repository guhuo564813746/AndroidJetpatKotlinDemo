package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.util.Log
import android.view.ViewTreeObserver
import android.widget.TextView
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.view.ViewUtils

/**
 *作者：create by 张金 on 2021/7/1 14:19
 *邮箱：564813746@qq.com
 */
class ChatItemPlayerVM : ArrayListViewModel<GameRoomChatDataBean>() {
    val TAG="ChatItemPlayerVM"
    lateinit var viewUtils: ViewUtils
    init {

    }
    override fun onBindAdapter(adapter: ArrayListAdapter<GameRoomChatDataBean>) {
        viewUtils=ViewUtils(mContext)
        viewHolder?.view?.let {
            val tvMsgContent1=it.findViewById<TextView>(R.id.tv_msg_content1)
            val tvMsgContent2=it.findViewById<TextView>(R.id.tv_msg_content2)
            var tagContent1=""
            var tagContent2=""
            var line1Length=0
            tvMsgContent1.post(Runnable {
                Log.d(TAG,"line1Length--"+line1Length+"tvMsgContent1_Widrth"+tvMsgContent1.width)
                model?.msg_list?.let {
                    if (it.size > 0){
                        it.get(0).body.text?.let {
                            Log.d(TAG,"byteArrayLength--"+0)
                            line1Length=viewUtils.getLineMaxNumber(it,tvMsgContent1.paint,tvMsgContent1.width)
                            if (it.length > line1Length){
                                tagContent1=it.substring(0,line1Length)
                                tagContent2=it.substring(line1Length,it.length)
                            }else{
                                tagContent1=it
                            }
                            tvMsgContent1.setText(tagContent1)
                            tvMsgContent2.setText(tagContent2)
                        }
                    }
                }
            })

        }

    }

    override fun getLayoutRes(): Int {
        return R.layout.chat_item_tag_lay
    }
}