package com.example.wanandroidtest.ui.project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.contract.project.ProjectListContract
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.other.bean.project.ProjectListData
import com.example.wanandroidtest.presenter.project.ProjectListPresenter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.example.wanandroidtest.util.checkListIsEmpty
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_project_list.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.find

/**
 * Created by Administrator on 2018/4/28.
 */
class ProjectListFragment: AbstractRootFragment<ProjectListPresenter>(), ProjectListContract.View {

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project_list
    }

    private val mRefreshLayout by lazy { find<SmartRefreshLayout>(R.id.normal_view) }
    private val mAdapter: ProjectListAdapter by lazy { ProjectListAdapter(R.layout.item_project_list, arrayListOf<FeedArticleData>()) }
    private var cid = 0
    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)
        initRefreshLayout()
        arguments
        cid=arguments!!.getInt(Constants.ARG_PARAM1)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            with(mAdapter.data[position]){
                JudgeUtils.startArticleDetailActivity(act,
                        null,
                        id,
                        title.trim(),
                        link.trim(),
                        isCollect,
                        false,
                        true)
            }
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.item_project_list_install_tv -> {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mAdapter.data[position].apkLink)))
                }
            }
        }
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager=LinearLayoutManager(activity)

        mPresenter.refresh(cid)
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }

    override fun reload() {
        mPresenter.refresh(cid)
    }
    override fun showProjectListData(response: BaseResponse<ProjectListData>, isRefresh: Boolean) {
        if (response.data==null|| response.data.datas==null) {
            showProjectListFail()
            return
        }
       val mDatas= response.data.datas
        checkListIsEmpty(mDatas)
        if (isRefresh){
            mAdapter.replaceData(mDatas)
        }else
            mAdapter.addData(mDatas)
        showNormal()
    }


    override fun showCollectOutsideArticle(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position,feedArticleData)
        CommonUtil.showSnackMessage(act,getString(R.string.collect_success))
    }

    override fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position,feedArticleData)
        CommonUtil.showSnackMessage(act,getString(R.string.cancel_collect_success))
    }

    override fun showProjectListFail() {
        CommonUtil.showSnackMessage(act,getString(R.string.failed_to_obtain_project_list))
    }

    override fun showJumpToTheTop() {
        recyclerView.smoothScrollToPosition(0)
    }


    private fun initRefreshLayout(){
        with(mRefreshLayout){
            setOnRefreshListener {
                it.layout.postDelayed({
                    mPresenter.refresh(cid)
                    finishRefresh()
                },1000)
            }
            setOnLoadMoreListener {
                it.layout.postDelayed({
                    mPresenter.loadMore(cid)
                    finishLoadMore()
                },1000)
            }
        }
    }
    companion object {
        /**
         * @param param1 cid
         */
        fun getInstance(param1: Int,param2: String=""): ProjectListFragment {
            val fragment=ProjectListFragment()
            val args=Bundle()
            args.putInt(Constants.ARG_PARAM1,param1)
            args.putString(Constants.ARG_PARAM2,param2)
            fragment.arguments=args
            return fragment
        }
    }
}