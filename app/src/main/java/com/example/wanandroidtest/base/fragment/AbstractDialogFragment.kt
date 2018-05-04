package com.example.wanandroidtest.base.fragment

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Administrator on 2018/4/25 0025.
 */
abstract class AbstractDialogFragment : DialogFragment() {

    protected lateinit var mRootView: View
    private val compositeDisposable by lazy { CompositeDisposable() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(getLayoutId(), container, false)
        return mRootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    abstract fun initEvent(savedInstanceState: Bundle?)

    abstract fun getLayoutId(): Int
}