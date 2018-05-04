package com.example.wanandroidtest.contract.main

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.search.UsefulSiteData


/**
 * @author quchao
 * @date 2018/4/2
 */

interface UsageDialogContract {

    interface View : BaseView {

        /**
         * Show useful sites
         *
         * @param usefulSitesResponse BaseResponse<List></List><UsefulSiteData>>
        </UsefulSiteData> */
        fun showUsefulSites(usefulSitesResponse: BaseResponse<MutableList<UsefulSiteData>>)

        /**
         * Show useful sites data fail
         */
        fun showUsefulSitesDataFail()
    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * 常用网站
         */
        fun getUsefulSites()
    }

}
