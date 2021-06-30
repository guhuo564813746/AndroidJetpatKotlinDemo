package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.widget.TextView
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatItemBean
import com.wawa.wawaandroid_ep.view.spanstring.RoundBackgroudColorSpan

/**
 *作者：create by 张金 on 2021/6/25 17:59
 *邮箱：564813746@qq.com
 */
class ChatItemViewModel : ArrayListViewModel<GameRoomChatDataBean>(){
    val MSG_TYPE_OFFICIAL=0
    val MSG_TYPE_PLAYER=1
    val MSG_TYPE_FIXER=2
    val MSG_TYPE_CS=3
    override fun onBindAdapter(adapter: ArrayListAdapter<GameRoomChatDataBean>) {
        when(model?.source){
            0->{
                //官方通知
                initShapeTagMsg(MSG_TYPE_OFFICIAL)
            }
            1->{
                //玩家
                initDrawableTagMsg()
            }
            2->{
                //机修
                initShapeTagMsg(MSG_TYPE_FIXER)
            }
            3->{
                //客服
                initShapeTagMsg(MSG_TYPE_CS)
            }
        }
    }

    fun initDrawableTagMsg(){

    }

    fun initShapeTagMsg(shapeType: Int){
        var tag=""
        var tagName=""
        var msgContent=""
        var tagBgColor=0
        when(shapeType){
            MSG_TYPE_OFFICIAL->{
                tag="官方通知"
                tagBgColor=R.color.msg_tag_bg_blue
            }
            MSG_TYPE_FIXER->{
                tag="机修"
                tagBgColor=R.color.msg_tag_bg_yellow
            }
            MSG_TYPE_CS->{
                tag="客服"
                tagBgColor=R.color.msg_tag_bg_purple
            }
        }
        model?.user_nickname?.let {
            tagName=it
        }
        model?.msg_list?.let {
            if (it.size >0){
                it.get(0).body?.text?.let {
                    msgContent=it
                }
            }
        }
        val spanString=SpannableString("${tag}+ \t +${tagName} +\t +${msgContent}")
        val spanRoundBg=RoundBackgroudColorSpan(tagBgColor,R.color.msg_tag_color)
        spanString.setSpan(spanRoundBg,0,tag.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        viewHolder?.view?.let {
            (it as TextView).setText(spanString)
        }

    }


    override fun getLayoutRes(): Int {
        return R.layout.gameroom_chat_item_lay
    }
}