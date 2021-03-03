package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.RecordlistitemFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.RecordListItemFragmentVM

/**
 *作者：create by 张金 on 2021/3/3 17:53
 *邮箱：564813746@qq.com
 */
class RecordListItemFragment : BaseFragment<RecordlistitemFmLayBinding,RecordListItemFragmentVM>(),
    OnRefreshListener, OnLoadMoreListener {
    val TAG="RecordListItemFragment"
    val GAME_RECORD="game_record"
    val COINS_RECORD="coins_record"
    val DIAMON_RECORD="diamon_record"
    val CHARGE_RECORD="charge_record"
    var recordItemType: String=GAME_RECORD
    private val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): RecordListItemFragmentVM {
        val recordListItemFragmentVM: RecordListItemFragmentVM by viewModels()
        return recordListItemFragmentVM
    }

    override fun getLayoutId(): Int {
        return R.layout.recordlistitem_fm_lay
    }

    override fun initFragmentView() {
        arguments?.let { recordItemType= requireArguments().getString(TAG,GAME_RECORD) }
        binding.refreshLayout.setOnRefreshListener(this)
        binding.refreshLayout.setOnLoadMoreListener(this)
        initRecordListData()
    }
    fun initRecordListData(){

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        LogUtils.d(TAG,"onRefresh--")
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        LogUtils.d(TAG,"onLoadMore--")
    }

}