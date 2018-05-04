package com.example.wanandroidtest.presenter.project

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.project.ProjectContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/28.
 */
class ProjectPresenter @Inject
internal  constructor(val dataManager: DataManager): BasePresenter<ProjectContract.View>(), ProjectContract.Presenter{

    override fun getProjectClassifyData() {
        addSubscribe(dataManager.projectClassifyData
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showProjectClassifyData(it)
                    else
                        mView?.showProjectClassifyDataFail()
                },{
                  showError(mView,it)
                }))
    }

}