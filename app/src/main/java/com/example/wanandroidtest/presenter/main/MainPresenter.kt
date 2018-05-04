package com.example.wanandroidtest.presenter.main

import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.GeeksApp
import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.component.event.LoginEvent
import com.example.wanandroidtest.component.event.NightModeEvent
import com.example.wanandroidtest.contract.main.MainContract
import com.example.wanandroidtest.component.event.SelectNavigationEvent
import com.example.wanandroidtest.component.event.SelectProjectEvent
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/23 0023.
 */
class MainPresenter @Inject internal constructor(): BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun attachView(view: MainContract.View) {
        super.attachView(view)
        registerEvent()
    }

    private fun registerEvent() {
       with(RxBus.instance){
           addSubscribe(toFlowable(SelectProjectEvent::class.java)
                   .subscribe { mView?.startProjectFragment() },
                   toFlowable(SelectNavigationEvent::class.java)
                           .subscribe { mView?.startNavigationFragment() },
                   toFlowable(LoginEvent::class.java)
                           .subscribe {
                               if(it.isLogin)
                                    mView?.showLoginView()
                               else
                                   mView?.showLogoutView()
                           }

//                   toFlowable(NightModeEvent::class.java)
//                           .subscribe({
//                               mView?.setNightMode(it.isNightMode)
//                           },{
//                               mView?.showErrorMsg(GeeksApp.instance.getString(R.string.failed_to_night_mode))
//                           })
           )
       }
    }

}