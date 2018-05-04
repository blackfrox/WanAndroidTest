package com.example.wanandroidtest.ui.navigation

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.contract.navigation.NavigationContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.navigation.NavigationListData
import com.example.wanandroidtest.presenter.NavigationPresenter
import com.example.wanandroidtest.util.CommonUtil
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.jetbrains.anko.support.v4.act
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView
import q.rorbin.verticaltablayout.widget.TabView
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/28.
 */
class NavigationFragment : AbstractRootFragment<NavigationPresenter>(), NavigationContract.View {
    companion object {
        fun getInstance(param1: String="", param2: String=""): NavigationFragment {
            val fragment = NavigationFragment()
            val args = Bundle()
            args.putString(Constants.ARG_PARAM1, param1)
            args.putString(Constants.ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_navigation
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    @Inject
    lateinit var mDataManager: DataManager
    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)

        mPresenter.getNavigationListData()
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }

    override fun reload() {
        mPresenter.getNavigationListData()
    }

    override fun showNavigationListData(navigationListResponse: BaseResponse<List<NavigationListData>>) {
        if (navigationListResponse.data == null) {
            showNavigationListFail()
            return
        }
        val mList = navigationListResponse.data
        mTabLayout.setTabAdapter(object : TabAdapter {
            override fun getCount(): Int {
                return mList.size
            }

            override fun getBadge(position: Int): ITabView.TabBadge? {
                return null
            }

            override fun getIcon(position: Int): ITabView.TabIcon? {
                return null
            }

            override fun getTitle(position: Int): ITabView.TabTitle {
                return ITabView.TabTitle.Builder()
                        .setContent(mList[position].name)
                        .setTextColor(-0xc94365, -0x8a8a8b)
                        .build()
            }

            override fun getBackground(position: Int): Int {
                return -1
            }
        })
        val mAdapter=NavigationAdapter(R.layout.item_navigation, mList)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = mLayoutManager
        leftRightLinkage()
        showNormal()
    }

    override fun showNavigationListFail() {
        CommonUtil.showSnackMessage(act, getString(R.string.failed_to_obtain_navigation_list))
    }

    override fun showError() {
        CommonUtil.showSnackMessage(act, getString(R.string.http_error))
    }

    private val mLayoutManager by lazy { LinearLayoutManager(act) }
    private var needScroll = false
    private var index =0
    private var isClickTab = false
    /**
     * Left tabLayout and right recyclerView linkage
     */
    private fun leftRightLinkage() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (needScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    needScroll = false
                    val indexDistance = index - mLayoutManager.findFirstVisibleItemPosition()
                    if (0 <= indexDistance&& indexDistance < recyclerView!!.childCount){
                        val top=recyclerView.getChildAt(0).top
                        recyclerView.smoothScrollBy(0,top)
                    }
                }
                rightLinkageLeft(newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (needScroll){
                    needScroll= false
                    val indexDistance=index-mLayoutManager.findFirstVisibleItemPosition()
                    if (0<=indexDistance&& indexDistance<recyclerView!!.childCount){
                        val top=recyclerView.getChildAt(indexDistance).top
                        recyclerView.smoothScrollBy(0,top)
                    }
                }
            }
        })
        mTabLayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabView?, position: Int) {
                isClickTab = true
                selectTag(position)
            }

            override fun onTabReselected(tab: TabView?, position: Int) {

            }
        })
    }

    /**
     * Right recyclerView linkage left tabLayout
     * SCROLL_STATE_IDLE just call once
     *
     * @param newState RecyclerView new scroll state
     */
    private fun rightLinkageLeft(newState: Int){
        if (newState==RecyclerView.SCROLL_STATE_IDLE){
            if(isClickTab){
                isClickTab= false
                return
            }
            val firstPosition=mLayoutManager.findFirstVisibleItemPosition()
            if (index!=firstPosition){
                index=firstPosition
                setChecked(index)
            }
        }
    }

    /**
     * Smooth right to select the position of the left tab
     *
     * @param position checked position
     */
    private fun setChecked(position: Int){
        if (isClickTab)
            isClickTab=false
        else
            mTabLayout.setTabSelected(index)
        index=position
    }

    private fun smoothScrollToPosition(currentPosition: Int){
        val firstPosition=mLayoutManager.findFirstVisibleItemPosition()
        val lastPosition=mLayoutManager.findLastVisibleItemPosition()
        when{
            currentPosition<=firstPosition -> recyclerView.smoothScrollToPosition(currentPosition)
            currentPosition<=lastPosition ->{
                val top=recyclerView.getChildAt(currentPosition-firstPosition).top
                recyclerView.smoothScrollBy(0,top)
            }
            else -> {
                recyclerView.smoothScrollToPosition(currentPosition)
                needScroll = true
            }
        }
    }

    private fun selectTag(i: Int) {
        index = i
        recyclerView.stopScroll()
        smoothScrollToPosition(i)
    }
    fun jumpToTop()=mTabLayout.setTabSelected(0)


}