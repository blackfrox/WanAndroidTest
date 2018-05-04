package com.example.wanandroidtest.base.presenter

import com.example.wanandroidtest.base.BaseView
import io.reactivex.annotations.NonNull
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 *  首先，项目中使用 Retrofit + RxJava2 进行网络请求，
 *  那么，我们在使用的时候就要考虑到 RxJava 的生命周期问题，如果不对它进行处理，
 *  那么在应用中进入一个 Activity、请求网络、在请求未完成返回这个流程中就会抛出异常，导致应用崩溃，
 *  于是，就有了这样的 BaseMVPPresenter
 * Created by Administrator on 2018/4/20 0020.
 */
abstract class BasePresenter<T: BaseView>: AbstractPresenter<T> {

    protected var mView: T?=null

    protected val compositeDisposable by lazy { CompositeDisposable() }

    protected fun addSubscribe(@NonNull vararg ds: Disposable){
        compositeDisposable.addAll(*ds)
    }

    override fun attachView(view: T) {
        mView=view
    }

    override fun detachView() {
        mView=null
        compositeDisposable.clear()
    }
}