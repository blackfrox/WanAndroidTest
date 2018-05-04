package com.example.wanandroidtest.base.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.example.wanandroidtest.R
import com.example.wanandroidtest.util.StatusBarUtil
import kotlinx.android.synthetic.main.toolbar.*
import me.yokeyword.fragmentation.SupportActivity

/**
 * Created by Administrator on 2018/4/20 0020.
 */
abstract class AbstractActivity: SupportActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initInject()
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        onViewCreated()
        initEvent(savedInstanceState)
    }


    //用于基类里添加事件
    open fun onViewCreated() {

    }

    abstract fun initEvent(savedInstanceState: Bundle?)

    abstract fun initInject()
    /**
     * 获取UI布局id
     */
    abstract fun getLayoutId(): Int

    /**
     * @param title  example (R.string.***)
     */
    protected fun initToolbar(title: Int) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        assert(actionBar != null)
        actionBar!!.setDisplayShowTitleEnabled(false)
        mTitleTv.text = getString(title)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
    }
}