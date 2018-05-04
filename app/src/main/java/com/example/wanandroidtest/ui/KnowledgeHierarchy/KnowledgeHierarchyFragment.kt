package com.example.wanandroidtest.ui.KnowledgeHierarchy

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.contract.hierarchy.KnowledgeHierarchyContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.hierarchy.KnowledgeHierarchyData
import com.example.wanandroidtest.presenter.hierarchy.KnowledgeHierarchyPresenter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.checkListIsEmpty
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_knowledge_hierarchy.*
import org.jetbrains.anko.support.v4.find
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class KnowledgeHierarchyFragment : AbstractRootFragment<KnowledgeHierarchyPresenter>(), KnowledgeHierarchyContract.View {
    companion object {
        fun getInstance(): KnowledgeHierarchyFragment {
            val fragment=KnowledgeHierarchyFragment()
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_knowledge_hierarchy
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    @Inject
    lateinit var dataManager: DataManager
    private val mAdapter by lazy { KnowledgeHierarchyAdapter(R.layout.item_knowledge_hierarchy,arrayListOf<KnowledgeHierarchyData>()) }
    private val mRefreshLayout by lazy { find<SmartRefreshLayout>(R.id.normal_view) }
    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)
        initRefresh()
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val options= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ActivityOptions.makeSceneTransitionAnimation(activity,view,getString(R.string.share_view))
            else null
            val intent= Intent(activity,KnowledgeHierarchyDetailActivity::class.java)
            intent.putExtra(Constants.ARG_PARAM1,mAdapter.data[position])
            startActivity(intent, options?.toBundle())
        }
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager=LinearLayoutManager(activity)

        mPresenter.getKnowledgeHierarchyData()
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }

    override fun showKnowledgeHierarchyData(knowledgeHierarchyResponse: BaseResponse<MutableList<KnowledgeHierarchyData>>) {
        if (knowledgeHierarchyResponse.data==null){
            showKnowledgeHierarchyDetailDataFail()
            return
        }
        val list=knowledgeHierarchyResponse.data
        checkListIsEmpty(list)
        mAdapter.replaceData(list)
        showNormal()
    }

    override fun showKnowledgeHierarchyDetailDataFail() {
        CommonUtil.showSnackMessage(activity!!,getString(R.string.failed_to_obtain_knowledge_data))
    }

    override fun reload() {
        if (recyclerView.visibility==View.VISIBLE)
            mPresenter.getKnowledgeHierarchyData()
    }

    override fun jumpToTop(){
        recyclerView.smoothScrollToPosition(0)
    }

    private fun initRefresh(){
        with(mRefreshLayout){
            setPrimaryColorsId(Constants.BLUE_THEME,R.color.white)
            setOnRefreshListener {
                it.layout.postDelayed({
                    mPresenter.getKnowledgeHierarchyData()
                    finishRefresh(1000)
                },1000)
            }
        }
    }


}