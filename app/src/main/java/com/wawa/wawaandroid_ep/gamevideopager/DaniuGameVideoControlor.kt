package com.wawa.wawaandroid_ep.gamevideopager

import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daniulive.smartplayer.SmartPlayerJniV2
import com.eventhandle.NTSmartEventCallbackV2
import com.eventhandle.NTSmartEventID
import com.videoengine.NTRenderer
import com.wawa.wawaandroid_ep.R
import com.wawa.wawaandroid_ep.databinding.DaniuGamevideoFmLayBinding
import com.wawa.wawaandroid_ep.gamevideopager.viewmodel.DaniuGameVideoViewModel

/**
 *作者：create by 张金 on 2021/2/5 14:18
 *邮箱：564813746@qq.com
 */
class DaniuGameVideoControlor : BaseGameVideoControlor<DaniuGamevideoFmLayBinding>() ,SurfaceHolder.Callback{
    companion object{
        val MASTER_VIDEO_URL="MASTER_VIDEO_URL"
        val SLAVE_VIDEO_URL="SLAVE_VIDEO_URL"
    }
    private val TAG="DaniuGameVideoControlor"
    private val playBuffer = 0 // 默认0ms
    private val daniuGameVideoViewModel: DaniuGameVideoViewModel by viewModels()
    private lateinit var mSurfaceView: SurfaceView
    private lateinit var mSlaveSurfaceView: SurfaceView
    private   var masterPlayer: SmartPlayerJniV2?=null
    private   var slavePlayer: SmartPlayerJniV2?=null
    private var masterLiveStreamUrl: String? = null
    private var slaveLiveStreamUrl: String? = null
    private var isEnableHardwareRenderMode=false
    private var isPlaying = false
    private var playerHandle: Long = 0
    private var masterPlayerHandle: Long = 0
    private var slavePlayerHandle: Long = 0
    private var isFastStartup = true // 是否秒开, 默认true
    private var isHardwareDecoder = true // 硬编码
    private var isLowLatency = false // 超低延时，默认不开启
    private var rotateDegress = 0
    private var mIsVideo1 = true //是否是视角1

    init {
        System.loadLibrary("SmartPlayer");
    }


    override fun switchCamera() {
        Log.d(TAG,"switchCamera--")
        if (masterPlayer == null || masterPlayerHandle == 0L) {
            return
        }
        if (slavePlayer == null || slavePlayerHandle == 0L) {
            return
        }
        if (!mIsVideo1) {
            //切换到视角1
            if (mSurfaceView.visibility != View.VISIBLE) {
                mSurfaceView.visibility = View.VISIBLE
            }
            mSurfaceView.setZOrderMediaOverlay(true)
            //主视图铺满全屏
            val params = mSurfaceView.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.flVideoStream.updateViewLayout(mSurfaceView, params)
        } else {
            if (mSlaveSurfaceView.visibility != View.VISIBLE) {
                mSlaveSurfaceView.visibility = View.VISIBLE
            }
            //主视图的高度和宽度改为1，实现隐藏效果
            val params = mSurfaceView.layoutParams
            params.width = 1
            params.height = 1
            binding.flVideoStream.updateViewLayout(mSurfaceView, params)
        }
        mIsVideo1 = !mIsVideo1
    }

    override fun showLinkView() {
        Log.d(TAG,"showLinkView--")
    }

    override fun hideLinkView() {
        Log.d(TAG,"hideLinkView--")
    }

    override fun joinLiveRoom() {
        Log.d(TAG,"joinLiveRoom--")
    }

    override fun quitLiveRoom() {
        Log.d(TAG,"quitLiveRoom--")
    }

    override fun upToVideoMember() {
        Log.d(TAG,"upToVideoMember--")
    }

    override fun downToVideoMember() {
        Log.d(TAG,"downToVideoMember--")
    }

    override fun gameUserLivePlayerPlay(url: String?, avatar: String?) {
        Log.d(TAG,"gameUserLivePlayerPlay--")
    }

