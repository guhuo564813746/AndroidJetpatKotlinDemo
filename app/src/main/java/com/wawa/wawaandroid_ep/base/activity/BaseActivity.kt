package com.wawa.wawaandroid_ep.base.activity

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewAdapter
import com.wawa.baselib.utils.baseadapter.BaseRecyclerViewModel
import com.wawa.baselib.utils.baseadapter.BaseViewHolder
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
        setStatusBar()
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

    /**
     * 设置透明状态栏
     */
    protected fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0
        }
    }

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

    fun <VM : BaseRecyclerViewModel<*,BaseViewHolder>> RecyclerView.bindAdapter(listAdapter: BaseRecyclerViewAdapter<VM>
                                                                                ,layoutManager: RecyclerView.LayoutManager?= null){
        this.layoutManager=layoutManager
        this.adapter=listAdapter
    }
}