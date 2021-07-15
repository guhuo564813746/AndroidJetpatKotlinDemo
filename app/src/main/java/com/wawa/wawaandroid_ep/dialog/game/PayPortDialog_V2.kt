package com.wawa.wawaandroid_ep.dialog.game

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ChargeItemListQuery
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.PayPortFishDialogLayBinding
import com.wawa.baselib.utils.baseadapter.imp.ArrayListAdapter
import com.wawa.baselib.utils.dialog.BaseVMDialogFragment
import com.wawa.wawaandroid_ep.dialog.viewmodel.PayDialogViewModel

/**
 *作者：create by 张金 on 2021/7/15 18:26
 *邮箱：564813746@qq.com
 */
class PayPortDialog_V2 : BaseVMDialogFragment<PayPortFishDialogLayBinding,PayDialogViewModel>() {
    val payListAdapter= ArrayListAdapter<ChargeItemListQuery.ChargeItemList>()
    override fun initDialogParams() {

    }

    override fun getLayoutId(): Int {
        return R.layout.pay_port_fish_dialog_lay
    }

    override fun initView(view: View) {
        val layoutManager=GridLayoutManager(activity,2,LinearLayoutManager.VERTICAL,false)
        binding.lvTopup.bindAdapter(payListAdapter,layoutManager)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): PayDialogViewModel {
        val payDialogViewModel: PayDialogViewModel by viewModels()
        return payDialogViewModel
    }
}