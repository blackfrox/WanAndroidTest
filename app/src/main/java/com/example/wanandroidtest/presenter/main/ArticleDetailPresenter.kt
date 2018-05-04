package com.example.wanandroidtest.presenter.main

import android.Manifest
import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.main.ArticleDetailContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/24 0024.
 * 1 取消收藏
 * 2 点击收藏       需要访问网络进行同步
 */
class ArticleDetailPresenter @Inject internal constructor(val dataManager: DataManager)
    :BasePresenter<ArticleDetailContract.View>(), ArticleDetailContract.Presenter{

    override fun getAutoCacheState(): Boolean {
        return dataManager.autoCacheState
    }

    override fun getNoImageState(): Boolean {
        return dataManager.imageState
    }

    override fun addCollectArticle(id: Int) {
        addSubscribe(
                dataManager.addCollectArticle(id)
                        .applyScheduler()
                        .subscribe({
                            if (it.errorCode== BaseResponse.SUCCESS)
                                mView?.showCollectArticleData(it)
                            else
                                mView?.showCollectFail(it.errorMsg)
                        },{
                            showError(mView,it)
                        })
        )
    }

    override fun cancelCollectArticle(id: Int) {
        addSubscribe(dataManager.cancelCollectArticle(id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showCancelCollectArticleData(it)
                    else
                        mView?.showCancelCollectFail()
                },{
                    showError(mView,it)
                }))
    }

    override fun cancelCollectPageArticle(id: Int) {
        addSubscribe(dataManager.cancelCollectPageArticle(id)
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showCancelCollectArticleData(it)
                    else
                        mView?.showCancelCollectFail()
                },{
                    showError(mView,it)
                }))
    }

    override fun shareEventPermissionVerify(rxPermissions: RxPermissions) {
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it)
                        mView?.shareEvent()
                    else
                        mView?.shareError()
                }
    }
}