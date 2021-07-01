package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.robotwar.app.R
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.wawaandroid_ep.bean.game.ChatItemTagBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatDataBean
import com.wawa.wawaandroid_ep.bean.game.GameRoomChatItemBean
import com.wawa.wawaandroid_ep.view.ViewUtils
import com.wawa.wawaandroid_ep.view.spanstring.ChatItemTagView
import com.wawa.wawaandroid_ep.view.spanstring.DrawbleBackgroudSpan
import com.wawa.wawaandroid_ep.view.spanstring.RoundBackgroudColorSpan

/**
 *作者：create by 张金 on 2021/6/25 17:59
 *邮箱：564813746@qq.com
 */
class ChatItemViewModel : ArrayListViewModel<GameRoomChatDataBean>(){
    val TAG="ChatItemViewModel"
    val MSG_TYPE_OFFICIAL=0
    val MSG_TYPE_PLAYER=1
    val MSG_TYPE_FIXER=2
    val MSG_TYPE_CS=3
    lateinit var viewUtils: ViewUtils
    override fun onBindAdapter(adapter: ArrayListAdapter<GameRoomChatDataBean>) {
        viewUtils=ViewUtils(mContext)
        when(model?.source){
            0->{
                //官方通知
                initShapeTagMsg(MSG_TYPE_OFFICIAL)
            }
            1->{
                //玩家
//                initDrawableTagMsg()
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
        viewHolder?.view?.context?.let {
            val  chatItemTagBean = ChatItemTagBean(model?.user_nickname,"","","")
            val chatItemTagView =ChatItemTagView(it,chatItemTagBean)
            Log.d(TAG,"initDrawableTagMsg--2")
            val bitMap=chatItemTagView.bitmapUtils.createViewBitmap(chatItemTagView.tagView!!,AppUtils.dp2px(mContext,120f),AppUtils.dp2px(mContext,58f))
            val drawable= BitmapDrawable(it.resources,bitMap)
            drawable?.let {
                viewHolder?.view?.let {
                    val drawableSpan=DrawbleBackgroudSpan(drawable,it as TextView)
                    model?.msg_list?.let {
                        if (it.size > 0){
                            it.get(0)?.body?.text?.let {
                                val spanString=SpannableString(it)
                                spanString.setSpan(drawableSpan,0,10,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                (viewHolder!!.view as TextView).setText(spanString)
                            }
                        }
                    }

                }
            }
        }
    }

    fun initShapeTagMsg(shapeType: Int){

        viewHolder?.view?.let {
            val tvMsgTag=it.findViewById<TextView>(R.id.tv_msg_tag)
            val tvMsgContent1=it.findViewById<TextView>(R.id.tv_msg_content1)
            val tvMsgName=it.findViewById<TextView>(R.id.tv_msg_name)
            val tvMsgContent2=it.findViewById<TextView>(R.id.tv_msg_content2)
            var tag=""
            var tagName=""
            var msgContent=""
            var tagBgColor=0
            val tagBg=GradientDrawable()
            tagBg.cornerRadius=AppUtils.dp2px(mContext,10f).toFloat()
            when(shapeType){
                MSG_TYPE_OFFICIAL->{
                    tag="官方通知"
                    tagBgColor=mContext.resources.getColor(R.color.msg_tag_bg_blue)
                }
                MSG_TYPE_FIXER->{
                    tag="机修"
                    tagBgColor=mContext.resources.getColor(R.color.msg_tag_bg_yellow)
                }
                MSG_TYPE_CS->{
                    tag="客服"
                    tagBgColor=mContext.resources.getColor(R.color.msg_tag_bg_purple)
                }
            }
            tagBg.setColor(tagBgColor)
            model?.user_nickname?.let {
                tagName=it
                tvMsgName.setText(tagName)
                tvMsgName.setTextColor(tagBgColor)
            }
            model?.msg_list?.let {
                if (it.size >0){
                    it.get(0).body?.text?.let {
                        msgContent=it
                    }
                }
            }
            val spanString="${msgContent}"
//        val spanRoundBg=RoundBackgroudColorSpan(tagBgColor,R.color.msg_tag_color)
//        spanString.setSpan(spanRoundBg,0,tag.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            var line1Length=0
            tvMsgTag.setText(tag)
            tvMsgTag.background=tagBg
            tvMsgContent1.post(Runnable {
                line1Length=viewUtils.getLineMaxNumber(spanString,tvMsgContent1.paint,tvMsgContent1.width)
                var tagContent1=""
                var tagContent2=""
                if (spanString.length > line1Length){
                    tagContent1=spanString.substring(0,line1Length)
                    tagContent2=spanString.substring(line1Length,spanString.length)
                }else{
                    tagContent1=spanString
                }
                tvMsgContent1.setText(tagContent1)
                tvMsgContent2.setText(tagContent2)
            })
        }

    }


    override fun getLayoutRes(): Int {
        return R.layout.gameroom_chat_item_lay
    }
}