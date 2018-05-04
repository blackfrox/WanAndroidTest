package com.example.wanandroidtest.presenter.main

import android.text.TextUtils
import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.main.LoginContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

class LoginPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<LoginContract.View>(),LoginContract.Presenter{

    override fun getLoginData(username: String, password: String) {
        addSubscribe(dataManager.getLoginData(username,password)
                .filter { checkEmpty(username)&&checkEmpty(password) }
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showLoginData(it)
                    else
                        mView?.showLoginFail()
                },{
                    showError(mView,it)
                }))
    }

    override fun getRegisterData(username: String, password: String, rePassword: String) {
        addSubscribe(dataManager.getRegisterData(username, password, rePassword)
                .filter { checkEmpty(username)&&checkEmpty(password)&&checkEmpty(rePassword) }
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showRegisterData(it)
                    else
                        mView?.showRegisterFail()
                },{
                    showError(mView,it)
                }))
    }

    private fun checkEmpty(text: String): Boolean {
        if (!TextUtils.isEmpty(text))
            return true
        else
            return false
    }
}