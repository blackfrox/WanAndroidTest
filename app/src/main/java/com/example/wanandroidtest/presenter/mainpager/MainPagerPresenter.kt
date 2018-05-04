package com.example.wanandroidtest.presenter.mainpager

import android.text.TextUtils
import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.mainpager.MainPagerContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.component.event.CollectEvent
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/22 0022.
 */
class MainPagerPresenter @Inject
internal constructor(val dataManager: DataManager) : BasePresenter<MainPagerContract.View>(), MainPagerContract.Presenter {

    private var isRefresh = true
    private var mCurrentPage = 0

    override fun attachView(view: MainPagerContract.View) {
        super.attachView(view)
        registerEvent() //注册event，直到调用post方法，才执行内部逻辑
    }

    //通过在fragment中调用 RxBus.getDefault().post(CollectEvent())
    //在这里进行逻辑处理，类似广播
    private fun registerEvent() {
        addSubscribe(
                //应该是detailActivity中收藏操作时调用
                RxBus.instance.toFlowable(CollectEvent::class.java)
                        .subscribe {
                            if (!it.isCancelCollectSuccess)
                                mView?.showCollectSuccess()
                            else
                                mView?.showCancelCollectSuccess()
                        })
        //LoginActivity通信调用
//                RxBus.instance.toFlowable(LoginEvent::class.java)
//                        .subscribe {
//                            if (it.isLogin)
//                                mView?.showLoginView()
//                            else
//                                mView?.showLogoutView()

    }

    //没有账号登录时候调用
    //mergeDelayError: 当其中一个observable出错时，还会继续执行 下一个observable;而不是像merge时中断不执行了
    override fun refresh() {
//        getLoginData()
        isRefresh = true
        mCurrentPage = 0
        getBannerData()
        getFeedArticleList()
    }

    private fun getLoginData(){
        with(dataManager) {
            if (!TextUtils.isEmpty(dataManager.loginAccount) &&
                    !TextUtils.isEmpty(dataManager.loginPassword))
                addSubscribe(getLoginData(loginAccount, loginPassword)
                        .applyScheduler()
                        .subscribe({
                            if (it.errorCode == BaseResponse.SUCCESS) {
                                with(it.data) {
                                    //保存账号和密码
                                    loginAccount = username
                                    loginPassword = password
                                    loginStatus = true
                                    mView?.showAutoLoginSuccess()
                                }
                            } else
                                mView?.showAutoLoginFail()
                        }, {
                            showError(mView, it)
                            mView?.showAutoLoginFail()
                        }))
        }
    }
    override fun loadMore() {
        isRefresh = false
        mCurrentPage++
        getFeedArticleList()
    }

    override fun getBannerData() {
        addSubscribe(dataManager.bannerData
                .applyScheduler()
                .subscribe({
                    if (it.errorCode == BaseResponse.SUCCESS)
                        mView?.showBannerData(it)
                    else
                        mView?.showBannerDataFail()
                }, {
                    showError(mView, it)
                }))
    }

    override fun getFeedArticleList() {
        addSubscribe(dataManager.getFeedArticleList(mCurrentPage)
                .applyScheduler()
                .filter { mView != null }
                .subscribe({
                    if (it.errorCode == BaseResponse.SUCCESS)
                        mView?.showArticleList(it, isRefresh)
                    else
                        mView?.showArticleListFail()
                }, {
                    showError(mView, it)
                }))
    }

    override fun addCollectArticle(position: Int, feedArticleData: FeedArticleData) {
        addSubscribe(dataManager.addCollectArticle(feedArticleData.id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode == BaseResponse.SUCCESS) {
                        feedArticleData.isCollect = true
                        mView?.showCollectArticleData(position, feedArticleData, it)
                    } else
                        mView?.showCollectFail(it.errorMsg)
                }, {
                    showError(mView, it)
                }))
    }

    override fun cancelCollectArticle(position: Int, feedArticleData: FeedArticleData) {
        addSubscribe(dataManager.cancelCollectArticle(feedArticleData.id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode == BaseResponse.SUCCESS) {
                        feedArticleData.isCollect = false
                        mView?.showCancelCollectArticleData(position, feedArticleData, it)
                    } else
                        mView?.showCancelCollectFail()
                }, {
                    showError(mView, it)
                }))
    }
}