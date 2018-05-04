package com.example.wanandroidtest.contract.main


import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.login.LoginData

/**
 * @author quchao
 * @date 2018/2/26
 */

interface LoginContract {

    interface View : BaseView {

        /**
         * Show login data
         *
         * @param loginResponse BaseResponse<LoginData>
        </LoginData> */
        fun showLoginData(loginResponse: BaseResponse<LoginData>)

        /**
         * Show register data
         *
         * @param loginResponse BaseResponse<LoginData>
        </LoginData> */
        fun showRegisterData(loginResponse: BaseResponse<LoginData>)

        /**
         * Show login fail
         */
        fun showLoginFail()

        /**
         * Show register fail
         */
        fun showRegisterFail()

    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * Get Login data
         *
         * @param username user name
         * @param password password
         */
        fun getLoginData(username: String, password: String)

        /**
         * 注册
         * http://www.wanandroid.com/user/register
         *
         * @param username user name
         * @param password password
         * @param rePassword re password
         */
        fun getRegisterData(username: String, password: String, rePassword: String)
    }
}
