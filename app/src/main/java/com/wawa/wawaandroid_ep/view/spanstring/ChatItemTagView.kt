package com.wawa.wawaandroid_ep.view.spanstring

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.robotwar.app.R
import com.wawa.baselib.utils.BitmapUtils
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.baselib.utils.glide.utils.ImageUtil
import com.wawa.wawaandroid_ep.bean.game.ChatItemTagBean
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *作者：create by 张金 on 2021/6/28 15:11
 *邮箱：564813746@qq.com
 */
class ChatItemTagView(private val mContext: Context,private val chatItemTagBean: ChatItemTagBean) {
    var tagView: View?=null
    lateinit var bitmapUtils: BitmapUtils
    init {
        tagView=LayoutInflater.from(mContext).inflate(R.layout.chat_item_tag_lay,null)
        bitmapUtils= BitmapUtils()
        val rlGamerContainer: RelativeLayout=tagView!!.findViewById(R.id.rl_gamer_container)
        val tvGamerName: TextView = tagView!!.findViewById(R.id.tv_gamer_name)
        val imHead: ImageView = tagView!!.findViewById(R.id.im_Head)
        val imHeadbg: ImageView =tagView!!.findViewById(R.id.im_headbg)
        val imGamerHonorBg: ImageView= tagView!!.findViewById(R.id.im_gamer_honor_bg)
        tvGamerName.setText(chatItemTagBean.name)
//        ImageLoader.with(mContext)
//            .url(chatItemTagBean.imgUrl)
//            .placeHolder(R.color.white)
//            .rectRoundCorner(ImageUtil.dip2px(30f), RoundedCornersTransformation.CornerType.ALL)
//            .into(imHead)
        imHead.setImageResource(R.mipmap.icon_zhangdan)
        chatItemTagBean.imgHeaderBg?.let {
            ImageLoader.with(mContext)
                .url(it)
                .into(imHeadbg)
        }
        chatItemTagBean.honorBg?.let {
            ImageLoader.with(mContext)
                .url(it)
                .into(imGamerHonorBg)
        }

    }



   /* fun getChatItemTagViewDrawable(): Drawable?{
        var tagDrawable: Drawable?= null
        tagView?.let {
            val bitMap=bitmapUtils.createViewBitmap(it)
            tagDrawable=BitmapDrawable(mContext.resources,bitMap)
        }
        return tagDrawable
    }*/
}