package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.apollographql.apollo.FeedbackCommentQuery
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel
import com.wawa.baselib.utils.glide.loader.ImageLoader
import com.wawa.wawaandroid_ep.MainViewModule
import com.wawa.wawaandroid_ep.activity.ImagePagerActivity
import java.util.*

/**
 *作者：create by 张金 on 2021/7/14 17:53
 *邮箱：564813746@qq.com
 */
class FeedbackCommentItemVM : ArrayListViewModel<FeedbackCommentQuery.List>() {
    var itemSelectListener: OnItemSelectListener? =null
    override fun onBindAdapter(adapter: ArrayListAdapter<FeedbackCommentQuery.List>) {
        val imFeedback=viewHolder!!.view.findViewById<ImageView>(R.id.im_feedback)
        val tvFbDetailTitle: TextView = viewHolder!!.view.findViewById(R.id.tv_fb_detail_title)
        val tvFbDetailContent: TextView = viewHolder!!.view.findViewById(R.id.tv_fb_detail_content)
        val tvFeedbackDatatime: TextView = viewHolder!!.view.findViewById(R.id.tv_feedback_datatime)
        model?.fragments()?.feedbackComment()?.let {
            if (it.pictureList() != null && it.pictureList()!!.size >0){
                imFeedback.visibility= View.VISIBLE
                ImageLoader.with(mContext).url(it.pictureList()!!.get(0)).into(imFeedback)
                imFeedback.setOnClickListener {
                    if (itemSelectListener != null){
                        itemSelectListener!!.onItemSelected(model?.fragments()?.feedbackComment()?.pictureList()!!.get(0))
                    }
                }
            }else{
                imFeedback.visibility=View.GONE
            }
            when(it.source()){
                0->{
                    tvFbDetailTitle.setText(MainViewModule.userData?.nickName()+":")
                }
                1->{
                    tvFbDetailTitle.setText("系统回复:")
                }
            }
            tvFbDetailContent.setText(it.content())
            tvFeedbackDatatime.setText(it.uptimeFormat())
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fb_detail_comment_item_lay
    }

    interface OnItemSelectListener{
        fun onItemSelected(curImg: String)
    }
}