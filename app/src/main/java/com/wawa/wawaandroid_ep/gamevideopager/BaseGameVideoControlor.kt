package com.wawa.wawaandroid_ep.gamevideopager

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.wawaandroid_ep.gamevideopager.viewmodel.BaseGameVideoViewModel

/**
 * 作者：create by 张金 on 2021/2/4 18:13
 * 邮箱：564813746@qq.com
 */
abstract class BaseGameVideoControlor<V : ViewDataBinding> : BaseFragment<V>(){
    protected val baseGameVideoViewModel: BaseGameVideoViewModel by viewModels()
    protected var booleanMute = false
    protected var mRotation = 0
    var isAudioRunInBg=false
    var isCanPlayStream = false
    var isScreenPort=false
    /**
     * 切换摄像头
     */
    abstract fun switchCamera()

    /**
     * 显示连麦的小窗口
     */
    abstract fun showLinkView()

    /**
     * 隐藏连麦的小窗口
     */
    abstract fun hideLinkView()

    abstract fun joinLiveRoom()
    abstract fun quitLiveRoom()
    abstract fun upToVideoMember()
    abstract fun downToVideoMember()
    abstract fun gameUserLivePlayerPlay(url: String?, avatar: String?)
    abstract fun gameUserLivePlayerStop()

    abstract fun destroy()

    open fun setRotation(rotation: Int){
        this.mRotation=rotation
    }

    open fun setMute(isMute: Boolean){
        booleanMute=isMute
    }
}