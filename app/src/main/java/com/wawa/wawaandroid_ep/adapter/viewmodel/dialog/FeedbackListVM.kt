package com.wawa.wawaandroid_ep.adapter.viewmodel.dialog

import android.widget.ImageView
import android.widget.TextView
import com.apollographql.apollo.MalfunctionListQuery
import com.apollographql.apollo.fragment.Malfunction
import com.robotwar.app.R
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.baseadapter.imp.ArrayListViewModel

/**
 *作者：create by 张金 on 2021/7/6 10:35
 *邮箱：564813746@qq.com
 */
class FeedbackListVM : ArrayListViewModel<MalfunctionListQuery.List>(){
    var pos=0
    var isSelect=false
    var listener: OnItemSelectListener?=null
    override fun getLayoutRes(): Int {
        return R.layout.item_question_lay
    }

    override fun onBindAdapter(adapter: ArrayListAdapter<MalfunctionListQuery.List>) {
        var imQuestfeedback:ImageView?=null
        var tvQuestionback: TextView?= null
        viewHolder?.view?.let {
            it.setOnClickListener {
                for (i in adapter.observableDataList){
                    val  vm: FeedbackListVM=i as FeedbackListVM
                    if (vm.pos == pos){
                        vm.isSelect=true
                    }else{
                        vm.isSelect=false
                    }
                }
                adapter.notifyDataSetChanged()
                listener?.let {
                    model?.let {
                        listener!!.onItemSelected(it.fragments()?.malfunction()?.title())
                    }
                }
            }
            imQuestfeedback=it.findViewById(R.id.im_questfeedback)
            tvQuestionback=it.findViewById(R.id.tv_questionback)
            model?.let {
                tvQuestionback?.setText(it.fragments()?.malfunction()?.title())
            }
            imQuestfeedback?.isSelected=isSelect
        }
    }

    interface OnItemSelectListener{
        fun onItemSelected(content: String?)
    }
}