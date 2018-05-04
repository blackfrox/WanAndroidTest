package com.example.wanandroidtest.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.example.wanandroidtest.R
import com.example.wanandroidtest.di.component.ActivityComponent
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

/**
 * Created by Administrator on 2018/4/23 0023.
 */

fun Fragment.checkNetwork(todo: ()-> Unit){
    if (CommonUtil.isNetworkConnected())
        todo
    else
        toast("网络连接失败请重新连接")
}
//检查 list数据是否为空
fun <T> Fragment.checkListIsEmpty(list: List<T>) {
    if (list.size == 0)
        CommonUtil.showSnackMessage(act, getString(R.string.no_more_data))
}

fun <T> Activity.checkListIsEmpty(list: List<T>) {
    if (list.size == 0)
        CommonUtil.showSnackMessage(this, getString(R.string.no_more_data))
}

/*      下面的方法没用到过，可以删除*/
fun Context.log(msg: String){
    Log.d(this.packageName,msg)
}

//检查权限
fun Activity.checkThePermission(permission: String,requestCode:Int){
    if (ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED)
        ActivityCompat.requestPermissions(this, arrayOf(permission),requestCode)
}