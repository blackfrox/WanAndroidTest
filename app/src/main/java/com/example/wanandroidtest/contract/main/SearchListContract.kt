package com.example.wanandroidtest.contract.main

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData


/**
 * @author quchao
 * @date 2018/3/13
 */

interface SearchListContract {

    interface View : BaseView {

        /**
         * Show search list
         *
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showSearchList(feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show collect article data
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show cancel collect article data
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show search list fail
         */
        fun showSearchListFail()

    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * 搜索
         * @param page page
         * @param k POST search key
         */
        fun getSearchList(page: Int, k: String)

        /**
         * Add collect article
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         */
        fun addCollectArticle(position: Int, feedArticleData: FeedArticleData)

        /**
         * Cancel collect article
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         */
        fun cancelCollectArticle(position: Int, feedArticleData: FeedArticleData)

    }

}
