package com.example.wanandroidtest.contract.mainpager

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.banner.BannerData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData

/**
 * Created by Administrator on 2018/4/22 0022.
 */
interface MainPagerContract {

    interface View : BaseView {
        fun showBannerData(response: BaseResponse<List<BannerData>>)

        fun showArticleList(response: BaseResponse<FeedArticleListData>,isRefresh: Boolean)

        fun showCollectArticleData(position: Int,feedArticleData: FeedArticleData,response: BaseResponse<FeedArticleListData>)
        fun showCancelCollectArticleData(position: Int,feedArticleData: FeedArticleData,response: BaseResponse<FeedArticleListData>)

        fun showAutoLoginSuccess()

        fun showArticleListFail()
        fun showBannerDataFail()
        fun showAutoLoginFail()

        fun jumpToTop()
    }

    interface Presenter: AbstractPresenter<View>{

        fun getBannerData()

        fun getFeedArticleList()

        //添加收藏
        fun addCollectArticle(position: Int,feedArticleData: FeedArticleData)
        //取消收藏
        fun cancelCollectArticle(position: Int,feedArticleData: FeedArticleData)

        fun refresh()

        fun loadMore()
    }
}