package com.wawa.wawaandroid_ep.adapter.viewmodel

import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import com.apollographql.apollo.FeedbackCommentAddMutation
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel

/**
 *作者：create by 张金 on 2021/7/9 10:52
 *邮箱：564813746@qq.com
 */
class FeedbackRecordItemCommitVM : ArrayListViewModel<FeedbackCommentAddMutation.Feedback>() {
    override fun onBindAdapter(adapter: ArrayListAdapter<FeedbackCommentAddMutation.Feedback>) {
        val statusGd = GradientDrawable()
        statusGd.shape = GradientDrawable.OVAL
        statusGd.setColor(mContext.resources.getColor(R.color.normal_red))
        viewHolder?.getView<View>(R.id.v_fb_msg)?.setBackground(statusGd)
        model?.fragments()?.feedback()?.let {
            viewHolder?.getView<TextView>(R.id.tv_feedback_title)?.setText(it.content())
            viewHolder?.getView<TextView>(R.id.tv_feedback_time)?.setText(it.replyTimeFormat())
            when(it.status()){
                0->{
                    viewHolder?.getView<TextView>(R.id.tv_feedback_status)?.setText("未处理")
                    viewHolder?.getView<TextView>(R.id.tv_feedback_status)?.setTextColor(mContext.getResources().getColor(R.color.base_blue))
                }
                1->{
                    viewHolder?.getView<TextView>(R.id.tv_feedback_status)?.setText("已处理")
                    viewHolder?.getView<TextView>(R.id.tv_feedback_status)?.setTextColor(mContext.getResources().getColor(R.color.normal_green))
                }
                2->{
                    viewHolder?.getView<TextView>(R.id.tv_feedback_status)?.setText("处理中")
                    viewHolder?.getView<TextView>(R.id.tv_feedback_status)?.setTextColor(mContext.getResources().getColor(R.color.yellow));
                }
                else->{}
            }

        }

    }

    override fun getLayoutRes(): Int {
        return R.layout.feedback_record_item_lay
    }
}