package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.apollographql.apollo.FeedBackListQuery
import com.apollographql.apollo.MalfunctionListQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FeedbackActivityLayBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.FeedBackFragmentVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/7/2 14:27
 *邮箱：564813746@qq.com
 */
class FeedBackFragment : BaseFragment<FeedbackActivityLayBinding,FeedBackFragmentVM>(){
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): FeedBackFragmentVM {
        val feedBackFragmentVm: FeedBackFragmentVM by viewModels()
        return feedBackFragmentVm
    }

    override fun getLayoutId(): Int {
        return R.layout.feedback_activity_lay
    }

    override fun initFragmentView() {
        viewModel.clicks.observe(this, Observer {
            when(it.id){
                R.id.rl_addphoto ->{

                }
                R.id.tv_commitfeedback ->{

                }
            }
        })
        initMalfunctionList()
        initFeedBackRecordList()
    }

    fun initMalfunctionList(){
        val malfunctionsSuccessDp=apolloDataSource.malfunctionList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlerMalfunctionList)
        fragmentDisposible.add(malfunctionsSuccessDp)
        apolloDataSource.getMalfunctionList("")
    }

    fun handlerMalfunctionList(feedbackList: MalfunctionListQuery.MalfunctionList){

    }

    fun initFeedBackRecordList(){

    }
}