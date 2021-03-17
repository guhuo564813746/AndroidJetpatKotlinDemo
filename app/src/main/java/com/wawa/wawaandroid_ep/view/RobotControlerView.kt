package com.wawa.wawaandroid_ep.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.robotwar.app.R
import com.wawa.baselib.utils.logutils.LogUtils

/**
 *作者：create by 张金 on 2021/3/15 14:33
 *邮箱：564813746@qq.com
 */
class RobotControlerView( context: Context,
                        attributeSet: AttributeSet) : View(context,attributeSet){

    // 角度
    private val ANGLE_0 = 0.0
    private val ANGLE_360 = 360.0

    // 360°水平方向平分2份的边缘角度
    private val ANGLE_HORIZONTAL_2D_OF_0P = 90.0
    private val ANGLE_HORIZONTAL_2D_OF_1P = 270.0

    // 360°垂直方向平分2份的边缘角度
    private val ANGLE_VERTICAL_2D_OF_0P = 0.0
    private val ANGLE_VERTICAL_2D_OF_1P = 180.0

    // 360°平分4份的边缘角度
    private val ANGLE_4D_OF_0P = 0.0
    private val ANGLE_4D_OF_1P = 90.0
    private val ANGLE_4D_OF_2P = 180.0
    private val ANGLE_4D_OF_3P = 270.0

    // 360°平分4份的边缘角度(旋转45度)
    private val ANGLE_ROTATE45_4D_OF_0P = 45.0
    private val ANGLE_ROTATE45_4D_OF_1P = 135.0
    private val ANGLE_ROTATE45_4D_OF_2P = 225.0
    private val ANGLE_ROTATE45_4D_OF_3P = 315.0

    // 360°平分8份的边缘角度
    private val ANGLE_8D_OF_0P = 22.5
    private val ANGLE_8D_OF_1P = 67.5
    private val ANGLE_8D_OF_2P = 112.5
    private val ANGLE_8D_OF_3P = 157.5
    private val ANGLE_8D_OF_4P = 202.5
    private val ANGLE_8D_OF_5P = 247.5
    private val ANGLE_8D_OF_6P = 292.5
    private val ANGLE_8D_OF_7P = 337.5

    private val TAG="RobotControlerView"
    private var tempDirection = Direction.DIRECTION_CENTER
    var mOnAngleChangeListener: OnAngleChangeListener? = null
    var mOnShakeListener: OnShakeListener? = null
    private val DEFAULT_SIZE = 400
    private val DEFAULT_ROCKER_RADIUS = DEFAULT_SIZE / 8
    private var mCenterPoint: Point? = null

    init {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.RockerView)
        mCenterPoint=Point()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureWidth: Int
        val measureHeight: Int

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        measureWidth = if (widthMode == MeasureSpec.EXACTLY) {
            // 具体的值和match_parent
            widthSize
        } else {
            // wrap_content
            DEFAULT_SIZE
        }
        measureHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            DEFAULT_SIZE
        }
        Log.i(TAG, "onMeasure: --------------------------------------")
        Log.i(
            TAG,
            "onMeasure: widthMeasureSpec = $widthMeasureSpec heightMeasureSpec = $heightMeasureSpec"
        )
        Log.i(
            TAG,
            "onMeasure: widthMode = $widthMode  measureWidth = $widthSize"
        )
        Log.i(
            TAG,
            "onMeasure: heightMode = $heightMode  measureHeight = $widthSize"
        )
        Log.i(
            TAG,
            "onMeasure: measureWidth = $measureWidth measureHeight = $measureHeight"
        )
        setMeasuredDimension(measureWidth, measureHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight

        val cx = measuredWidth / 2
        val cy = measuredHeight / 2
        // 中心点
        // 中心点
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                LogUtils.d(TAG,"onTouchEvent--ACTION_DOWN")
                event?.let {
                    mCenterPoint?.set(it.x.toInt(), it.y.toInt())
                }
                callbackStart()
            }
            MotionEvent.ACTION_MOVE->{
                LogUtils.d(TAG,"onTouchEvent--ACTION_MOVE")
                var moveX=event?.x
                var moveY=event?.y
                callbackMove(Point(moveX?.toInt(),moveY?.toInt()))

            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                LogUtils.d(TAG,"onTouchEvent--ACTION_UP")
                callBackFinish()
            }
        }
        return true
    }

    fun callbackMove(movePoint: Point){
        //两点在X轴的距离
        var lenx: Float=0f
        //两点在Y轴的距离
        var lenY: Float=0f
        //两点之间的距离
        var lenXY: Float=0f
        var radian: Double=0.0
        var angle: Double=0.0
        mCenterPoint?.x?.let {
            lenx=(movePoint.x-it).toFloat()
        }
        mCenterPoint?.y?.let {
            lenY=(movePoint.y-it).toFloat()
        }
        lenXY= Math.sqrt((lenx * lenx + lenY * lenY).toDouble()).toFloat()
        mCenterPoint?.y?.let {
            radian=Math.acos(lenx / lenXY.toDouble()) * if (movePoint.y < it) -1 else 1
            angle=radian2Angle(radian)
        }
        LogUtils.d(TAG,"angle--$angle radian--$radian")
        mOnAngleChangeListener?.angle(angle)
        mOnShakeListener?.let {
            if (ANGLE_0 < angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) {
                // 右
                if (tempDirection != Direction.DIRECTION_RIGHT){
                    it.direction(Direction.DIRECTION_RIGHT,lenXY.toInt())
                    tempDirection=Direction.DIRECTION_RIGHT
                }
            } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle) {
                // 下
                if (tempDirection != Direction.DIRECTION_DOWN){
                    it.direction(Direction.DIRECTION_DOWN,lenXY.toInt())
                    tempDirection = Direction.DIRECTION_DOWN
                }

            } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle) {
                // 左
                if (tempDirection != Direction.DIRECTION_LEFT){
                    it.direction(Direction.DIRECTION_LEFT,lenXY.toInt())
                    tempDirection=Direction.DIRECTION_LEFT
                }

            } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle) {
                // 上
                if (tempDirection != Direction.DIRECTION_UP){
                    it.direction(Direction.DIRECTION_UP,lenXY.toInt())
                    tempDirection=Direction.DIRECTION_UP
                }
            }
        }

    }

    /**
     * 弧度转角度
     *
     * @param radian 弧度
     * @return 角度[0, 360)
     */
    private fun radian2Angle(radian: Double): Double {
        val tmp = Math.round(radian / Math.PI * 180).toDouble()
        return if (tmp >= 0) tmp else 360 + tmp
    }

    private fun callbackStart(){
        tempDirection = Direction.DIRECTION_CENTER
        mOnAngleChangeListener?.onStart()
        mOnShakeListener?.onStart()
    }

    /**
     * 回调
     * 结束
     */
    private fun callBackFinish() {
        tempDirection = Direction.DIRECTION_CENTER
        mOnAngleChangeListener?.onFinish()
        mOnShakeListener?.onFinish()
    }

    /**
     * 摇杆支持几个方向
     */
    enum class DirectionMode {
        DIRECTION_2_HORIZONTAL,  // 横向 左右两个方向
        DIRECTION_2_VERTICAL,  // 纵向 上下两个方向
        DIRECTION_4_ROTATE_0,  // 四个方向
        DIRECTION_4_ROTATE_45,  // 四个方向 旋转45度
        DIRECTION_8 // 八个方向
    }

    /**
     * 方向
     */
    enum class Direction {
        DIRECTION_LEFT,  // 左
        DIRECTION_RIGHT,  // 右
        DIRECTION_UP,  // 上
        DIRECTION_DOWN,  // 下
        DIRECTION_UP_LEFT,  // 左上
        DIRECTION_UP_RIGHT,  // 右上
        DIRECTION_DOWN_LEFT,  // 左下
        DIRECTION_DOWN_RIGHT,  // 右下
        DIRECTION_CENTER // 中间
    }

    /**
     * 摇动方向监听接口
     */
    interface OnShakeListener {
        // 开始
        fun onStart()

        /**
         * 摇动方向
         *
         * @param direction 方向
         */
        fun direction(direction: Direction?,distance: Int)

        // 结束
        fun onFinish()
    }

    /**
     * 摇动角度的监听接口
     */
    interface OnAngleChangeListener {
        // 开始
        fun onStart()

        /**
         * 摇杆角度变化
         *
         * @param angle 角度[0,360)
         */
        fun angle(angle: Double)

        // 结束
        fun onFinish()
    }
}