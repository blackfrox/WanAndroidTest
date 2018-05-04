package com.example.wanandroidtest.presenter.hierarchy

import com.example.wanandroidtest.base.presenter.BasePresenter
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.hierarchy.KnowledgeHierarchyDetailContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.component.event.SelectNavigationEvent
import com.example.wanandroidtest.component.event.SelectProjectEvent
import javax.inject.Inject


/**
 * @author quchao
 * @date 2018/2/23
 */

class KnowledgeHierarchyDetailPresenter @Inject
internal constructor(private val mDataManager: DataManager) : BasePresenter<KnowledgeHierarchyDetailContract.View>(), KnowledgeHierarchyDetailContract.Presenter {


    override fun attachView(view: KnowledgeHierarchyDetailContract.View) {
        super.attachView(view)
        registerEvent()
    }

    private fun registerEvent() {
        addSubscribe(
                RxBus.instance.toFlowable(SelectProjectEvent::class.java)
                        .subscribe { mView?.showSwitchProject() },
                RxBus.instance.toFlowable(SelectNavigationEvent::class.java)
                        .subscribe { mView?.showSwitchNavigation()}
        )
    }


}
