package com.example.wanandroidtest

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.wanandroidtest.ui.homepager.MainPagerFragment
import me.yokeyword.fragmentation.SupportFragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatDelegate
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.wanandroidtest.base.activity.BaseActivity
import com.example.wanandroidtest.contract.main.MainContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.http.cookies.CookiesManager
import com.example.wanandroidtest.presenter.main.MainPresenter
import com.example.wanandroidtest.ui.KnowledgeHierarchy.KnowledgeHierarchyFragment
import com.example.wanandroidtest.ui.LoginActivity
import com.example.wanandroidtest.ui.main.CollectFragment
import com.example.wanandroidtest.ui.main.SearchDialogFragment
import com.example.wanandroidtest.ui.main.UsageDialogFragment
import com.example.wanandroidtest.ui.navigation.NavigationFragment
import com.example.wanandroidtest.ui.project.ProjectFragment
import com.example.wanandroidtest.util.BottomNavigationViewHelper
import com.example.wanandroidtest.util.CommonAlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import javax.inject.Inject


/**
 * 对原作者吐槽 ：
 * 1 savedState ==null 和 ！=null 时，里面的方法是不是重复调用了?(明明可以写在外面？？？)
 */
class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initInject() {
        getActivityComponent().inject(this)
        if (mDataManager.nightModeState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private val FIRST = 0
    private val SECOND = 1
    private val THIRD = 2
    private val FOUR = 3
    private val FIVE = 4
    private val mFragments = arrayOfNulls<SupportFragment>(5)

    @Inject
    lateinit var mDataManager: DataManager
    //头布局的控件(只能通过findById方法找到)
    private val mUserTv: TextView  by lazy { nav_view.getHeaderView(0).findViewById<TextView>(R.id.mUserTv) }
    private var currentPage = FIRST //当前显示的fragment(通过buttomNavView获取)

    private val settingFragment by lazy { SettingFragment.instance }


    override fun initEvent(savedInstanceState: Bundle?) {
        val firstFragment = findFragment(MainPagerFragment::class.java)
        if (firstFragment == null) {
            mFragments[FIRST] = MainPagerFragment.getInstance()
            mFragments[SECOND] = KnowledgeHierarchyFragment.getInstance()
            mFragments[THIRD] = NavigationFragment.getInstance()
            mFragments[FOUR] = ProjectFragment.getInstance()
            mFragments[FIVE] = CollectFragment.getInstance()
            loadMultipleRootFragment(R.id.container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR],
                    mFragments[FIVE])
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findFragment(KnowledgeHierarchyFragment::class.java)
            mFragments[THIRD] = findFragment(NavigationFragment::class.java)
            mFragments[FOUR] = findFragment(ProjectFragment::class.java)
            mFragments[FIVE] = findFragment(CollectFragment::class.java)
        }

        initView()
    }

    private fun initView() {
        initToolbar(R.string.home_pager)
        //actionbar和drawerLayout的互动动画
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        initNavigationView()
        with(mBottomNavView) {
            BottomNavigationViewHelper.disableShiftMode(mBottomNavView) //bottomNavView的动画效果设置
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.tab_home -> {
                        mTitleTv.setText(R.string.home_pager)
                        showHideFragment(mFragments[FIRST])
                        currentPage = FIRST
                    }
                    R.id.tab_knowledge -> {
                        mTitleTv.setText(R.string.knowledge_hierarchy)
                        showHideFragment(mFragments[SECOND])
                        currentPage = SECOND
                    }
                    R.id.tab_navigation -> {
                        startNavigationFragment()
                    }
                    R.id.tab_project -> {
                        startProjectFragment()
                    }
                //todo: 添加fragment
                }
                true
            }
        }
        fab.setOnClickListener {
            val fragment = mFragments[currentPage]
            when (fragment) {
                is MainPagerFragment -> fragment.jumpToTop()
                is KnowledgeHierarchyFragment -> fragment.jumpToTop()
                is NavigationFragment -> fragment.jumpToTop()
                is ProjectFragment -> fragment.jumpToTop()
                is CollectFragment -> fragment.jumpToTop()
            }
        }
    }

    private fun initNavigationView() {
        if (mDataManager.loginStatus) //navView的头布局显示效果
            showLoginView()
        else
            showLogoutView()
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_wan_android -> showHideFragment(mFragments[currentPage])
                R.id.nav_my_collect -> {
                    if (mDataManager.loginStatus){
                        showHideFragment(mFragments[FIVE])
                        (mFragments[FIVE] as CollectFragment).reload()
                        mBottomNavView.visibility=View.INVISIBLE
//                        fab.visibility=View.VISIBLE
                        drawer_layout.closeDrawer(Gravity.START)
                        return@setNavigationItemSelectedListener true
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        toast(R.string.login_tint)
                    }
                }
                R.id.nav_setting -> startActivity(Intent(this,SettingsActivity::class.java))
                R.id.nav_night_mode -> {
                    mDataManager.nightModeState = !mDataManager.nightModeState
                    setNightMode(mDataManager.nightModeState)
                }
                R.id.nav_logout -> logout()
            }
            mBottomNavView.visibility=View.VISIBLE
//            fab.visibility=View.VISIBLE
            drawer_layout.closeDrawer(Gravity.START)
            true
        }
    }

    private fun logout() {
        CommonAlertDialog.newInstance().showDialog(
                this, getString(R.string.login_tint),
                getString(R.string.ok),
                getString(R.string.no),
                {
                    mDataManager.loginStatus = false
                    showLogoutView()
                    CookiesManager.clearAllCookies()
                    CommonAlertDialog.newInstance().cancelDialog(true)
                }, {
            CommonAlertDialog.newInstance().cancelDialog(true)
        })
    }

    override fun startNavigationFragment() {
        mTitleTv.setText(getString(R.string.navigation))
        showHideFragment(mFragments[THIRD])
        currentPage = THIRD
    }

    override fun startProjectFragment() {
        mTitleTv.setText(R.string.project)
        showHideFragment(mFragments[FOUR])
        currentPage = FOUR
    }

    /*                  头布局的登录登出状态          */
    override fun showLogoutView() {
        mUserTv.text = getString(R.string.login)
        mUserTv.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        //登出item设置隐藏
        nav_view.menu.findItem(R.id.nav_logout).isVisible = false
    }

    override fun showLoginView() {
        mUserTv.text = mDataManager.loginAccount
        mUserTv.setOnClickListener(null)
        nav_view.menu.findItem(R.id.nav_logout).isVisible = true
    }
    /*                  头布局的登录登出状态          */


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_usage -> {
                UsageDialogFragment().show(fragmentManager!!, "UsageDialogFragment")
            }
            R.id.action_search -> SearchDialogFragment().show(fragmentManager, "SearchDialogFragment")
        }
        return super.onOptionsItemSelected(item)
    }

}
