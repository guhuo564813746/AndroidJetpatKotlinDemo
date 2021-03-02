package com.wawa.wawaandroid_ep.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo.ChargeItemListQuery
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.BR
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.charge.ChargeDialogAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.databinding.ChargeListFmLayBinding
import com.wawa.wawaandroid_ep.fragment.viewmodule.ChargeItemViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/1/29 11:28
 *邮箱：564813746@qq.com
 */
class ChargeListFragment : BaseFragment<ChargeListFmLayBinding,ChargeItemViewModel>() {
    private val TAG="ChargeListFragment"
    var chargeItemType=ChargeFragment.GOODS_TYPE_COIN
    private var chargeDialogAdapter: ChargeDialogAdapter?= null
    private val chargeDataDisposable = CompositeDisposable()
    private val dataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    override fun getLayoutId(): Int {
        return R.layout.charge_list_fm_lay
    }

    override fun initFragmentView() {
        initChargeItemView()
        chargeItemType= arguments?.getInt(ChargeFragment.BUNDLE_PARAMS_GOODS_TYPE,ChargeFragment.GOODS_TYPE_COIN)!!
        initChargeData()
    }

    fun initChargeItemView(){
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=
            GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        if (chargeDialogAdapter == null){
            chargeDialogAdapter= activity?.let { ChargeDialogAdapter(it) }
        }
        binding.recyclerView.adapter=chargeDialogAdapter
    }

    fun initChargeData(){
        val successChargeItemDispose= dataSource.chargeItemList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessChargeItem)

        val errorChargeItemDispose=dataSource.error
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleErrorChargeItem)
        chargeDataDisposable.add(successChargeItemDispose)
        chargeDataDisposable.add(errorChargeItemDispose)
        dataSource.getChargeItemList()
    }

    fun handleSuccessChargeItem(chargeItemList: List<ChargeItemListQuery.ChargeItemList>){
        chargeDialogAdapter?.listType=chargeItemType
        chargeDialogAdapter?.setData(chargeItemList)
    }

    fun handleErrorChargeItem(e: Throwable?){
        LogUtils.d(TAG,"handleErrorChargeItem--"+e?.message)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ChargeItemViewModel {
        val chargeItemViewModel: ChargeItemViewModel by viewModels()
        return chargeItemViewModel
    }

}