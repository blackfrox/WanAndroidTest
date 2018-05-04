package com.example.wanandroidtest.util

import android.text.TextUtils
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.GeeksApp
import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.other.http.exception.ServerException
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException

/**
 * Created by Administrator on 2018/4/22 0022.
 */

/**
 * 统一线程处理
 * @param <T> 指定的泛型类型
 * @return ObservableTransformer
</T> */
fun <T>Observable<T>.applyScheduler(): Observable<T>{
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

/**
 *供Observable.onError()中调用
 */
fun showError(mView: BaseView?,e: Throwable,errorMsg: String?=null){
    if (mView==null)
        return
    when{
        errorMsg!=null &&!TextUtils.isEmpty(errorMsg) -> mView.showErrorMsg(errorMsg)
        e is ServerException -> mView.showErrorMsg(e.toString())
        e is HttpException -> mView.showErrorMsg(GeeksApp.instance.getString(R.string.http_error))
        else -> {
            mView.showErrorMsg(GeeksApp.instance.getString(R.string.unKnown_error))
        }
    }
}