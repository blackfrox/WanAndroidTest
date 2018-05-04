package com.example.wanandroidtest.ui.KnowledgeHierarchy

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.activity.AbstractRootActivity
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.hierarchy.KnowledgeHierarchyDetailContract
import com.example.wanandroidtest.other.bean.hierarchy.KnowledgeHierarchyData
import com.example.wanandroidtest.component.event.KnowledgeJumpTopEvent
import com.example.wanandroidtest.presenter.hierarchy.KnowledgeHierarchyDetailPresenter
import com.example.wanandroidtest.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_knowledge_hierarchy_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class KnowledgeHierarchyDetailActivity : AbstractRootActivity<KnowledgeHierarchyDetailPresenter>(), KnowledgeHierarchyDetailContract.View {

    override fun getLayoutId(): Int {
        return R.layout.activity_knowledge_hierarchy_detail
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    private val mFragments by lazy { arrayListOf<Fragment>() }
    override fun initEvent(savedInstanceState: Bundle?) {
        initToolbar()
        viewPager.adapter=object :FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                if (intent.getBooleanExtra(Constants.IS_SINGLE_CHAPTER,false))
                    return chapterName
                else
                    return mList[position].name
            }
        }
        tabLayout.background=toolbar.background
        tabLayout.setViewPager(viewPager)

        fab.setOnClickListener {
            RxBus.instance.post(KnowledgeJumpTopEvent())
        }
    }

    private val mList by lazy { arrayListOf<KnowledgeHierarchyData>() }

    private lateinit var chapterName : String

    private fun initToolbar (){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedSupport() }
       with(intent){
           if (getBooleanExtra(Constants.IS_SINGLE_CHAPTER,false)){
               val superChapterName=getStringExtra(Constants.SUPER_CHAPTER_NAME)
               chapterName=getStringExtra(Constants.CHAPTER_NAME)
               val chapterId=getIntExtra(Constants.CHAPTER_ID,0)
               mTitleTv.text=superChapterName
               mFragments.add(KnowledgeHierarchyListFragment.getInstance(chapterId))
           }else{
               val data=getSerializableExtra(Constants.ARG_PARAM1) as KnowledgeHierarchyData
               assert(data.name!=null)
               mTitleTv.text=data.name.trim()
               mList.clear()
               mList.addAll(data.children)
               if (mList.size<1)
                   return
               else{
                   for (item in mList)
                       mFragments.add(KnowledgeHierarchyListFragment.getInstance(item.id))
               }
           }
       }

    }

    override fun showSwitchProject() {
        onBackPressedSupport()
    }

    override fun showSwitchNavigation() {
        onBackPressedSupport()
    }

}
