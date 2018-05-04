package com.example.wanandroidtest.widget

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import com.example.wanandroidtest.R
import kotlinx.android.synthetic.main.popup_window_register.view.*

class RegisterPopupWindow(activity: Activity): PopupWindow() {

    val account: String
    get() = view.register_account_edit.text.toString().trim()
    val password: String
    get() =view.register_password_edit.text.toString().trim()
    val rePassword:String
    get() = view.register_re_password_edit.text.toString().trim()
    val registerBtn: Button

    private val view: View
    init {
        view=LayoutInflater.from(activity).inflate(R.layout.popup_window_register,null)
        //设置按钮监听
        registerBtn=view.register_btn
        //设置外部可点击
        isOutsideTouchable=true
        //设置视图
        contentView=view

        /**
         * 设置弹出的窗体的宽高，获取圣诞框的窗口对象及参数对象以修改对话框的布局设置
         * 可以直接调用getWindow（),表示获得这个Activity的window
         *对象，这样就可以以同样的方式改变这个Activity的属性。
         */
        val dialogWindow=activity.window
        val m =activity.windowManager
        //获取屏幕的宽高
        val d=m.defaultDisplay
        //获取对话框当前的参数值
        val p=dialogWindow.attributes

        width= (d.width*0.7).toInt()
        height= (d.height*0.5).toInt()

        //设置弹出的窗体可点击
        isFocusable=true
    }

}