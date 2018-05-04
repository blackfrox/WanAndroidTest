package com.example.wanandroidtest.ui.project

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.fragment.AbstractRootFragment
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.project.ProjectContract
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.project.ProjectClassifyData
import com.example.wanandroidtest.component.event.JumpToTheTopEvent
import com.example.wanandroidtest.presenter.project.ProjectPresenter
import com.example.wanandroidtest.util.CommonUtil
import kotlinx.android.synthetic.main.fragment_project.*
import org.jetbrains.anko.support.v4.act

/**
 * Created by Administrator on 2018/4/28.
 */
class ProjectFragment : AbstractRootFragment<ProjectPresenter>(), ProjectContract.View {

    companion object {
        fun getInstance(param1: String = "", param2: String = ""): ProjectFragment {
            val fragment = ProjectFragment()
            val args = Bundle()
            args.putString(Constants.ARG_PARAM1, param1)
            args.putString(Constants.ARG_PARAM2, param2)
            return fragment
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    override fun initInject() {
        fragmentComponent.inject(this)
    }

    override fun initEvent(savedInstanceState: Bundle?) {
        super.initEvent(savedInstanceState)

        mPresenter.getProjectClassifyData()
        if (CommonUtil.isNetworkConnected())
            showLoading()
    }

    override fun reload() {
        mPresenter.getProjectClassifyData()
    }
    override fun showProjectClassifyData(projectClassifyResponse: BaseResponse<List<ProjectClassifyData>>) {
        if (projectClassifyResponse.data == null) {
            showProjectClassifyDataFail()
            return
        }
        val mFragments=arrayListOf<Fragment>()
        val mData = projectClassifyResponse.data
        for (item in mData) {
            val fragment = ProjectListFragment.getInstance(item.id)
            mFragments.add(fragment)
        }
        viewPager.adapter=object : FragmentPagerAdapter(childFragmentManager){
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mData.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mData[position].name
            }
        }
        mTabLayout.setViewPager(viewPager)
        viewPager.setCurrentItem(Constants.TAB_ONE)
        showNormal()
    }

    override fun showProjectClassifyDataFail() {
        CommonUtil.showSnackMessage(act,getString(R.string.failed_to_obtain_project_classify_data))
    }

    fun jumpToTop()=RxBus.instance.post(JumpToTheTopEvent())

}