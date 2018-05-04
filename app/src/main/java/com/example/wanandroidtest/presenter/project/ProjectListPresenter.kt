package com.example.wanandroidtest.presenter.project

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.project.ProjectListContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/28.
 */
class ProjectListPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<ProjectListContract.View>(), ProjectListContract.Presenter{

    private var mCurrentPage =1
    private var isRefresh = true

    override fun refresh(cid: Int) {
        mCurrentPage=1
        isRefresh = true
        getProjectListData(mCurrentPage,cid)
    }

    override fun loadMore(cid: Int) {
        mCurrentPage++
        isRefresh= false
        getProjectListData(mCurrentPage,cid)
    }
    override fun getProjectListData(page: Int, cid: Int) {
        addSubscribe(dataManager.getProjectListData(page, cid)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showProjectListData(it,isRefresh)
                    else
                        mView?.showProjectListFail()
                },{
                    showError(mView,it)
                }))
    }

    override fun addCollectOutsideArticle(position: Int, feedArticleData: FeedArticleData) {
        addSubscribe(dataManager.addCollectOutsideArticle(feedArticleData?.title,feedArticleData?.author,
                feedArticleData?.link)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS){
                        feedArticleData.isCollect=true
                        mView?.showCollectOutsideArticle(position,feedArticleData,it)
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