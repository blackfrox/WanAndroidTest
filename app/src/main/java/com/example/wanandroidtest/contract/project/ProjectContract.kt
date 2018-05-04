package com.example.wanandroidtest.contract.project

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.project.ProjectClassifyData


/**
 * @author quchao
 * @date 2018/2/11
 */

interface ProjectContract {

    interface View : BaseView {

        /**
         * Show project classify data
         *
         * @param projectClassifyResponse List<ProjectClassifyData>
        </ProjectClassifyData> */
        fun showProjectClassifyData(projectClassifyResponse: BaseResponse<List<ProjectClassifyData>>)

        /**
         * Show project calssify data fail
         */
        fun showProjectClassifyDataFail()

    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * Get project classify data
         */
        fun getProjectClassifyData()
    }

}
