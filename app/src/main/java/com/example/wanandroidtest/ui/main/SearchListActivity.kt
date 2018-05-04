package com.example.wanandroidtest.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.activity.AbstractRootActivity
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.main.SearchListContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.component.event.SelectNavigationEvent
import com.example.wanandroidtest.component.event.SelectProjectEvent
import com.example.wanandroidtest.presenter.main.SearchListPresenter
import com.example.wanandroidtest.ui.LoginActivity
import com.example.wanandroidtest.ui.homepager.ArticleListAdapter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.example.wanandroidtest.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_search_list.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject

class SearchListActivity : AbstractRootActivity<SearchListPresenter>(), SearchListContract.View {

    override fun getLayoutId(): Int {
        return R.layout.activity_search_list
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    @Inject
    lateinit var dataManager: DataManager
    private val mRecyclerView by lazy { find<RecyclerView>(R.id.normal_view) }
    private val mAdapter by lazy { ArticleListAdapter(R.layout.item_search_pager, arrayListOf()) }
    private var mCurrentPage = 0
    private var articlePosition =0 //当前点击的item的position
    private var isAddData = false //是否是下拉刷新

    private lateinit var searchText: String

    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)
        val bundle=intent.extras
        if (bundle==null)
            return
        searchText=bundle.get(Constants.SEARCH_TEXT) as String
        if(!TextUtils.isEmpty(searchText))
            mTitleTv.text=searchText
        initToolbar()
        mPresenter.getSearchList(mCurrentPage,searchText)
        fab.setOnClickListener { mRecyclerView.scrollToPosition(0) }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            articlePosition=position
            val options=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(this,view,getString(R.string.share_view))
            }else
                null
           with(mAdapter.data[position]){
               JudgeUtils.startArticleDetailActivity(this@SearchListActivity,
                       options,
                       id,
                       title,
                       link,
                       isCollect,
                       false,
                       false)
           }
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.item_search_pager_chapterName ->
                    with(mAdapter.data[position]){
                        JudgeUtils.startKnowledgeHierarchyDetailActivity(this@SearchListActivity,
                                true,
                                superChapterName,
                                chapterName,
                                chapterId)
                    }
                R.id.item_search_pager_like_iv -> likeEvent(position)
                R.id.item_search_pager_tag_red_tv ->{
                    val superChapterName=mAdapter.data[position].superChapterName
                    if (superChapterName.contains(getString(R.string.open_project))){
                        onBackPressedSupport()
                        RxBus.instance.post(SelectProjectEvent())
                    }else if (superChapterName.contains(getString(R.string.navigation))){
                        onBackPressedSupport()
                        RxBus.instance.post(SelectNavigationEvent())
                    }
                }
            }
        }
        mRecyclerView.adapter=mAdapter
        mRecyclerView.layoutManager=LinearLayoutManager(this)
        setRefresh()
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }


    override fun showSearchList(feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        if (feedArticleListResponse.data==null||
                feedArticleListResponse.data.datas==null){
            showSearchListFail()
            return
        }
        if (isAddData){
            mAdapter.addData(feedArticleListResponse.data.datas)
        }else
            mAdapter.replaceData(feedArticleListResponse.data.datas)
        showNormal()
    }

    override fun showCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position,feedArticleData)
        CommonUtil.showSnackMessage(this,getString(R.string.collect_success))
    }

    override fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position,feedArticleData)
        CommonUtil.showSnackMessage(this,getString(R.string.cancel_collect_success))
    }

    override fun showSearchListFail() {
        CommonUtil.showSnackMessage(this,getString(R.string.failed_to_obtain_search_data_list))
    }

    override fun showCollectSuccess() {
        if (mAdapter.data.size>articlePosition){
            mAdapter.data[articlePosition].isCollect=true
            mAdapter.notifyItemChanged(articlePosition)
        }
    }

    private fun initToolbar(){

        StatusBarUtil.setPaddingSmart(this,toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title=""
        toolbar.setNavigationOnClickListener { onBackPressedSupport() }
    }

    private fun likeEvent(position: Int){
        if (!dataManager.loginStatus){
            startActivity(Intent(this,LoginActivity::class.java))
            toast(getString(R.string.login_tint))
            return
        }
        if (mAdapter.data[position].isCollect)
            mPresenter.cancelCollectArticle(position,mAdapter.data[position])
        else
            mPresenter.addCollectArticle(position,mAdapter.data[position])

    }

    private fun setRefresh(){
        refreshLayout.setOnRefreshListener {
            mCurrentPage=0
            isAddData=false
            mPresenter.getSearchList(mCurrentPage,searchText)
            refreshLayout.finishRefresh(1000)
        }
        refreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            isAddData= true
            mPresenter.getSearchList(mCurrentPage,searchText)
            refreshLayout.finishRefresh(1000)
        }
    }
}
