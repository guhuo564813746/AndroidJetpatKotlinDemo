package com.wawa.wawaandroid_ep.gamevideopager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.viewModels
import com.panda.wawajisdk.core.XHLiveManager
import com.panda.wawajisdk.core.XHLivePlayer
import com.panda.wawajisdk.core.listener.XHLivePlayerListener
import com.robotwar.app.BR
import com.robotwar.app.R
import com.robotwar.app.databinding.TxlivevideoLayBinding
import com.tencent.rtmp.TXLiveConstants
import com.tencent.rtmp.TXLiveConstants.LOG_LEVEL_NULL
import com.tencent.rtmp.TXLivePlayer
import com.tencent.rtmp.ui.TXCloudVideoView
import com.wawa.wawaandroid_ep.gamevideopager.viewmodel.TxLiveVideoControlorViewModel

class LiveGameFragment :
    BaseGameVideoControlor<TxlivevideoLayBinding, TxLiveVideoControlorViewModel>() {
    //@BindView(R.id.av_root_view) AVRootView mAVRootView;//播放器
    private var mIsVideo1LoadSuccess=false //视角1 是否加载成功 = false
    private var mIsVideo2LoadSuccess=false //视角2 是否加载成功 = false
    private var mIsVideo1 = true //是否是视角1
    private var mMasterLiveViewoView: TXCloudVideoView? = null
    private var mSlaveLiveViewoView: TXCloudVideoView? = null
    private var mGameUserLiveVideoView: TXCloudVideoView? = null
    private var mHeadImgView: ImageView? = null

    var mUserGroupView: RelativeLayout? = null
    private val xhLiveManager: XHLiveManager? = null
    private var xhLivePlayer: XHLivePlayer? = null
    private var mUserGroupMarginTop = 0


    override fun onPause() {
        super.onPause()
        if (xhLivePlayer != null) {
            xhLivePlayer?.setMute(if (isCanPlayStream) false else if (isAudioRunInBg) booleanMute else true)
        }
    }

    override fun onResume() {
        super.onResume()
        if (xhLivePlayer != null) {
            xhLivePlayer?.setMute(booleanMute) //isMute
        }
    }

    fun setLiveStreamUrl(masterUrl: String?, slaveUrl: String?) {
        val masterChanged = masterUrl != null && masterUrl != masterLiveStreamUrl
        val slaveChanged = slaveUrl != null && slaveUrl == slaveLiveStreamUrl
        if (xhLivePlayer != null && (masterChanged || slaveChanged)) {
            xhLivePlayer?.pause()
            xhLivePlayer?.play(
                masterLiveStreamUrl,
                slaveLiveStreamUrl,
                TXLivePlayer.PLAY_TYPE_LIVE_RTMP
            )
        }
    }

    /**
     * 切换摄像头
     */
    override fun switchCamera() {
        //切换视角
        if (false) { // xhLiveManager.isEnterRoom()
            //xhLiveManager.switchCamera();
            mIsVideo1 = !mIsVideo1
        } else if (switchCamera) {
            XHLivePlayer.getInstance().switchCamera()
        }
    }

    /**
     * 显示连麦的小窗口
     */
    override fun showLinkView() {
        //xhLiveManager.showOtherView(true);
        xhLivePlayer?.showOtherView(true)
        if (mUserGroupView != null) {
            mUserGroupView?.setVisibility(View.VISIBLE)
        }
        /**
         * if (mAVRootView != null) {
         * AVVideoView videoView = mAVRootView.getViewByIndex(2);
         * if (videoView != null && videoView.hasVideo()) {
         * videoView.setVisibility(GLView.VISIBLE);
         * }
         * }
         */
    }

    /**
     * 隐藏连麦的小窗口
     */
    override fun hideLinkView() {
        //xhLiveManager.showOtherView(false);
        xhLivePlayer?.showOtherView(false)
        if (mUserGroupView != null) {
            mUserGroupView?.setVisibility(View.INVISIBLE)
        }
        /**
         * if (mAVRootView != null) {
         * AVVideoView videoView = mAVRootView.getViewByIndex(2);
         * if (videoView != null && videoView.hasVideo()) {
         * videoView.setVisibility(GLView.INVISIBLE);
         * }
         * }
         */
    }

    override fun joinLiveRoom() {
        /**
         * 注释互动直播代码
         * if (xhLiveManager.isEnterRoom()) {
         * return;
         * }
         * try {
         * xhLiveManager.joinRoom(Integer.parseInt(mLiveBean.getNumber()), mLiveBean.getStream_one(), mLiveBean.getStream_two(), mAVRootView, new XHLiveJoinRoomListener(){
         * @Override
         * public void onSuccess(Object data) {
         * L.e("LiveGameFragment", "joinLiveRoom Success");
         * }
         * @Override
         * public void onError(String module, int errCode, String errMsg) {
         * L.e("LiveGameFragment", "joinLiveRoom Error");
         * }
         * @Override
         * public void onSubViewCreated(AVRootView avRoot){
         * // 处理回调视频视图
         * }
         * @Override
         * public void onFirstFrameRecved(AVVideoView v, int position, int width, int height, int angle, String identifier){
         *
         * }
         * @Override
         * public void onHasVideo(AVVideoView v, int position, String identifier, int srcType){
         * onVideoLoaded(position, true, 0);
         * }
         * @Override
         * public void onNoVideo(AVVideoView v, int position, String identifier, int srcType){
         * onVideoLoaded(position, false, 0);
         * }
         * });
         * } catch (Exception e) {
         * e.printStackTrace();
         * }
         */
    }

    override fun quitLiveRoom() {
        /**
         * if (! xhLiveManager.isEnterRoom()) {
         * return;
         * }
         * xhLiveManager.quitRoom(new XHLiveListener(){
         * @Override
         * public void onSuccess() {
         * }
         * @Override
         * public void onError(String module, int errCode, String errMsg) {
         * }
         * });
         */
    }

    override fun setRotation(rotation: Int) {
        super.setRotation(rotation)
        XHLivePlayer.getInstance().setRotation(360 - rotation).setLogPrint(false, LOG_LEVEL_NULL)
    }

    override fun setMute(mute: Boolean) {
        super.setMute(mute)
        XHLivePlayer.getInstance().setMute(mute)

    }

    override fun destroy() {
        /**
         * if (xhLiveManager != null) {
         * xhLiveManager.quitRoom(new XHLiveListener(){
         * @Override
         * public void onSuccess() {
         * }
         * @Override
         * public void onError(String module, int errCode, String errMsg) {
         * }
         * });
         * xhLiveManager.onDestory();
         * }
         */
        if (xhLivePlayer != null) {
            xhLivePlayer?.destroy()
        }
        //mAVRootView = null;
        mMasterLiveViewoView = null
        mSlaveLiveViewoView = null
        mGameUserLiveVideoView = null
        mHeadImgView = null
        mUserGroupView = null
        Log.e("LiveGameFragment", "-----destroy---->释放资源成功")
    }

    override fun upToVideoMember() {
        /**
         * if (!xhLiveManager.isEnterRoom()) {
         * L.e(TAG, "未进入房间");
         * return;
         * }
         * xhLiveManager.upToVideoMember(null);
         */
    }

    override fun downToVideoMember() {
        /**
         * if (!xhLiveManager.isEnterRoom()) {
         * L.e(TAG, "未进入房间");
         * return;
         * }
         * xhLiveManager.downToVideoMember(null);
         */
    }

    override fun gameUserLivePlayerPlay(
        url: String?,
        avatar: String?
    ) {
        xhLivePlayer?.userPlay(url)
    }

    override fun gameUserLivePlayerStop() {
        if (mHeadImgView != null) {
            mHeadImgView?.setVisibility(View.INVISIBLE)
        }
        xhLivePlayer?.userStop()
    }
    /**********************
     * 腾讯sdk回调
     */
    /**
     * 视频加载完成
     *
     * @param index
     * @param isLoaded
     * @param cost
     */
    fun onVideoLoaded(index: Int, isLoaded: Boolean, cost: Int) {
        if (index == 0) { //视角1
            mIsVideo1LoadSuccess = isLoaded
            if (mIsVideo1LoadSuccess) { //加载成功
//                EventBus.getDefault().post(VideoLoadedEvent())
            } else {

            }
        } else if (index == 1) {
            mIsVideo2LoadSuccess = isLoaded
            if (mIsVideo2LoadSuccess) { //加载成功
//                EventBus.getDefault().post(VideoLoadedEvent())
            } else {
            }
        }
    }

    /**
     * 初始化旁路 Player
     */
    private fun initLivePlayer() {
        val player: XHLivePlayer = XHLivePlayer.getInstance()
        xhLivePlayer = player
        player.setVideoView(mMasterLiveViewoView, mSlaveLiveViewoView)
        player.setUserView(mGameUserLiveVideoView, mHeadImgView)
        player.initPlayer(activity, masterLiveStreamUrl, slaveLiveStreamUrl)
        player.initUserPlayer()
        player.setMute(booleanMute)
        player.setListener(object : XHLivePlayerListener {
            override fun onPlayEvent(index: Int, event: Int, param: Bundle?) {
                Log.e("onPlayEvent","TXLivePlayer onPlayEvent index: $index  event:$event")
                when (event) {
                    TXLiveConstants.PLAY_ERR_NET_DISCONNECT -> {
                    }
                    TXLiveConstants.PLAY_EVT_CONNECT_SUCC -> {
                    }
                    TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN -> {
                    }
                    TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME -> {
                    }
                    TXLiveConstants.PLAY_EVT_PLAY_BEGIN -> {
                        xhLivePlayer?.setLogPrint(false, LOG_LEVEL_NULL)
                        if (index == 0) {
//                            EventBus.getDefault().post(VideoLoadedEvent())
                        } else if (index == 1) {
//                            EventBus.getDefault().post(VideoLoadedEvent())
                        } else if (index == 2) {
                            player.showUserView(true)
                        }
                    }
                    TXLiveConstants.PLAY_EVT_PLAY_LOADING -> {
                    }
                    TXLiveConstants.PLAY_EVT_PLAY_PROGRESS -> {
                    }
                    else -> {
                    }
                }
//                if (listener is XHLivePlayerListener) {
//                    (listener as XHLivePlayerListener).onPlayEvent(index, event, param)
//                }
            }

            override fun onNetStatus(position: Int, bundle: Bundle?) {
                val str: String? = xhLivePlayer?.getNetStatusString(bundle)
                Log.e("onNetStatus","TXLivePlayer onNetStatus : $str")
//                if (listener is XHLivePlayerListener) {
//                    (listener as XHLivePlayerListener).onNetStatus(position, bundle)
//                }
            }
        })
        player.setRotation(360 - mRotation)
        player.play()
    }

    /**
     * 初始化互动直播
     */
    private fun initILive() {
        // 互动直播
        //xhLiveManager = XHLiveManager.getInstance();
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewModel(): TxLiveVideoControlorViewModel {
        val txLiveVideoControlorViewModel: TxLiveVideoControlorViewModel by viewModels()
        return txLiveVideoControlorViewModel
    }

    override fun initFragmentView() {
        masterLiveStreamUrl=arguments?.getString(MASTER_VIDEO_URL)
//        masterLiveStreamUrl="rtmp://liveplay.wowgotcha.com/mlb/wowgotcha_500994_1"
        slaveLiveStreamUrl= arguments?.getString(SLAVE_VIDEO_URL)
        switchCamera = false
        mMasterLiveViewoView =binding.masterLiveVideoView
        mSlaveLiveViewoView =binding.slaveLiveVideoView
        mGameUserLiveVideoView =binding.playerLiveVideoView
        if (mUserGroupMarginTop > 0) {
            val relativeParams: FrameLayout.LayoutParams =
                mUserGroupView?.getLayoutParams() as FrameLayout.LayoutParams
            relativeParams?.setMargins(0, mUserGroupMarginTop, 0, 0)
            mUserGroupView?.setLayoutParams(relativeParams)
        }
        initLivePlayer()
        initILive()
    }

    companion object {
        private const val TAG = "LiveGameFragment"
    }

    override fun getLayoutId(): Int {
        return R.layout.txlivevideo_lay
    }
}