package com.example.wanandroidtest.presenter

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.navigation.NavigationContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/28.
 */
class NavigationPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<NavigationContract.View>(), NavigationContract.Presenter{

    override fun getNavigationListData() {
        addSubscribe(dataManager.navigationListData
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showNavigationListData(it)
                    else
                        mView?.showNavigationListFail()
                },{
                    showError(mView,it)
                }))
    }


}