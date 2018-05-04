package com.example.wanandroidtest.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.getColor
import android.widget.TextView
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.app.GeeksApp
import java.util.*

/**
 * Created by Administrator on 2018/4/20 0020.
 */
object CommonUtil {

    /**
     * 检查是否有可用网络
     */
    fun isNetworkConnected(): Boolean {
        val connectivityManager = GeeksApp.instance.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }

    fun showSnackMessage(activity: Activity, message: CharSequence) {
        val snackbar=Snackbar.make(activity.window.decorView,message,Snackbar.LENGTH_SHORT)
        snackbar.view.findViewById<TextView>(R.id.snackbar_text)
                .setTextColor(getColor(activity,R.color.white))
        snackbar.show()
    }

    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(): Int {
        val random = Random()
        //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
        val red = random.nextInt(150)
        //0-190
        val green = random.nextInt(150)
        //0-190
        val blue = random.nextInt(150)
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }

    fun randomTagColor(): Int {
        val randomNum = Random().nextInt()
        var position = randomNum % Constants.TAB_COLORS.size
        if (position < 0) {
            position = -position
        }
        return Constants.TAB_COLORS[position]
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Float): Int {
        val scale = GeeksApp.instance.getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }
}