package com.example.wanandroidtest.contract.hierarchy


import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData

/**
 * @author quchao
 * @date 2018/2/23
 */

interface KnowledgeHierarchyListContract {

    interface View : BaseView {

        /**
         * Show Knowledge Hierarchy Detail Data
         *
         * @param response BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showKnowledgeHierarchyDetailData(response: BaseResponse<FeedArticleListData>, refresh: Boolean)

        /**
         * Show collect article data
         *
         * @param position                Position
         * @param feedArticleData         FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show cancel collect article data
         *
         * @param position                Position
         * @param feedArticleData         FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show knowledge hierarchy detail data fail
         */
        fun showKnowledgeHierarchyDetailDataFail()

        /**
         * Show jump the top
         */
        fun showJumpTheTop()

        /**
         * Show reload detail event
         */
        fun showReloadDetailEvent()

    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * 知识列表
         *
         * @param page page num
         * @param cid  second page id
         */
        fun getKnowledgeHierarchyDetailData(page: Int, cid: Int)

        fun refresh(cid: Int)

        fun loadMore(cid: Int)
        /**
         * Add collect article
         *
         * @param position        Position
         * @param feedArticleData FeedArticleData
         */
        fun addCollectArticle(position: Int, feedArticleData: FeedArticleData)

        /**
         * Cancel collect article
         *
         * @param position        Position
         * @param feedArticleData FeedArticleData
         */
        fun cancelCollectArticle(position: Int, feedArticleData: FeedArticleData)
    }
}
