package com.example.wanandroidtest.presenter.main

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.main.UsageDialogContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class UsageDialogPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<UsageDialogContract.View>(), UsageDialogContract.Presenter {

    override fun getUsefulSites() {
        addSubscribe(dataManager.usefulSites
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showUsefulSites(it)
                    else
                        mView?.showUsefulSitesDataFail()
                },{
                    showError(mView,it)
                }))
    }


}