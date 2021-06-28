package com.wawa.wawaandroid_ep.view.spanstring

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

/**
 *作者：create by 张金 on 2021/6/28 10:19
 *邮箱：564813746@qq.com
 */
class RoundBackgroudColorSpan(private val bgColor: Int,private val textColor: Int) : ReplacementSpan(){

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return paint.measureText(text, start, end) as Int + 60
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
        val color1: Int=paint.color
        paint.setColor(bgColor)
        canvas.drawRoundRect(RectF(x,top +1f,x+paint.measureText(text, start, end)+40,bottom-1f),15f,15f,paint)
        paint.setColor(textColor)
        text?.let {
            canvas.drawText(text,start,end,x+20f,y.toFloat(),paint)
        }
        paint.setColor(color1)
    }
}