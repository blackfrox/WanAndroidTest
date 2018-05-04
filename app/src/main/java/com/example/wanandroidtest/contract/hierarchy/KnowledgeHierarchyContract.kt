package com.example.wanandroidtest.contract.hierarchy

import com.example.wanandroidtest.base.BaseView
import com.example.wanandroidtest.base.presenter.AbstractPresenter
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.hierarchy.KnowledgeHierarchyData

/**
 * Created by Administrator on 2018/4/25 0025.
 */
interface KnowledgeHierarchyContract{

    interface View : BaseView {

        /**
         * Show Knowledge Hierarchy Data
         *
         * @param knowledgeHierarchyResponse BaseResponse<List></List><KnowledgeHierarchyData>>
        </KnowledgeHierarchyData> */
        fun showKnowledgeHierarchyData(knowledgeHierarchyResponse: BaseResponse<MutableList<KnowledgeHierarchyData>>)

        /**
         * Show knowledge hierarchy detail data fail
         */
        fun showKnowledgeHierarchyDetailDataFail()

        fun jumpToTop()
    }

    interface Presenter : AbstractPresenter<View> {

        /**
         * 知识列表
         */
        fun getKnowledgeHierarchyData()
    }
}