//package com.example.wanandroidtest.base.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.wanandroidtest.R
import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.BasePresenter
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.loading_view.*
import org.jetbrains.anko.support.v4.find

/**
 * 因为BasePresenter中括号里的泛型不能省略，有冲突，所以还是用java写了
 *三种状态显示不同的View(注:UI的父布局id必须为normal_view)
 * 将loadingView和errorView动态添加到布局中
 * Created by Administrator on 2018/4/22 0022.
 */
//abstract class AbstractRootFragment<T: BasePresenter<BaseView>>: BaseFragment<T>() {

//    private val STATE_NORMAL=0
//    private val STATE_LOADING=1
//    private val STATE_ERROR=2
//
//    private var currentState=STATE_NORMAL
//    private  var mNormalView: ViewGroup?=null
//
//    override fun initEvent(savedInstanceState: Bundle?) {
//        mNormalView=find(R.id.normal_view)
//        if (mNormalView==null)
//            throw IllegalStateException("The subclass of RootActivity must contain a View named normal_view")
//        //原本这里的normal_view还有一个父布局，
//        View.inflate(activity,R.layout.loading_view,mNormalView)
//        View.inflate(activity,R.layout.error_view,mNormalView)
//        error_reload_tv.setOnClickListener { reload() }
//        error_group.Gone()
//        loading_group.Gone()
//        mNormalView?.Visiblie()
//    }
//
//    override fun showLoading() {
//        if (currentState==STATE_LOADING)
//            return
//        hideCurrentView()
//        currentState=STATE_LOADING
//        loading_group.Visiblie()
//        loading_animation.setAnimation("loading_bus.json")
//        loading_animation.playAnimation()
//    }
//
//    override fun showError() {
//        if (currentState==STATE_ERROR)
//            return
//        hideCurrentView()
//        currentState=STATE_ERROR
//        error_group.Visiblie()
//    }
//
//    override fun showNormal() {
//        if (currentState==STATE_NORMAL)
//            return
//        hideCurrentView()
//        currentState=STATE_NORMAL
//        mNormalView?.Visiblie()
//    }
//
//    private fun hideCurrentView(){
//        when(currentState){
//            STATE_NORMAL -> mNormalView?.InVisible()
//            STATE_LOADING -> {
//                loading_animation.cancelAnimation()
//                loading_group.InVisible()
//            }
//            STATE_ERROR -> error_group.Gone()
//        }
//    }
//    /*    以下三个是kotlin的扩展函数(作用：方便书写，减少代码量)*/
//    protected fun View.Gone(){
//        visibility=View.GONE
//    }
//
//    protected fun View.Visiblie(){
//        visibility=View.VISIBLE
//    }
//
//    protected fun View.InVisible(){
//        visibility=View.INVISIBLE
//    }
//    abstract fun reload()
//}