    override fun gameUserLivePlayerStop() {
        Log.d(TAG,"gameUserLivePlayerStop--")
    }

    override fun destroy() {
        Log.d(TAG,"destroy--")
    }

    override fun getLayoutId(): Int {
        Log.d(TAG,"getLayoutId--")
        return R.layout.daniu_gamevideo_fm_lay
    }

    override fun setRotation(rotation: Int) {
        super.setRotation(rotation)
        Log.d(TAG,"setRotation--")
        rotateDegress = rotation
        if (masterPlayerHandle != 0L) {
            masterPlayer!!.SmartPlayerSetRotation(masterPlayerHandle, rotateDegress)
        }
        if (slavePlayerHandle != 0L) {
            slavePlayer!!.SmartPlayerSetRotation(slavePlayerHandle, rotateDegress)
        }
    }

    override fun setMute(isMute: Boolean) {
        super.setMute(isMute)
        if (masterPlayerHandle != 0L) {
            masterPlayer!!.SmartPlayerSetMute(masterPlayerHandle, if (isMute) 1 else 0)
        }
        if (slavePlayerHandle != 0L) {
            slavePlayer!!.SmartPlayerSetMute(slavePlayerHandle, if (isMute) 1 else 0)
        }
    }

    override fun initFragmentView() {
        Log.d(TAG,"initFragmentView--")
        masterLiveStreamUrl=arguments?.getString(MASTER_VIDEO_URL)
        slaveLiveStreamUrl= arguments?.getString(SLAVE_VIDEO_URL)
        if (mSurfaceView == null){
            mSurfaceView=createSurfaceView()
        }
        binding.flVideoStream.addView(mSurfaceView)
        mSurfaceView.visibility=View.VISIBLE
        //mSurfaceView.setZOrderOnTop(true);
        mSurfaceView.setZOrderMediaOverlay(true)
        initGameVideo()
    }

    fun createSurfaceView(): SurfaceView{
        var surfaceView: SurfaceView
        val useOpenGL = false
        surfaceView=NTRenderer.CreateRenderer(activity,useOpenGL)
        if (isEnableHardwareRenderMode){
            var surfaceHolder=surfaceView.holder
            if (surfaceHolder == null){
                Log.e(TAG, "CreateView, surfaceHolder with null..")
            }
            surfaceHolder.addCallback(this)
        }
        return surfaceView
    }

    fun initGameVideo(){
        masterPlayer = initSmartPlayer(masterLiveStreamUrl, mSurfaceView)
        masterPlayerHandle = playerHandle
        isPlaying = true
    }

