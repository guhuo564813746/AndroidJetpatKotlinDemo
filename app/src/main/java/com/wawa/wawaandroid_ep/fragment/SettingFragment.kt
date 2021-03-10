package com.wawa.wawaandroid_ep.fragment

import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.AppUtils
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.SettingFmLayBinding
import com.wawa.baselib.utils.GlideCatchUtil
import com.wawa.baselib.utils.SharePreferenceUtils
import com.wawa.baselib.utils.logutils.LogUtils
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.fragment.viewmodule.SettingFragmentViewModel

/**
 *作者：create by 张金 on 2021/3/2 16:54
 *邮箱：564813746@qq.com
 */
class SettingFragment : BaseFragment<SettingFmLayBinding,SettingFragmentViewModel>(),
    CompoundButton.OnCheckedChangeListener,View.OnClickListener {
    private val TAG="SettingFragment"
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): SettingFragmentViewModel {
        val settingFragmentViewModel: SettingFragmentViewModel by viewModels()
        return settingFragmentViewModel
    }



    override fun getLayoutId(): Int {
        return R.layout.setting_fm_lay
    }

    override fun initFragmentView() {
        val title=binding.titleView.findViewById<TextView>(R.id.title)
        title.setText("设置")
        val imBack=binding.titleView.findViewById<ImageView>(R.id.im_back)
        imBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnBgmSwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM)
        binding.btnBgmLiveSwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_LIVE)
        binding.btnBgmGameSwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_GAME)
        binding.btnBgmKeySwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.BGM_KEY)
        binding.btnPlayerOpenglSwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.PLAYER_OPENGL)
        binding.btnVideoPlayerSwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.VIDEO_PLAYER)
        binding.btnWifisetSwitch.isChecked=SharePreferenceUtils.getSwitch(SharePreferenceUtils.WIFI_SET)
        binding.btnBgmSwitch.setOnCheckedChangeListener(this)
        binding.btnBgmLiveSwitch.setOnCheckedChangeListener(this)
        binding.btnBgmGameSwitch.setOnCheckedChangeListener(this)
        binding.btnBgmKeySwitch.setOnCheckedChangeListener(this)
        binding.btnPlayerOpenglSwitch.setOnCheckedChangeListener(this)
        binding.btnVideoPlayerSwitch.setOnCheckedChangeListener(this)
        binding.btnWifisetSwitch.setOnCheckedChangeListener(this)
        val version: String = String.format(
            getString(R.string.FULL_VERSION),
            AppUtils.getAppVersionName(),
            AppUtils.getAppVersionCode()
        )
        binding.clearCacheText.setText(" (" + getCacheSize() + ")")
        binding.btnVersionText.setText(version)
        binding.chooseLanText.setText(getString(R.string.tv_choose_lanstips))
        binding.tvCurlan.setText(getLanguageName(SharePreferenceUtils.getStr(SharePreferenceUtils.LOCALE_LAN)))

        binding.profileLayout.setOnClickListener(this)
        binding.btnClearCache.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnChooseLan.setOnClickListener(this)

    }

    override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {
        when(view?.id){
            R.id.btn_bgm_switch -> {
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM,isChecked)
            }
            R.id.btn_bgm_live_switch -> {
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_LIVE,isChecked)
            }
            R.id.btn_bgm_game_switch -> {
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_GAME,isChecked)
            }
            R.id.btn_bgm_key_switch ->{
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.BGM_KEY,isChecked)
            }
            R.id.btn_player_opengl_switch -> {
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.PLAYER_OPENGL,isChecked)
            }
            R.id.btn_video_player_switch ->{
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.VIDEO_PLAYER,isChecked)
            }
            R.id.btn_wifiset_switch ->{
                SharePreferenceUtils.saveSwitch(SharePreferenceUtils.WIFI_SET,isChecked)
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.profile_layout ->{}
            R.id.btn_clear_cache ->{}
            R.id.btn_logout -> {
                SharePreferenceUtils.saveToken("")
                SharePreferenceUtils.saveUid("")
                findNavController().navigate(R.id.loginFragment)
            }
            R.id.btn_choose_lan -> {
                findNavController().navigate(R.id.chooseLanguageFragment)
            }
        }
    }

    private fun getCacheSize(): String? {
        var cache: String = GlideCatchUtil.getInstance(activity?.applicationContext).getCacheSize()
        LogUtils.e(TAG,"缓存大小--->$cache")
        if ("0.0Byte".equals(cache, ignoreCase = true)) {
            cache = getString(R.string.no_cache)
        }
        return cache
    }

    private fun getLanguageName(languageType: String?): String? {
        var result = ""
        result = when (languageType) {
            "km" -> "ភាសា khmer"
            "vi" -> "Ngôn ngữ Việt"
            "in" -> "Bahasa Indonesia"
            "inc" -> "हिंदी भाषा"
            "th" -> "ภาษาไทย"
            "ms" -> "Malay"
            "ja" -> "日本語"
            "en" -> "English"
            "zh-HK" -> "繁體中文(香港)"
            "zh-TW" -> "繁體中文(台灣)"
            "zh-CN" -> "简体中文"
            else -> languageType.toString()
        }
        return result
    }

}