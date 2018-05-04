package com.example.wanandroidtest.contract.main

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.search.TopSearchData
import com.example.wanandroidtest.other.db.HistoryData


/**
 * @author quchao
 * @date 2018/2/12
 */

interface SearchContract {

    interface View : BaseView {

        /**
         * Show history data
         *
         * @param historyDataList List<HistoryData>
        </HistoryData> */
        fun showHistoryData(historyDataList: List<HistoryData>)

        /**
         * Show top search data
         *
         * @param topSearchDataResponse BaseResponse<List></List><TopSearchData>>
        </TopSearchData> */
        fun showTopSearchData(topSearchDataResponse: BaseResponse<MutableList<TopSearchData>>)

        /**
         * Show top search data fail
         */
        fun showTopSearchDataFail()

        /**
         * Judge to the search list activity
         */
        fun judgeToTheSearchListActivity(text: String)

    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * Add history data
         *
         * @param data history data
         */
        fun addHistoryData(data: String)

        /**
         * 热搜
         */
        fun getTopSearchData()

        /**
         * Clear history data
         */
        fun clearHistoryData()
    }

}
