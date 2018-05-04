package com.example.wanandroidtest.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebSettings
import android.widget.LinearLayout
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.activity.BaseActivity
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.contract.main.ArticleDetailContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.component.event.CollectEvent
import com.example.wanandroidtest.presenter.main.ArticleDetailPresenter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.StatusBarUtil
import com.just.agentweb.AgentWeb
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class ArticleDetailActivity : BaseActivity<ArticleDetailPresenter>(), ArticleDetailContract.View {

    override fun getLayoutId(): Int {
        return R.layout.activity_article_detail
    }

    @Inject
    lateinit var dataManager: DataManager

    private val mAgentWeb: AgentWeb by lazy {
        AgentWeb.with(this)
                .setAgentWebParent(mWebContent, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb().ready().go(articleLink)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initEvent(savedInstanceState: Bundle?) {
        initToolbar()

        val mWebView = mAgentWeb.webCreator.webView
        val mSettings = mWebView.settings

        with(mSettings) {
            if (mPresenter.getAutoCacheState()) {
                setAppCacheEnabled(true)
                domStorageEnabled = true
                databaseEnabled = true
                if (CommonUtil.isNetworkConnected())
                    cacheMode = WebSettings.LOAD_DEFAULT
                else
                    cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            } else {
                setAppCacheEnabled(false)
                domStorageEnabled = false
                databaseEnabled = false
                cacheMode = WebSettings.LOAD_NO_CACHE
            }
            javaScriptEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            //不显示缩放按钮
            displayZoomControls = false
            //设置自适应屏幕，两者合用
            //将图片调整到适合webView的大小
            useWideViewPort = true
            //缩放至屏幕的大小
            loadWithOverviewMode = true
            //自适应屏幕
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return mAgentWeb.handleKeyEvent(keyCode, event) ||
                super.onKeyDown(keyCode, event)
    }

    private var bundle: Bundle? = null
    private var isCollect = false
    private lateinit var articleLink: String
    private lateinit var mCollectItem: MenuItem

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        bundle = intent.extras
        assert(bundle != null)
        isCommonSite = bundle!!.get(Constants.IS_COMMON_SITE) as Boolean
        menuInflater.inflate(R.menu.menu_article, menu)
        if (!isCommonSite) {
            mCollectItem = menu.findItem(R.id.item_collect)
            mCollectItem.isVisible=true
            if (isCollect) {
                mCollectItem.setTitle(getString(R.string.cancel_collect))
                mCollectItem.setIcon(R.mipmap.ic_toolbar_like_p)
            } else {
                mCollectItem.setTitle(getString(R.string.collect))
                mCollectItem.setIcon(R.mipmap.ic_toolbar_like_n)
            }
        }
         return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.item_share -> mPresenter.shareEventPermissionVerify(RxPermissions(this))
            R.id.item_collect -> collectEvent()
            R.id.item_system_browser -> startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(articleLink)))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun shareEvent() {
        val intent=Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_type_url,getString(R.string.app_name),title,articleLink))
        intent.setType("text/plain")
        startActivity(intent)
    }

    override fun shareError() {
        CommonUtil.showSnackMessage(this,getString(R.string.write_permission_not_allowed))
    }

    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount>1)
            pop()
        else
//            super.onBackPressedSupport()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            }else
                finish()
    }

    private fun collectEvent(){
        if (!dataManager.loginStatus){
            toast(R.string.login_tint)
//            startActivity(Intent(this,LoginActivity::class.java))
        }else{
            if (mCollectItem.title.equals(getString(R.string.collect)))
                mPresenter.addCollectArticle(articleId)
            else{
                if (isCollectPage)
                    mPresenter.cancelCollectPageArticle(articleId)
                else
                    mPresenter.cancelCollectArticle(articleId)
            }

        }
    }

    private lateinit var mTitle: String
    private var articleId = -1
    private var isCommonSite = false
    private var isCollectPage = false

    private fun initToolbar(){
        bundle=intent.extras
        assert(bundle!=null)
        mTitle=bundle!!.get(Constants.ARTICLE_TITLE) as String
        mTitleTv.text=Html.fromHtml(mTitle)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this,toolbar)
        toolbar.setNavigationOnClickListener {
            if (dataManager.loginStatus==true){
                if (isCollect)
                    RxBus.instance.post(CollectEvent(true))
                else
                    RxBus.instance.post(CollectEvent(false))
            }
            onBackPressedSupport()
        }

        with(bundle!!){
            articleLink=get(Constants.ARTICLE_LINK) as String
            articleId=get(Constants.ARTICLE_ID) as Int
            isCommonSite=get(Constants.IS_COMMON_SITE) as Boolean
            isCollect=get(Constants.IS_COLLECT) as Boolean
            isCollectPage=get(Constants.IS_COLLECT_PAGE) as Boolean
        }
    }

    override fun showCollectArticleData(response: BaseResponse<FeedArticleListData>) {
        isCollect=true
        mCollectItem.setTitle(R.string.cancel_collect)
        mCollectItem.setIcon(R.mipmap.ic_toolbar_like_p)
        CommonUtil.showSnackMessage(this,getString(R.string.collect_success))
    }

    override fun showCancelCollectArticleData(response: BaseResponse<FeedArticleListData>) {
        isCollect = false
        if (!isCollectPage){
            mCollectItem.setTitle(R.string.collect)
            mCollectItem.setIcon(R.mipmap.ic_toolbar_like_n)
        }
        CommonUtil.showSnackMessage(this,getString(R.string.cancel_collect_success))
    }

}
