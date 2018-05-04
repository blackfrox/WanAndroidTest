package com.example.wanandroidtest.base

/**
 * Created by Administrator on 2018/4/20 0020.
 */
interface BaseView {
    /*        根据view的状态显示不同的View        */
//    fun showLoading(){
//
//    }
//
//    fun showError(){
//
//    }
//
//    fun showNormal(){
//
//    }
//
//    fun reload(){
//
//    }

    /*        根据view的状态显示不同的View        */
    //Rxjava2.onError()调用
    fun showErrorMsg(msg: String){

    }

        /* 收藏相关*/
    fun showCollectSuccess() {

    }

    fun showCollectFail(errorMsg: String) {

    }

    fun showCancelCollectSuccess() {

    }

    fun showCancelCollectFail() {

    }

    fun setNightMode(nightModeState: Boolean) {

    }
    /* 收藏相关*/

}