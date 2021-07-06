package com.wawa.wawaandroid_ep.gamevideopager

import androidx.databinding.ViewDataBinding
import com.wawa.wawaandroid_ep.base.fragment.BaseFragment
import com.wawa.baselib.utils.viewmodel.BaseVM

/**
 * 作者：create by 张金 on 2021/2/4 18:13
 * 邮箱：564813746@qq.com
 */
abstract class BaseGameVideoControlor<V : ViewDataBinding,VM : BaseVM> : BaseFragment<V,VM>(){
    companion object{
        val MASTER_VIDEO_URL="MASTER_VIDEO_URL"
        val SLAVE_VIDEO_URL="SLAVE_VIDEO_URL"
    }
    protected var booleanMute = false
    protected var mRotation = 0
    protected var masterLiveStreamUrl: String? = null
    protected var slaveLiveStreamUrl: String? = null
    var isAudioRunInBg=false
    var isCanPlayStream = false
    var isScreenPort=false
    protected var switchCamera = true
    protected var isMasterUrlChange = false
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

    fun setVideoStream(masterUrl: String?, slaveUrl: String){
        isMasterUrlChange = if (masterLiveStreamUrl != null && masterLiveStreamUrl == masterUrl) {
            false
        } else {
            true
        }
        masterLiveStreamUrl = masterUrl
        slaveLiveStreamUrl = slaveUrl
    }

    open fun setRotation(rotation: Int){
        this.mRotation=rotation
    }

    open fun setMute(isMute: Boolean){
        booleanMute=isMute
    }
}