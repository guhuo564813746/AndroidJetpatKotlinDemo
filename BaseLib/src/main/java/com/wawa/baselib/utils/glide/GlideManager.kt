package com.wawa.baselib.utils.glide

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

/**
 *
 * @ProjectName:    WawaAndroid_EP
 * @Package:        com.wawa.baselib.utils.glide
 * @ClassName:      GlideManager
 * @Description:     java类作用描述
 * @Author:         zhangjin
 * @CreateDate:     2021-01-31 10:10
 * @UpdateDate:     2021-01-31 10:10
 * @Version:        1.0
 */
open class GlideManager() {
    lateinit var mContext: Context
    open fun initGlide(context: Context){
        mContext=context
    }
    open fun disPlayCircleImg(url: String?,img: ImageView){
        val glideOption= RequestOptions().transform(CircleImageTransformation(mContext))
        Glide.with(mContext).applyDefaultRequestOptions(glideOption).load(url).into(img)
    }

    open fun displayImg(url: String?, img: ImageView){
        Glide.with(mContext).load(url).into(img)
    }

    open fun displayCornerImg(url: String?, img: ImageView,cornor: Float=0f){
        val glideOption= RequestOptions().transform(CircleImageTransformation(mContext,cornor))
        Glide.with(mContext).applyDefaultRequestOptions(glideOption).load(url).into(img)
    }

}