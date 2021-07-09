package com.wawa.wawaandroid_ep.dialog.game

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.MalfunctionListQuery
import com.apollographql.apollo.RoomFeedbackAddMutation
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.fragment.Malfunction
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.FeedbackDialogLayBinding
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.dialog.BaseDialogFragment
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.viewmodel.dialog.FeedbackListVM
import com.wawa.wawaandroid_ep.dialog.viewmodel.GameFeedBackVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 *作者：create by 张金 on 2021/7/6 09:56
 *邮箱：564813746@qq.com
 */
class GameFeedBackDialog : BaseVMDialogFragment<FeedbackDialogLayBinding,GameFeedBackVM>(),
    FeedbackListVM.OnItemSelectListener {
    companion object{
        val TAG="GameFeedBackDialog"
    }
    var machineType=""
    var fbContent: String?=null
    var roomId: Int =0
    val questionAdapter=ArrayListAdapter<MalfunctionListQuery.List>()
    val apolloDataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    override fun initDialogParams() {
        dialogWidth= SizeUtils.dp2px(290f)
        dialogHeight= SizeUtils.dp2px(380f)
    }

    override fun getLayoutId(): Int {
        return R.layout.feedback_dialog_lay
    }

    override fun initView(view: View) {
        questionAdapter.clearData()
        val manager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding.lvQuestionFeedback.bindAdapter(questionAdapter,manager)
        val successDp=apolloDataSource.malfunctionList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handlerMalfunctionList)
        fragmentDisposible.add(successDp)
        apolloDataSource.getMalfunctionList(machineType)
        binding.tvCommitQuestion.setOnClickListener {
            fbContent?.let {
                commitFb()
            }
        }
    }
    //提交反馈
    fun commitFb(){
        val roomFeedbackQuery=RoomFeedbackAddMutation(roomId,fbContent!!)
        WawaApp.apolloClient.mutate(roomFeedbackQuery)
            .enqueue(object: ApolloCall.Callback<RoomFeedbackAddMutation.Data>(){
                override fun onFailure(e: ApolloException) {
                    ToastUtils.showShort(e.message)
                }

                override fun onResponse(response: Response<RoomFeedbackAddMutation.Data>) {
                    Log.d(TAG,"commitFb--"+response.errors?.size+response.data.toString())
                    if (response.errors == null || response.errors!!.size ==0){
                        ToastUtils.showShort(getString(R.string.tx_feedback_success_tips))
                        dismissAllowingStateLoss()
                    }
                }

            })
    }

    fun handlerMalfunctionList(data: MalfunctionListQuery.MalfunctionList){
        Log.d(TAG,"handlerMalfunctionList--"+data.toString())
        data?.list()?.let {
            if (it.size > 0){
                for (i in 0..it.size-1){
                    val mal=FeedbackListVM()
                    mal.model=it.get(i)
                    mal.pos=i
                    mal.listener=this
                    if (i==0){
                        fbContent=mal.model?.fragments()?.malfunction()?.title()
                        mal.isSelect=true
                    }else{
                        mal.isSelect=false
                    }
                    questionAdapter.add(mal)
                }
            }
        }
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): GameFeedBackVM {
        val gameFeedBackVM: GameFeedBackVM by viewModels()
        return gameFeedBackVM
    }

    override fun onItemSelected(content: String?,fbId: String?) {
        fbContent=content
    }
}