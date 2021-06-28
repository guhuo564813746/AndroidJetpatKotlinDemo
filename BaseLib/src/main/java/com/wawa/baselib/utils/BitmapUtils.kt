package com.wawa.baselib.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

/**
 *作者：create by 张金 on 2021/6/28 15:37
 *邮箱：564813746@qq.com
 */
class BitmapUtils() {
    fun createViewBitmap(view: View): Bitmap{
        val bitmap=Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas: Canvas= Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}