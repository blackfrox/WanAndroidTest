package com.example.wanandroidtest.presenter.main

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.main.SearchListContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.component.event.CollectEvent
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/28.
 */
class SearchListPresenter @Inject
internal constructor(val dataManager: DataManager) : BasePresenter<SearchListContract.View>(), SearchListContract.Presenter {

    override fun attachView(view: SearchListContract.View) {
        super.attachView(view)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscribe(
                RxBus.instance.toFlowable(CollectEvent::class.java)
                        .subscribe {
                            if (!it.isCancelCollectSuccess)
                                mView?.showCollectSuccess()
                            else
                                mView?.showCancelCollectSuccess()
                        }
        )
    }

    override fun getSearchList(page: Int, k: String) {
        addSubscribe(dataManager.getSearchList(page, k)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode == BaseResponse.SUCCESS)
                        mView?.showSearchList(it)
                    else
                        mView?.showSearchListFail()
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
                        mView?.showCancelCollectFail()
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
                        mView?.showCollectArticleData(position, feedArticleData, it)
                    } else
                        mView?.showCancelCollectFail()
                }, {
                    showError(mView, it)
                }))
    }


}