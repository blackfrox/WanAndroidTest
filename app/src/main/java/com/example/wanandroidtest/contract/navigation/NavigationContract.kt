package com.example.wanandroidtest.contract.navigation

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.navigation.NavigationListData


/**
 * @author quchao
 * @date 2018/2/11
 */

interface NavigationContract {

    interface View : BaseView {

        /**
         * Show navigation list data
         *
         * @param navigationListResponse BaseResponse<List></List><NavigationListData>>
        </NavigationListData> */
        fun showNavigationListData(navigationListResponse: BaseResponse<List<NavigationListData>>)

        /**
         * Show navigation list fail
         */
        fun showNavigationListFail()
    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * Get navigation list data
         */
        fun getNavigationListData()
    }

}
