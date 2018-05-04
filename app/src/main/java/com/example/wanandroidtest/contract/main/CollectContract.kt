package com.example.wanandroidtest.contract.main


import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData

/**
 * @author quchao
 * @date 2018/2/27
 */

interface CollectContract {

    interface View : BaseView {

        /**
         * Show collect list
         *
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCollectList(feedArticleListResponse: BaseResponse<FeedArticleListData>,isRefresh: Boolean)

        /**
         * Show cancel collect article data
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCancelCollectPageArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show collect list fail
         */
        fun showCollectListFail()

        /**
         * Show Refresh event
         */
        fun showRefreshEvent()

    }

    interface Presenter : AbstractPresenter<View> {

        fun refresh()

        fun loadMore()
        /**
         * Get collect list
         *
         * @param page page number
         */
        fun getCollectList(page: Int)

        /**
         * Cancel collect article
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         */
        fun cancelCollectPageArticle(position: Int, feedArticleData: FeedArticleData)

    }
}
