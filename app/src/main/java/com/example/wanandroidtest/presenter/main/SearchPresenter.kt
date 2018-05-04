package com.example.wanandroidtest.presenter.main

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.contract.main.SearchContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.util.applyScheduler
import com.example.wanandroidtest.util.showError
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/26.
 */
class SearchPresenter @Inject
internal constructor(val dataManager: DataManager): BasePresenter<SearchContract.View>(), SearchContract.Presenter{

    override fun addHistoryData(data: String) {
//        addSubscribe(Observable.create<List<HistoryData>> {
//            val mList=dataManager.addHistoryData(data)
//        }.applyScheduler().subscribe{
//            mView?.judgeToTheSearchListActivity()
//        })
        dataManager.addHistoryData(data)
        mView?.judgeToTheSearchListActivity(data)
    }

    override fun clearHistoryData() {
        dataManager.clearHistoryData()
    }

    override fun getTopSearchData() {
        addSubscribe(dataManager.topSearchData
                .applyScheduler()
                .subscribe({
                    if (it.errorCode==BaseResponse.SUCCESS)
                        mView?.showTopSearchData(it)
                    else
                        mView?.showTopSearchDataFail()
                },{
                    showError(mView,it)
                }))
    }
}