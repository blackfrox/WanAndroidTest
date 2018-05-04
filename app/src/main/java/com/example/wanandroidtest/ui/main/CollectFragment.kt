package com.example.wanandroidtest.ui.main

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.wanandroidtest.R
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.contract.main.CollectContract
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.presenter.main.CollectPresenter
import com.example.wanandroidtest.ui.homepager.ArticleListAdapter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_collect.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.find

/**
 * Created by Administrator on 2018/5/1.
 */
class CollectFragment: AbstractRootFragment<CollectPresenter>(),CollectContract.View {

    companion object {
        fun getInstance(): CollectFragment {
            val fragment=CollectFragment()
            return fragment
        }
    }
    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect
    }

    private val mRefreshLayout by lazy { find<SmartRefreshLayout>(R.id.normal_view) }
    private val mAdapter by lazy { ArticleListAdapter(R.layout.item_search_pager, arrayListOf<FeedArticleData>()) }
    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)
        initView()
        initRefreshLayout()
        mPresenter.refresh()
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }

    override fun reload() {
        mPresenter.refresh()
    }

    override fun showCollectList(feedArticleListResponse: BaseResponse<FeedArticleListData>, isRefresh: Boolean) {
        if (feedArticleListResponse.data==null||
                feedArticleListResponse.data.datas==null){
            showCollectListFail()
            return
        }
        val list=feedArticleListResponse.data.datas
        if (isRefresh)
            mAdapter.replaceData(list)
        else
            mAdapter.addData(list)
        if (list.size==0)
            CommonUtil.showSnackMessage(act,getString(R.string.no_collect))
        showNormal()
    }

    override fun showCancelCollectPageArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.remove(position)
        CommonUtil.showSnackMessage(act,getString(R.string.cancel_collect_success))
    }

    override fun showCollectListFail() {
        CommonUtil.showSnackMessage(act,getString(R.string.failed_to_obtain_collect_data))
    }
    private fun initView() {
        with(mAdapter){
            isCollectPage()
            setOnItemClickListener { adapter, view, position ->
                val options= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ActivityOptions.makeSceneTransitionAnimation(activity,view,getString(R.string.share_view))
                 else null
                with(mAdapter.data[position]){
                    JudgeUtils.startArticleDetailActivity(act, options,
                            id,
                            title,
                            link,
                            true,
                            true,
                            false)
                }
            }
            setOnItemChildClickListener { adapter, view, position ->
                when(view.id){
                    R.id.item_search_pager_chapterName ->{
                        with(mAdapter.data[position]){
                            JudgeUtils.startKnowledgeHierarchyDetailActivity(act,
                                    true,
                                    chapterName,
                                    chapterName,
                                    chapterId)
                        }
                    }
                    R.id.item_search_pager_like_iv -> {
                        //取消收藏
                        mPresenter.cancelCollectPageArticle(position,mAdapter.data[position])
                    }
                }
            }
        }
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager=LinearLayoutManager(activity)

//        fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
    }

    fun jumpToTop()=recyclerView.smoothScrollToPosition(0)
    override fun showRefreshEvent() {
        mPresenter.refresh()
    }
    private fun initRefreshLayout(){
        with(mRefreshLayout){
            setOnRefreshListener {
                it.layout.postDelayed({
                    mPresenter.refresh()
                    finishRefresh()
                },1000)
            }
            setOnLoadMoreListener {
                it.layout.postDelayed({
                    mPresenter.loadMore()
                    finishLoadMore()
                },1000)
            }
        }
    }

}