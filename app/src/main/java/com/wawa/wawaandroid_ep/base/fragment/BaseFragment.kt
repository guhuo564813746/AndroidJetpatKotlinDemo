package com.wawa.wawaandroid_ep.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewAdapter
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewModel
import com.wawa.baselib.utils.baseadapter.BaseViewHolder
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import kotlin.properties.Delegates

/**
 *作者：create by 张金 on 2021/1/13 18:15
 *邮箱：564813746@qq.com
 */
abstract class BaseFragment<V : ViewDataBinding, VM : BaseVM> : Fragment(){
    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    protected var viewModelId by Delegates.notNull<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot=inflater.inflate(getLayoutId(),container,false)
        binding= DataBindingUtil.bind<V>(viewRoot)!!
        viewModelId=initVariableId()
        viewModel=initViewModel()
        binding.setVariable(viewModelId,viewModel)
        binding.lifecycleOwner=this
        initFragmentView()
        return binding.root
    }
    abstract fun initVariableId(): Int
    abstract fun initViewModel(): VM
    abstract fun getLayoutId() : Int
    abstract fun initFragmentView()

    fun <VM : BaseRecyclerViewModel<*, BaseViewHolder>> RecyclerView.bindAdapter(listAdapter: BaseRecyclerViewAdapter<VM>
                                                                                 , layoutManager: RecyclerView.LayoutManager?= null){
        this.layoutManager=layoutManager
        this.adapter=listAdapter
    }
}