package com.example.wanandroidtest.presenter.main

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.component.event.CollectEvent
import com.example.wanandroidtest.contract.main.CollectContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * dataManager相当于model
 *
 */
class CollectPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<CollectContract.View>(),CollectContract.Presenter{
    override fun attachView(view: CollectContract.View) {
        super.attachView(view)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscribe(RxBus.instance.toFlowable(CollectEvent::class.java)
                .subscribe { mView?.showRefreshEvent() })
    }

    private var mCurrentPager =0
    private var isRefresh = true
    override fun refresh(){
        mCurrentPager=0
        isRefresh = true
        getCollectList(mCurrentPager)
    }

    override fun loadMore(){
        mCurrentPager++
        isRefresh=false
        getCollectList(mCurrentPager)
    }
    override fun getCollectList(page: Int) {
        addSubscribe(dataManager.getCollectList(page)
                .applyScheduler()
                .subscribe({
                    if(it.errorCode==BaseResponse.SUCCESS)
                        mView?.showCollectList(it,isRefresh)
                    else
                        mView?.showCollectListFail()
                },{
                    showError(mView,it)
                }))
    }

    override fun cancelCollectPageArticle(position: Int, feedArticleData: FeedArticleData) {
        addSubscribe(dataManager.cancelCollectPageArticle(feedArticleData.id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS){
                        feedArticleData.isCollect=false
                        mView?.showCancelCollectPageArticleData(position,feedArticleData,it)
                    }else
                        mView?.showCancelCollectFail()
                },{
                    showError(mView,it)
                }))
    }
}