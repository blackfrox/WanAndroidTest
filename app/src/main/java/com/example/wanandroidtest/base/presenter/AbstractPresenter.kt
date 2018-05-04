package com.example.wanandroidtest.base.presenter

import com.example.wanandroidtest.base.BaseView

/**
 * Created by Administrator on 2018/4/20 0020.
 * 用于与View绑定生命周期，便于GC回收
 */
interface AbstractPresenter<V: BaseView> {

    fun attachView(view:V)

    fun detachView()

}