package com.wawa.baselib.utils.dialog

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.wawa.baselib.R
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewAdapter
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewModel
import com.wawa.baselib.utils.baseadapter.BaseViewHolder
import com.wawa.baselib.utils.viewmodel.BaseVM
import io.reactivex.disposables.CompositeDisposable
import kotlin.properties.Delegates

/**
 *作者：create by 张金 on 2021/7/6 10:16
 *邮箱：564813746@qq.com
 */
abstract class BaseVMDialogFragment<V : ViewDataBinding,VM : BaseVM> : DialogFragment(){
    protected lateinit var binding : V
    lateinit var viewModel: VM
    protected var viewModelId by Delegates.notNull<Int>()
    val fragmentDisposible: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    var dialogWidth=0
    var dialogHeight=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(getLayoutId(), container, false)
        binding= DataBindingUtil.bind<V>(view)!!
        viewModelId=initVariableId()
        viewModel=initViewModel()
        binding.setVariable(viewModelId,viewModel)
        binding.lifecycleOwner=this
        initView(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return createDialog()
    }

    override fun onResume() {
        super.onResume()
        initDialogParams()
    }

    open fun createDialog(): Dialog{
        val dialog = activity?.let { Dialog(it, R.style.dialog) }
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        val window = dialog?.window
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        params?.width= dialogWidth
        params?.height= dialogHeight
        window?.attributes = params
        window?.setWindowAnimations(R.style.bottomToTopAnim)
        return dialog!!
    }
    abstract fun initDialogParams()
    abstract fun getLayoutId(): Int
    abstract fun initView(view: View)
    abstract fun initVariableId(): Int
    abstract fun initViewModel(): VM

    fun showDialog(manager: FragmentManager, tag: String){

//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(this, tag).addToBackStack(null);
//        return transaction.commitAllowingStateLoss();
        if(!isAdded){
            show(manager,tag)
        }
    }

    override fun show(
        manager: FragmentManager,
        tag: String?
    ) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed) return
        }
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            //同一实例使用不同的tag会异常，这里捕获一下
            e.printStackTrace()
        }
    }

    fun <VM : BaseRecyclerViewModel<*, BaseViewHolder>> RecyclerView.bindAdapter(listAdapter: BaseRecyclerViewAdapter<VM>
                                                                                 , layoutManager: RecyclerView.LayoutManager?= null){
        this.layoutManager=layoutManager
        this.adapter=listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentDisposible?.clear()
    }
}