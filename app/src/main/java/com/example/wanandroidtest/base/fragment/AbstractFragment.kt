package com.example.wanandroidtest.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.GeeksApp
import com.example.wanandroidtest.util.CommonUtil
import io.reactivex.disposables.CompositeDisposable
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.support.v4.act

/**
 * Created by Administrator on 2018/4/21 0021.
 */
abstract class AbstractFragment: SupportFragment() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),container,false)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        //LeakCanary
      GeeksApp.refWatcher.watch(this)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initEvent(savedInstanceState)
    }


//    override fun onBackPressedSupport(): Boolean {
//        if (childFragmentManager.backStackEntryCount>1)
//            popChild()
//        else{
////            if (isInnerFragment){
////                activity.finish()
////                return true
////            }
//            //todo: 双击退出需要写在mainActivity中
////            var currentTime=System.currentTimeMillis()
////            val time=2000
////            if ((System.currentTimeMillis()-currentTime)>time){
////                CommonUtil.showSnackMessage(activity,getString(R.string.double_click_exit_tint))
////            }else
////                activity.finish()
//        }
//        return true
//    }

    //获取当前UI布局
    abstract fun getLayoutId(): Int
    //初始化数据
    abstract fun initEvent(savedInstanceState: Bundle?)


}