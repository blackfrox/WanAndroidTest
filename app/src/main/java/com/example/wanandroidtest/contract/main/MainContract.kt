package com.example.wanandroidtest.contract.main

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter

/**
 * Created by Administrator on 2018/4/23 0023.
 */
interface MainContract{
    interface View: BaseView{
        //跳转到ProjectFragment界面
        fun startProjectFragment()
        //跳转到NavigationFragment界面
        fun startNavigationFragment()

        //头布局的登陆/登出状态
        fun showLoginView()
        fun showLogoutView()

    }

    interface Presenter: AbstractPresenter<View>{

    }
}