package com.example.wanandroidtest.ui.main

import android.os.Bundle
import android.text.TextUtils

import android.content.Intent
import android.view.Gravity
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.activity.BaseActivity
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.component.event.LoginEvent
import com.example.wanandroidtest.contract.main.LoginContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.login.LoginData
import com.example.wanandroidtest.presenter.main.LoginPresenter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.StatusBarUtil
import com.example.wanandroidtest.widget.RegisterPopupWindow
import com.jakewharton.rxbinding2.view.RxView

import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity<LoginPresenter>(),LoginContract.View {

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    @Inject
    lateinit var mDataManager: DataManager
    private val mPopupWindow: RegisterPopupWindow
            by lazy {  RegisterPopupWindow(this@LoginActivity) }
    override fun initEvent(savedInstanceState: Bundle?) {
        initToolbar()
        with(mPopupWindow){
            setOnDismissListener {
                setBackgroundAlpha()
            }
            registerBtn.setOnClickListener {
                register()
            }
        }
        signUpBtn.setOnClickListener {
            mPopupWindow.showAtLocation(login_group,Gravity.CENTER,0,0)
        }

        RxView.clicks(loginBtn)
                .throttleFirst(Constants.CLICK_TIME_AREA,TimeUnit.MILLISECONDS)
                .subscribe {
                    val account=login_account_edit.text.toString().trim()
                    val password=login_password_edit.text.toString().trim()
                    if (TextUtils.isEmpty(account)||TextUtils.isEmpty(password)){
                        CommonUtil.showSnackMessage(this,getString(R.string.account_password_null_tint))
                        return@subscribe
                    }
                    mPresenter.getLoginData(account,password)
                }

    }

    /**
     * 设置屏幕透明度
     */
    private fun setBackgroundAlpha() {
        val lp=window.attributes
        //0.0~1.0
        lp.alpha=1.0f
        window.attributes=lp
    }

    private fun register() {
        with(mPopupWindow){
            if (TextUtils.isEmpty(account)||TextUtils.isEmpty(password)||
                    TextUtils.isEmpty(rePassword)){
                toast(R.string.account_password_null_tint)
//                CommonUtil.showSnackMessage(this@LoginActivity,getString(R.string.account_password_null_tint))
                return
            }
            if (!password.equals(rePassword)){
                toast(R.string.password_not_same)
//                CommonUtil.showSnackMessage(this@LoginActivity,getString(R.string.password_not_same))
                return
            }
            mPresenter.getRegisterData(account,password,rePassword)
        }
    }

    override fun showLoginData(loginResponse: BaseResponse<LoginData>) {
        if(loginResponse.data==null){
            showLoginFail()
            return
        }
        val data=loginResponse.data
        with(mDataManager){
            loginAccount=data.username
            loginPassword=data.password
            loginStatus=true
        }
        //通知MainActivity更新NavVew的头布局
        RxBus.instance.post(LoginEvent(true))
        CommonUtil.showSnackMessage(this,getString(R.string.login_success))
        onBackPressed()
    }

    override fun showRegisterData(loginResponse: BaseResponse<LoginData>) {
        if (loginResponse.data==null){
            showRegisterFail()
            return
        }
        val data=loginResponse.data
        mPresenter.getLoginData(data.username,data.password)
    }

    override fun showLoginFail() {
        CommonUtil.showSnackMessage(this,getString(R.string.login_fail))
    }

    override fun showRegisterFail() {
        CommonUtil.showSnackMessage(this,getString(R.string.register_fail))
    }
    private fun initToolbar() {
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }
}
