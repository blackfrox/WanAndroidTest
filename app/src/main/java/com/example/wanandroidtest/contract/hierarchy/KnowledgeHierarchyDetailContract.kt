package com.example.wanandroidtest.contract.hierarchy

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter

/**
 * Created by Administrator on 2018/4/25 0025.
 */
interface KnowledgeHierarchyDetailContract {

    interface View : BaseView {

        /**
         * Show switch project
         */
        fun showSwitchProject()

        /**
         * Show switch navigation
         */
        fun showSwitchNavigation()
    }

    interface Presenter : AbstractPresenter<View>
}