package com.example.wanandroidtest.contract.project


import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.other.bean.project.ProjectListData

/**
 * @author quchao
 * @date 2018/2/24
 */

interface ProjectListContract {

    interface View : BaseView {

        /**
         * Show project list data
         *
         * @param response BaseResponse<ProjectListData>
        </ProjectListData> */
        fun showProjectListData(response: BaseResponse<ProjectListData>, isRefresh: Boolean)

        /**
         * Show article list
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCollectOutsideArticle(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show cancel collect article data
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         * @param feedArticleListResponse BaseResponse<FeedArticleListData>
        </FeedArticleListData> */
        fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>)

        /**
         * Show project list fail
         */
        fun showProjectListFail()

        /**
         * Show jump to the top
         */
        fun showJumpToTheTop()

    }

    interface Presenter : AbstractPresenter<View> {
        fun refresh(cid: Int)

        fun loadMore(cid: Int)

        /**
         * Get project list data
         *
         * @param page page num
         * @param cid second page id
         */
        fun getProjectListData(page: Int, cid: Int)

        /**
         * Add collect outside article
         *收藏网站之外的文章
         * @param position Position
         * @param feedArticleData FeedArticleData
         */
        fun addCollectOutsideArticle(position: Int, feedArticleData: FeedArticleData)

        /**
         * Cancel collect article
         *
         * @param position Position
         * @param feedArticleData FeedArticleData
         */
        fun cancelCollectArticle(position: Int, feedArticleData: FeedArticleData)

    }

}
