package com.example.wanandroidtest.ui.homepager

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.wanandroidtest.R
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.contract.mainpager.MainPagerContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.banner.BannerData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.component.event.LoginEvent
import com.example.wanandroidtest.component.event.SelectNavigationEvent
import com.example.wanandroidtest.component.event.SelectProjectEvent
import com.example.wanandroidtest.other.http.cookies.CookiesManager
import com.example.wanandroidtest.presenter.mainpager.MainPagerPresenter
import com.example.wanandroidtest.ui.main.LoginActivity
import com.example.wanandroidtest.util.*
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.fragment_main_pager.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

/**
 * 在BaseFragment中将view与presenter进行了绑定，传值
 * view与presenter的通信 -> RxBus(相当于EventBus，代替广播)
 * Created by Administrator on 2018/4/22 0022.
 */
class MainPagerFragment : AbstractRootFragment<MainPagerPresenter>(), MainPagerContract.View {

    companion object {
        fun getInstance(): MainPagerFragment {
            val fragment = MainPagerFragment()
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        mBanner?.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        mBanner?.stopAutoPlay()
    }

    override fun initInject() {
        return fragmentComponent.inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_pager
    }

    @Inject
    lateinit var mDataManager: DataManager
    private val mAdapter by lazy { ArticleListAdapter(R.layout.item_search_pager, arrayListOf<FeedArticleData>()) }
    private val mRefreshLayout by lazy { find<SmartRefreshLayout>(R.id.normal_view) }
    private var mBanner: Banner? = null
    private var articlePosition: Int = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)
        mPresenter.refresh()
        if (CommonUtil.isNetworkConnected())
            showLoading()
        with(mAdapter) {
            //add head banner
            val mHeaderGroup = LayoutInflater.from(_mActivity).inflate(R.layout.head_banner, null) as LinearLayout
            mBanner = mHeaderGroup.findViewById<Banner>(R.id.head_banner)
            mHeaderGroup.removeView(mBanner)
            addHeaderView(mBanner)
            setOnItemClickListener { _, view, position ->
                //记录点击的文章位置，便于在文章内点击收藏返回到此界面时能显示正确的收藏状态
                articlePosition = position
                val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ActivityOptions.makeSceneTransitionAnimation(act, view, getString(R.string.share_view))
                else null
                with(mAdapter.data[position]) {
                    JudgeUtils.startArticleDetailActivity(act,
                            options,
                            id,
                            title,
                            link,
                            isCollect)
                }
            }
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.item_search_pager_chapterName -> {
                        with(mAdapter.data[position]) {
                            JudgeUtils.startKnowledgeHierarchyDetailActivity(act,
                                    true,
                                    superChapterName,
                                    chapterName,
                                    chapterId)
                        }
                    }
                    R.id.item_search_pager_like_iv -> likeEvent(position)

                    R.id.item_search_pager_tag_red_tv -> {
                        val superChapterName = mAdapter.data[position].superChapterName
                        //通知activity,让它跳转到相应的fragment
                        if (superChapterName.contains(getString(R.string.open_project)))
                            RxBus.instance.post(SelectProjectEvent())
                        else
                            RxBus.instance.post(SelectNavigationEvent())
                    }
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter

        //吐槽: 唉，不知道怎么回事，原版里只需要finish**(1000),而这里却出现问题:数据加载完了，动画还没结束
        with(mRefreshLayout) {
            setOnRefreshListener {
                it.layout.postDelayed({
                    mPresenter.refresh()
                    it.finishRefresh()
                },1000)
            }
            setOnLoadMoreListener {
                it.layout.postDelayed({
                    mPresenter.loadMore()
                    it.finishLoadMore()
                },1000)
            }
        }
    }

    private fun likeEvent(position: Int) {
        if (!mDataManager.loginStatus) {
            toast(R.string.login_tint)
            startActivity(Intent(activity, LoginActivity::class.java))
        } else {
            val item = mAdapter.data[position]
            if (item.isCollect)
                mPresenter.cancelCollectArticle(position, item)
            else
                mPresenter.addCollectArticle(position, item)
        }
    }

    override fun showBannerData(response: BaseResponse<List<BannerData>>) {
        if (response.data == null) {
            showBannerDataFail()
            return
        }
        val titleList = arrayListOf<String>()
        val imageList = arrayListOf<String>()
        val urlList = arrayListOf<String>()
        val dataList = response.data
        for (i in dataList) {
            titleList.add(i.title)
            imageList.add(i.imagePath)
            urlList.add(i.url)
        }
        with(mBanner!!) {
            //            设置banner样式
            setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
            //设置图片加载器
            setImageLoader(GlideImageLoader())
            //设置图片集合
            setImages(imageList)
            //设置banner动画效果
            setBannerAnimation(Transformer.DepthPage)
            //设置标题集合(当banner样式有显示title时)
            setBannerTitles(titleList)
            //设置自动轮播，默认为true
            isAutoPlay(true)
            //设置轮播时间
            setDelayTime(dataList.size * 400)
            //设置指示器位置(当banner模式中有指示器时
            setIndicatorGravity(BannerConfig.CENTER)

            setOnBannerListener {
                JudgeUtils.startArticleDetailActivity(act, null,
                        0, titleList[it], urlList[it],
                        false, false, true)
            }
            //banner设置完毕时调用
            start()
        }
    }

    override fun showArticleList(response: BaseResponse<FeedArticleListData>, isRefresh: Boolean) {
        if (response.data == null || response.data.datas == null) {
            showArticleListFail()
            return
        }
        val list=response.data.datas
        checkListIsEmpty(list)
        if (isRefresh)
            mAdapter.replaceData(list)
        else
            mAdapter.addData(list)
        showNormal()
    }

    override fun showCollectArticleData(position: Int, feedArticleData: FeedArticleData, response: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position, feedArticleData)
        CommonUtil.showSnackMessage(act, getString(R.string.collect_success))
    }

    override fun showCancelCollectArticleData(position: Int, feedArticleData: FeedArticleData, response: BaseResponse<FeedArticleListData>) {
        mAdapter.setData(position, feedArticleData)
        CommonUtil.showSnackMessage(act, getString(R.string.cancel_collect_success))
    }

    override fun showBannerDataFail() {
        CommonUtil.showSnackMessage(act, getString(R.string.failed_to_obtain_banner_data))
    }

    override fun showArticleListFail() {
        CommonUtil.showSnackMessage(act, getString(R.string.failed_to_obtain_article_list))
    }

    override fun reload() {
        if (CommonUtil.isNetworkConnected())
            mRefreshLayout.autoRefresh()
        else
            CommonUtil.showSnackMessage(act, getString(R.string.please_check_the_network))
    }


    override fun showCollectSuccess() {
        if (articlePosition < mAdapter.data.size) {
            mAdapter.data.get(articlePosition).isCollect = true
            mAdapter.setData(articlePosition, mAdapter.data[articlePosition])
        }
    }

    override fun showCancelCollectSuccess() {
        if (articlePosition < mAdapter.data.size) {
            mAdapter.data[articlePosition].isCollect = false
            mAdapter.setData(articlePosition, mAdapter.data[articlePosition])
        }
    }

    override fun showAutoLoginSuccess() {
        CommonUtil.showSnackMessage(act, getString(R.string.auto_login_success))
        RxBus.instance.post(LoginEvent(true))
    }

    override fun showAutoLoginFail() {
        mDataManager.loginStatus = false
        CookiesManager.clearAllCookies()
        RxBus.instance.post(LoginEvent(false))
    }

    override fun jumpToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}