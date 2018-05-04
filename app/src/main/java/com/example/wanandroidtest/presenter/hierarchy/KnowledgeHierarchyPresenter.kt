package com.example.wanandroidtest.presenter.hierarchy

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.hierarchy.KnowledgeHierarchyContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class KnowledgeHierarchyPresenter @Inject constructor(val dataManager: DataManager)
    :BasePresenter<KnowledgeHierarchyContract.View>(), KnowledgeHierarchyContract.Presenter{

    override fun getKnowledgeHierarchyData() {
        addSubscribe(dataManager.getKnowledgeHierarchyData()
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showKnowledgeHierarchyData(it)
                    else
                        mView?.showKnowledgeHierarchyDetailDataFail()
                },{
                    showError(mView,it)
                }))
    }
}