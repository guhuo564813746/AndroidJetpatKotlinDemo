package com.wawa.wawaandroid_ep.fragment

import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.ChooseLanguageLayBinding
import com.wawa.baselib.utils.LanguageUtils
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.wawaandroid_ep.adapter.ChooseLanguageAdapter
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.ChooseLanguageViewModel
import java.util.*

/**
 *作者：create by 张金 on 2021/3/3 15:03
 *邮箱：564813746@qq.com
 */
class ChooseLanguageFragment : BaseFragment<ChooseLanguageLayBinding, ChooseLanguageViewModel>(){
    private var lans: List<String> = ArrayList()
    private var lansType: List<String> = ArrayList()
    private var chooseLanguageAdapter: ChooseLanguageAdapter? = null
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): ChooseLanguageViewModel {
        val chooseLanguageViewModel: ChooseLanguageViewModel by viewModels()
        return chooseLanguageViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.choose_language_lay
    }

    override fun initFragmentView() {
        val imBack=binding.titlelayout.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            findNavController().popBackStack()
        }
        val titleRight=binding.titlelayout.findViewById<TextView>(R.id.tv_title_right)
        titleRight.visibility= View.VISIBLE
        titleRight.setText(getString(R.string.tx_save))
        titleRight.setOnClickListener {
            if (chooseLanguageAdapter == null){
                return@setOnClickListener
            }
            if (!SharePreferenceUtils.getStr(SharePreferenceUtils.LOCALE_LAN).equals(chooseLanguageAdapter?.curLan)){
                changeLanguage(chooseLanguageAdapter?.curLan)
            }else{
                findNavController().popBackStack()
            }
        }
        val title=binding.titlelayout.findViewById<TextView>(R.id.title)
        title.setText(getString(R.string.tv_choose_lanstips))
        initLans()
        initLansListView()

    }

    fun initLansListView(){
        binding.lvLans.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        chooseLanguageAdapter=activity?.let { ChooseLanguageAdapter(it,lans,lansType) }
        binding.lvLans.adapter= chooseLanguageAdapter
    }

    private fun initLans() {
        lans= listOf("跟随系统","简体中文","繁體中文(台灣)","繁體中文(香港)","English","日本語","Bahasa Melayu"
            ,"ไทย","हिंदी","Bahasa Indonesia","Tiếng Việt","ជនជាតិខ្មែរ")
        lansType= listOf("1","zh-CN","zh-TW","zh-HK","en","ja","ms","th","inc","in","vi","km")
    }

    /**
     * 如果是7.0以下，我们需要调用changeAppLanguage方法，
     * 如果是7.0及以上系统，直接把我们想要切换的语言类型保存在SharedPreferences中即可
     * 然后重新启动MainActivity
     *
     * @param language
     */
    fun changeLanguage(language: String?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            LanguageUtils.changeAppLanguage(activity?.getApplicationContext(), language)
        }
        SharePreferenceUtils.saveStr(SharePreferenceUtils.LOCALE_LAN,language)
        findNavController().popBackStack()
    }

}