    fun initSmartPlayer(streamUrl: String?,surfaceView: SurfaceView): SmartPlayerJniV2?{
        if (streamUrl.isNullOrEmpty()){
            return null
        }
        var player = SmartPlayerJniV2()
        playerHandle=player.SmartPlayerOpen(activity)
        if (playerHandle == 0L) {
            Log.e(TAG, "surfaceHandle with nil..")
            return null
        }
        player.SetSmartPlayerEventCallbackV2(playerHandle, ntSmartEventCallback)

        player.SmartPlayerSetBuffer(playerHandle, playBuffer)

        // set report download speed(默认5秒一次回调 用户可自行调整report间隔)
        // set report download speed(默认5秒一次回调 用户可自行调整report间隔)
        player.SmartPlayerSetReportDownloadSpeed(playerHandle, 1, 5)

        player.SmartPlayerSetFastStartup(playerHandle, if (isFastStartup) 1 else 0)
        if (isHardwareDecoder && isEnableHardwareRenderMode) {
            player.SmartPlayerSetHWRenderMode(playerHandle, 1)
        }
        player.SmartPlayerSetAudioOutputType(playerHandle, 0)


        player.SmartPlayerSetUrl(playerHandle, streamUrl)
        player.SmartPlayerSetSurface(playerHandle, surfaceView)
        player.SmartPlayerSetLowLatencyMode(playerHandle, if (isLowLatency) 1 else 0)

        if (isHardwareDecoder) {
            val isSupportHevcHwDecoder = player.SetSmartPlayerVideoHevcHWDecoder(playerHandle, 1)
            val isSupportH264HwDecoder = player.SetSmartPlayerVideoHWDecoder(playerHandle, 1)
            Log.i(TAG, "isSupportH264HwDecoder: $isSupportH264HwDecoder, isSupportHevcHwDecoder: $isSupportHevcHwDecoder")
        }
        player.SmartPlayerSetRotation(playerHandle, rotateDegress)
        player.SmartPlayerSetMute(playerHandle, if (booleanMute) 1 else 0)

        val iPlaybackRet = player
                .SmartPlayerStartPlay(playerHandle)

        if (iPlaybackRet != 0) {
            Log.e(TAG, "StartPlayback strem failed..")
            return null
        }
        return player
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d(TAG,"surfaceChanged--")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG,"surfaceDestroyed--")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (isHardwareDecoder && isEnableHardwareRenderMode && isPlaying) {
            Log.i(TAG, "UpdateHWRenderSurface..")
            if (mSurfaceView.holder === holder) {
                if (masterPlayer != null && masterPlayerHandle != 0L) {
                    masterPlayer!!.SmartPlayerUpdateHWRenderSurface(masterPlayerHandle)
                }
            }
            if (mSlaveSurfaceView != null && mSlaveSurfaceView.holder === holder) {
                if (slavePlayer != null && slavePlayerHandle != 0L) {
                    slavePlayer!!.SmartPlayerUpdateHWRenderSurface(slavePlayerHandle)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (masterPlayer != null){
            if (masterPlayerHandle != 0L) {
                if (isPlaying) {
                    masterPlayer!!.SmartPlayerStopPlay(masterPlayerHandle)
                }
                masterPlayer!!.SmartPlayerClose(masterPlayerHandle)
            }
            masterPlayerHandle = 0
        }

        if (slavePlayer != null) {
            if (slavePlayerHandle != 0L) {
                if (isPlaying) {
                    slavePlayer!!.SmartPlayerStopPlay(slavePlayerHandle)
                }
                slavePlayer!!.SmartPlayerClose(slavePlayerHandle)
            }
            slavePlayerHandle = 0
        }
        masterPlayer = null
        slavePlayer =null

    }

    override fun onPause() {
        if (masterPlayer != null && masterPlayerHandle != 0L) {
            masterPlayer!!.SmartPlayerSetMute(masterPlayerHandle, if (booleanMute) 1 else if (isCanPlayStream) 0 else if (isAudioRunInBg) 0 else 1)
        }
        if (slavePlayer != null && slavePlayerHandle != 0L) {
            slavePlayer!!.SmartPlayerSetMute(slavePlayerHandle, if (booleanMute) 1 else if (isCanPlayStream) 0 else if (isAudioRunInBg) 0 else 1)
        }
        super.onPause()
    }

    override fun onResume() {
        Log.d(TAG, "onResume--")
        if (masterPlayer != null && masterPlayerHandle != 0L) {
            masterPlayer!!.SmartPlayerSetMute(masterPlayerHandle, if (booleanMute) 1 else 0)
            if (isPlaying && !isHardwareDecoder || !isEnableHardwareRenderMode) {
                masterPlayer!!.SmartPlayerSetOrientation(masterPlayerHandle, if (isScreenPort) 1 else 2)
            }
        }
        if (slavePlayer != null && slavePlayerHandle != 0L) {
            slavePlayer!!.SmartPlayerSetMute(slavePlayerHandle, if (booleanMute) 1 else 0)
            if (isPlaying && !isHardwareDecoder || !isEnableHardwareRenderMode) {
                slavePlayer!!.SmartPlayerSetOrientation(slavePlayerHandle, if (isScreenPort) 1 else 2)
            }
        }
        super.onResume()
    }

    private var ntSmartEventCallback = NTSmartEventCallbackV2 { handle, id, param1, param2, param3, param4, param5 ->
        Log.i(TAG, "NTSmartEventCallbackV2: handle=$handle id:$id")
        var player_event = ""
        when (id) {
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STARTED -> player_event = "开始.."
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTING -> player_event = "连接中.."
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTION_FAILED -> player_event = "连接失败.."
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CONNECTED -> player_event = "连接成功.."
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_DISCONNECTED -> {
                player_event = "连接断开.."
//                mCover1.setImageResource(R.mipmap.video_load_failed_1)
            }
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP -> {
                player_event = "停止播放.."
//                mCover1.setImageResource(R.mipmap.video_load_failed_1)
            }
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RESOLUTION_INFO -> {
                player_event = "分辨率信息: width: $param1, height: $param2"
//                EventBus.getDefault().post(VideoLoadedEvent())
                activity?.runOnUiThread { // SurfaceView 重新 removew 再Add 回去，可以解决无法隐藏问题
                    if (handle == masterPlayerHandle) {
                        if (mSurfaceView.visibility != View.VISIBLE) {
                            mSurfaceView.visibility = View.VISIBLE
                        }
                        if (slaveLiveStreamUrl != null && slavePlayerHandle == 0L) {
                            if (mSlaveSurfaceView == null) {
                                mSlaveSurfaceView = createSurfaceView()
                                binding.flVideoStream.addView(mSlaveSurfaceView)
                                mSlaveSurfaceView.visibility = View.INVISIBLE
                                slavePlayer = initSmartPlayer(slaveLiveStreamUrl, mSlaveSurfaceView)
                                slavePlayerHandle = playerHandle
                            }
                        } else {
                            // 无侧面
                            //masterPlayer.SmartPlayerSetOrientation(masterPlayerHandle, ScreenUtils.isPortrait() ? 1 : 2);
//                            if (mCover1.getVisibility() == View.VISIBLE) {
//                                mCover1.setVisibility(View.INVISIBLE)
//                            }
                        }
                    }
                    if (handle == slavePlayerHandle && mSlaveSurfaceView != null && mSlaveSurfaceView.visibility != View.VISIBLE) {
                        masterPlayer!!.SmartPlayerSetOrientation(masterPlayerHandle, if (isScreenPort) 1 else 2)
                        mSlaveSurfaceView.visibility = View.VISIBLE
//                        if (mCover1.getVisibility() == View.VISIBLE) {
//                            mCover1.setVisibility(View.INVISIBLE)
//                        }
                    }
                }
            }
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_NO_MEDIADATA_RECEIVED -> player_event = "收不到媒体数据，可能是url错误.."
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_SWITCH_URL -> player_event = "切换播放URL.."
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_CAPTURE_IMAGE -> {
                player_event = "快照: $param1 路径：$param3"
                player_event = if (param1 == 0L) {
                    "$player_event, 截取快照成功"
                } else {
                    "$player_event, 截取快照失败"
                }
            }
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RECORDER_START_NEW_FILE -> player_event = "[record]开始一个新的录像文件 : $param3"
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_ONE_RECORDER_FILE_FINISHED -> player_event = "[record]已生成一个录像文件 : $param3"
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_START_BUFFERING -> Log.i(TAG, "Start Buffering")
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_BUFFERING -> Log.i(TAG, "Buffering:$param1%")
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_STOP_BUFFERING -> Log.i(TAG, "Stop Buffering")
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_DOWNLOAD_SPEED -> player_event = ("download_speed:" + param1 + "Byte/s" + ", "
                    + param1 * 8 / 1000 + "kbps" + ", " + param1 / 1024
                    + "KB/s")
            NTSmartEventID.EVENT_DANIULIVE_ERC_PLAYER_RTSP_STATUS_CODE -> {
                Log.e(TAG, "RTSP error code received, please make sure username/password is correct, error code:$param1")
                player_event = "RTSP error code:$param1"
            }
        }
        Log.i(TAG, "NTSmartEventCallbackV2: $player_event")
    }

}