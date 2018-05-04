package com.example.wanandroidtest.util

import android.content.Context
import android.provider.Settings
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.example.wanandroidtest.app.GeeksApp


/**
 * @author quchao
 * @date 2017/11/27
 */

object ImageLoader {

    /**
     * 使用Glide加载圆形ImageView(如头像)时，不要使用占位图
     *
     * @param context context
     * @param url image url
     * @param iv imageView
     */
    fun load(context: Context, url: String, iv: ImageView) {
        if (!GeeksApp.appComponent.preferenceHelper.imageState) {
            Glide.with(context).load(url).into(iv)
        }
    }


}
