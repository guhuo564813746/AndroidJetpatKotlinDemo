package com.wawa.wawaandroid_ep.base.activity

import android.graphics.Typeface
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.wawa.wawaandroid_ep.WawaApp
import com.wawa.wawaandroid_ep.base.viewmodel.BaseVM
import kotlin.properties.Delegates

/**
 *作者：create by 张金 on 2021/1/13 16:17
 *邮箱：564813746@qq.com
 */
abstract class BaseActivity<V : ViewDataBinding,VM : BaseVM> : AppCompatActivity() {
    protected lateinit var binding : V
    public lateinit var viewModel: VM
    protected var viewModelId by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setFonts()
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,initContentView(savedInstanceState))
        viewModelId=initVariableId()
        viewModel=initViewModel()
        binding.setVariable(viewModelId,viewModel)
        binding.lifecycleOwner=this
        initView()
    }
    abstract fun initVariableId(): Int
    abstract fun initViewModel(): VM
    abstract fun initContentView(savedInstanceState: Bundle?): Int
    abstract fun initView()


   /* open fun <T : ViewModel?> createViewModel(
        fragment: Fragment?,
        cls: Class<T>?
    ): T {
//        return ViewModelProvider.of(fragment).get(cls);
        return ViewModelProvider(
            fragment!!,
            ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
        )[cls!!]
    }*/

    private fun setFonts() {
        if (WawaApp.mMainTypeface == null){
            WawaApp.mMainTypeface = Typeface.createFromAsset(assets, "font/myfont.ttf")
        }
        val typeface: Typeface? = WawaApp.mMainTypeface
        LayoutInflaterCompat.setFactory(
            LayoutInflater.from(this)
        ) { parent, name, context, attrs ->
            val delegate = delegate
            val view = delegate.createView(parent, name, context, attrs)
            if (view != null && view is TextView) {
                view.setTypeface(typeface)
            }
            if (view != null && view is EditText) {
                view.setTypeface(typeface)
            }
            view
        }
    }
}