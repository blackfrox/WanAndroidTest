package com.example.wanandroidtest.ui.KnowledgeHierarchy

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.hierarchy.KnowledgeHierarchyListContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.component.event.CollectEvent
import com.example.wanandroidtest.component.event.SelectNavigationEvent
import com.example.wanandroidtest.component.event.SelectProjectEvent
import com.example.wanandroidtest.presenter.hierarchy.KnowledgeHierarchyListPresenter
import com.example.wanandroidtest.ui.main.LoginActivity
import com.example.wanandroidtest.ui.homepager.ArticleListAdapter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.example.wanandroidtest.util.checkListIsEmpty
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.fragment_knowledge_hierarchy_list.*
import org.jetbrains.anko.support.v4.find
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class KnowledgeHierarchyListFragment : AbstractRootFragment<KnowledgeHierarchyListPresenter>(), KnowledgeHierarchyListContract.View {

    companion object {
        /**
         * @param  当前item的id(网络访问需要)
         */
        fun getInstance(id: Int = 0): KnowledgeHierarchyListFragment {
            val fragment = KnowledgeHierarchyListFragment()
            val args = Bundle()
            args.putInt(Constants.ARG_PARAM1, id)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var dataManager: DataManager

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_knowledge_hierarchy_list
    }

    private var cid = 0
    private var mList = arrayListOf<FeedArticleData>()
    private val mAdapter by lazy { ArticleListAdapter(R.layout.item_search_pager, mList) }
    private var articlePosition = 0 //当前点击的item的position
    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)
        initRefreshLayout()
        cid = arguments!!.getInt(Constants.ARG_PARAM1, 0)
        if (cid == 0)
            return
        with(mAdapter){
           setOnItemClickListener { _, view, position ->
                articlePosition = position
                val option = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ActivityOptions.makeSceneTransitionAnimation(activity, view, getString(R.string.share_view))
                else null
                with(mAdapter.data[position]) {
                    JudgeUtils.startArticleDetailActivity(activity!!,
                            option,
                            id,
                            title.trim(),
                            link.trim(),
                            isCollect)
                }
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_search_pager_like_iv -> likeEvent(position)
                    R.id.item_search_pager_tag_red_tv -> {
                        val superChapterName = mAdapter.data[position].superChapterName
                        if (superChapterName.contains(getString(R.string.open_project)))
                            RxBus.instance.post(SelectProjectEvent())
                        else if (superChapterName.contains(getString(R.string.navigation)))
                            RxBus.instance.post(SelectNavigationEvent())
                    }
                }
            }
        }
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        mPresenter.refresh(cid)
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }

    override fun showKnowledgeHierarchyDetailData(response: BaseResponse<FeedArticleListData>, refresh: Boolean) {
        if (response.data == null ||
                response.data.datas == null) {
            showKnowledgeHierarchyDetailDataFail()
            return
        }
        val list = response.data.datas
        checkListIsEmpty(list)
        if (isRefresh)
            mAdapter.replaceData(list)
        else
            mAdapter.addData(list)
        showNormal()
    }

    override fun reload() {
        normal_view.autoRefresh()
    }

    override fun showCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position, feedArticleData)
        RxBus.instance.post(CollectEvent(false))
        CommonUtil.showSnackMessage(activity!!, getString(R.string.collect_success))

    }

    override fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, feedArticleListResponse: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position, feedArticleData)
        RxBus.getDefault().post(CollectEvent(true))
        CommonUtil.showSnackMessage(activity!!, getString(R.string.cancel_collect))
    }

    override fun showKnowledgeHierarchyDetailDataFail() {
        CommonUtil.showSnackMessage(activity!!, getString(R.string.failed_to_obtain_knowledge_data))
    }

    override fun showJumpTheTop() {
        recyclerView.scrollToPosition(0)
    }

    override fun showReloadDetailEvent() {
        normal_view.autoRefresh()
    }

    override fun showCollectSuccess() {
        if (mAdapter.data.size > articlePosition) {
            mAdapter.data[articlePosition].isCollect = true
            mAdapter.setData(articlePosition, mAdapter.data[articlePosition])
        }
    }

    override fun showCancelCollectSuccess() {
        if (mAdapter.data.size > articlePosition) {
            mAdapter.data[articlePosition].isCollect = false
            mAdapter.setData(articlePosition, mAdapter.data[articlePosition])
        }
    }


    private fun likeEvent(position: Int) {
        if (!dataManager.loginStatus) {
            startActivity(Intent(activity, LoginActivity::class.java))
            CommonUtil.showSnackMessage(activity!!, getString(R.string.login_tint))
            return
        }
        if (mAdapter.data[position].isCollect)
            mPresenter.cancelCollectArticle(position, mAdapter.data[position])
        else
            mPresenter.addCollectArticle(position, mAdapter.data[position])
    }

    private val mRefreshLayout by lazy { find<SmartRefreshLayout>(R.id.normal_view) }
    private var isRefresh = false
    private fun initRefreshLayout() {
        with(mRefreshLayout) {
            setPrimaryColorsId(Constants.BLUE_THEME, R.color.white)
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
}