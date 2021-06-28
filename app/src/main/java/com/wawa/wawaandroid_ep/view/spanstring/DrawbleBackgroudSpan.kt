package com.wawa.wawaandroid_ep.view.spanstring

import android.graphics.*
import android.graphics.Typeface.NORMAL
import android.graphics.drawable.Drawable
import android.text.style.DrawableMarginSpan
import android.text.style.ImageSpan
import android.widget.TextView

/**
 *作者：create by 张金 on 2021/6/28 11:04
 *邮箱：564813746@qq.com
 */
class DrawbleBackgroudSpan(private val bg: Drawable,private val mTextView: TextView) : ImageSpan(bg){
    private var textSize=20f
    private val textColor=Color.GRAY
    private var textBoundHeight: Float=0f
    private var textY: Float=0f

    init {
        textSize= mTextView.textSize
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        //获取需要的字符串
        val  textContent=text?.subSequence(start,end).toString()
        //得到宽高
        var bounds=Rect()
        //设置字体的属性，大小
        paint.textSize==textSize
        //获得字符串所占空间大小
        paint.getTextBounds(textContent,0,textContent.length,bounds)
        //得到宽高
        val textHeight=bounds.height()
        val textWidth=bounds.width()
        //设置背景绘制宽高
        drawable.setBounds(0,(top*1.3).toInt(),(bounds.width()*1.3).toInt(),bottom)
        super.draw(canvas, textContent, start, end, x, top, y, bottom, paint)
        //绘制文本，文本颜色
        paint.setColor(mTextView.textColors.defaultColor)
        paint.setTypeface(Typeface.create("normal",NORMAL))
        //得到之前的背景图的大小
        val bound1=drawable.bounds
        //根据背景图算出字符串居中绘制的位置
        var textX: Float= x+bound1.width()/2 -bounds.width()/2
        if (textBoundHeight == 0f){
            textBoundHeight=bounds.height().toFloat()
            textY=bound1.height()/2 + textBoundHeight/2
        }
        canvas.drawText(textContent,textX,textY,paint)
    }

    override fun getDrawable(): Drawable {
        return super.getDrawable()
    }
}