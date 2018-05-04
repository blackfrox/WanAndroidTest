package com.example.wanandroidtest.presenter.hierarchy

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.hierarchy.KnowledgeHierarchyListContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.component.event.CollectEvent
import com.example.wanandroidtest.component.event.KnowledgeJumpTopEvent
import com.example.wanandroidtest.component.event.ReloadDetailEvent
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class KnowledgeHierarchyListPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<KnowledgeHierarchyListContract.View>(), KnowledgeHierarchyListContract.Presenter{

    private var isRefresh = true
    private var mCurrentPage=0

    override fun attachView(view: KnowledgeHierarchyListContract.View) {
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
                        },
                RxBus.instance.toFlowable(KnowledgeJumpTopEvent::class.java)
                        .subscribe { mView?.showJumpTheTop() },
                RxBus.instance.toFlowable(ReloadDetailEvent::class.java)
                        .subscribe { mView?.showReloadDetailEvent() }
        )
    }

    override fun refresh(cid: Int) {
        isRefresh = true
        mCurrentPage= 0
       getKnowledgeHierarchyDetailData(mCurrentPage,cid)
    }

    override fun loadMore(cid: Int) {
        isRefresh= false
        mCurrentPage++
        getKnowledgeHierarchyDetailData(mCurrentPage,cid)
    }

    override fun getKnowledgeHierarchyDetailData(page: Int, cid: Int) {
        addSubscribe(dataManager.getKnowledgeHierarchyDetailData(page,cid)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showKnowledgeHierarchyDetailData(it,isRefresh)
                    else
                        mView?.showKnowledgeHierarchyDetailDataFail()
                },{
                    showError(mView,it)
                }))
    }

    override fun addCollectArticle(position: Int, feedArticleData: FeedArticleData) {
        addSubscribe(dataManager.addCollectArticle(feedArticleData.id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS){
                        feedArticleData.isCollect=true
                        mView?.showCollectArticleData(position,feedArticleData,it)
                    }else
                        mView?.showCollectFail(it.errorMsg)
                },{
                    showError(mView,it)
                }))
    }

    override fun cancelCollectArticle(position: Int, feedArticleData: FeedArticleData) {
        addSubscribe(dataManager.cancelCollectArticle(feedArticleData.id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS){
                        feedArticleData.isCollect=false
                        mView?.showCancelCollectArticleData(position,feedArticleData,it)
                    }else
                        mView?.showCancelCollectFail()
                },{
                    showError(mView,it)
                }))
    }
}