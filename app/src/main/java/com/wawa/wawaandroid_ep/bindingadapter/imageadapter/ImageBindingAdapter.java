package com.wawa.wawaandroid_ep.bindingadapter.imageadapter;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

/**
 * 作者：create by 张金 on 2021/3/9 10:29
 * 邮箱：564813746@qq.com
 */
public final class ImageBindingAdapter {
    @BindingAdapter("android:src")
    public static void imageResource(ImageView img,Integer src){
        img.setImageResource(src);
    }

}
