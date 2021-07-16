package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeItemListQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.PayPortFishDialogLayBinding
import com.wawa.baselib.utils.AppUtils
import com.wawa.baselib.utils.apollonet.BaseDataSource
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.adapter.viewmodel.PayItemViewModel
import com.wawa.wawaandroid_ep.dialog.viewmodel.PayDialogViewModel
import com.wawa.wawaandroid_ep.fragment.ChargeFragment
import com.wawa.wawaandroid_ep.pay.PayManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *作者：create by 张金 on 2021/7/15 18:26
 *邮箱：564813746@qq.com
 */
class PayPortDialog_V2 : BaseVMDialogFragment<PayPortFishDialogLayBinding,PayDialogViewModel>() {
    companion object{
        var payManager: PayManager?= null
        var shutDownDialog= MutableLiveData<Boolean>(false)
    }
    val apolloDataSource: BaseDataSource by lazy {
        (activity?.application as WawaApp).getDataSource(WawaApp.ServiceTypes.COROUTINES)
    }
    val payListAdapter= ArrayListAdapter<ChargeItemListQuery.ChargeItemList>()
    override fun initDialogParams() {
        dialogWidth=AppUtils.dp2px(activity,290f)
        dialogHeight=AppUtils.dp2px(activity,450f)
    }

    override fun getLayoutId(): Int {
        return R.layout.pay_port_fish_dialog_lay
    }

    override fun initView(view: View) {
        initViewObserver()
        activity?.let {
            payManager= PayManager(it,WawaApp.apolloClient)
        }
        val layoutManager=GridLayoutManager(activity,2,LinearLayoutManager.VERTICAL,false)
        binding.lvTopup.bindAdapter(payListAdapter,layoutManager)
        initChargeData()
    }

    private fun initViewObserver(){
        shutDownDialog.observe(this, Observer {
            if (it){
                dismissAllowingStateLoss()
            }
        })
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    fun initChargeData(){
        val successChargeItemDispose= apolloDataSource.chargeItemList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleSuccessChargeItem)
        fragmentDisposible.add(successChargeItemDispose)
        apolloDataSource.getChargeItemList()
    }

    fun handleSuccessChargeItem(chargeItemList: List<ChargeItemListQuery.ChargeItemList>){
        for (i in chargeItemList){
            val payItemVM= PayItemViewModel()
            payItemVM.model=i
            payListAdapter.add(payItemVM)
        }
    }

    override fun initViewModel(): PayDialogViewModel {
        val payDialogViewModel: PayDialogViewModel by viewModels()
        return payDialogViewModel
    }
}