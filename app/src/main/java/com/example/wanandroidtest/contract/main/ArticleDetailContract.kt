package com.example.wanandroidtest.contract.main

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created by Administrator on 2018/4/24 0024.
 */
interface ArticleDetailContract {

    interface View: BaseView{

        fun showCollectArticleData(response: BaseResponse<FeedArticleListData>)

        fun showCancelCollectArticleData(response: BaseResponse<FeedArticleListData>)

        fun shareEvent()

        fun shareError()
    }

    interface Presenter: AbstractPresenter<View>{

        fun getAutoCacheState(): Boolean

        fun getNoImageState(): Boolean

        fun addCollectArticle(id: Int)

        fun cancelCollectArticle(id: Int)

        fun cancelCollectPageArticle(id: Int)

        fun shareEventPermissionVerify(rxPermissions: RxPermissions)
    }
}