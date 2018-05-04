package com.example.wanandroidtest.other

import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.hierarchy.KnowledgeHierarchyData
import com.example.wanandroidtest.other.bean.main.banner.BannerData
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleListData
import com.example.wanandroidtest.other.bean.main.login.LoginData
import com.example.wanandroidtest.other.bean.main.search.TopSearchData
import com.example.wanandroidtest.other.bean.main.search.UsefulSiteData
import com.example.wanandroidtest.other.bean.navigation.NavigationListData
import com.example.wanandroidtest.other.bean.project.ProjectClassifyData
import com.example.wanandroidtest.other.bean.project.ProjectListData
import com.example.wanandroidtest.other.db.HistoryData
import com.example.wanandroidtest.other.db.DbHelper
import com.example.wanandroidtest.other.http.HttpHelper
import com.example.wanandroidtest.other.pref.PreferenceHelper
import io.reactivex.Observable

/**
 * Created by Administrator on 2018/4/20 0020.
 * 可操作网络，本地sharedPreference和db(PS:不知道可不可以优化，现在这写法有点模板，麻烦)
 * @param 传入三个实现参数里接口的实例
 */
class DataManager(private val httpHelper: HttpHelper,private val dbHelper: DbHelper,private val preferenceHelper: PreferenceHelper)
    : HttpHelper, DbHelper, PreferenceHelper {
    override fun getFeedArticleList(pageNum: Int): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.getFeedArticleList(pageNum)
    }

    override fun getSearchList(pageNum: Int, k: String?): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.getSearchList(pageNum, k)
    }

    override fun getTopSearchData(): Observable<BaseResponse<MutableList<TopSearchData>>> {
        return httpHelper.topSearchData
    }

    override fun getUsefulSites(): Observable<BaseResponse<MutableList<UsefulSiteData>>> {
        return httpHelper.usefulSites
    }

    override fun getKnowledgeHierarchyData(): Observable<BaseResponse<MutableList<KnowledgeHierarchyData>>> {
        return httpHelper.knowledgeHierarchyData
    }

    override fun getKnowledgeHierarchyDetailData(page: Int, cid: Int): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.getKnowledgeHierarchyDetailData(page, cid)
    }

    override fun getNavigationListData(): Observable<BaseResponse<List<NavigationListData>>> {
        return httpHelper.navigationListData
    }

    override fun getProjectClassifyData(): Observable<BaseResponse<List<ProjectClassifyData>>> {
        return httpHelper.projectClassifyData
    }

    override fun getProjectListData(page: Int, cid: Int): Observable<BaseResponse<ProjectListData>> {
        return httpHelper.getProjectListData(page, cid)
    }

    override fun getLoginData(username: String, password: String): Observable<BaseResponse<LoginData>> {
        return httpHelper.getLoginData(username, password)
    }

    override fun getRegisterData(username: String, password: String, rePassword: String): Observable<BaseResponse<LoginData>> {
        return httpHelper.getRegisterData(username, password, rePassword)
    }

    override fun addCollectArticle(id: Int): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.addCollectArticle(id)
    }

    override fun addCollectOutsideArticle(title: String?, author: String?, link: String?): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.addCollectOutsideArticle(title, author, link)
    }

    override fun getCollectList(page: Int): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.getCollectList(page)
    }

    override fun cancelCollectPageArticle(id: Int): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.cancelCollectPageArticle(id)
    }

    override fun cancelCollectArticle(id: Int): Observable<BaseResponse<FeedArticleListData>> {
        return httpHelper.cancelCollectArticle(id)
    }


    override fun getBannerData(): Observable<BaseResponse<List<BannerData>>> {
        return httpHelper.bannerData
    }


    override fun addHistoryData(data: String): List<HistoryData> {
        return dbHelper.addHistoryData(data)
    }

    override fun clearHistoryData() {
        return dbHelper.clearHistoryData()    }

    override fun loadAllHistoryData(): List<HistoryData> {
        return dbHelper.loadAllHistoryData()    }

    override var loginAccount: String
        get() = preferenceHelper.loginAccount
        set(value) {preferenceHelper.loginAccount=value}

    override var loginPassword: String
        get() =preferenceHelper.loginPassword
        set(value) { preferenceHelper.loginPassword=value}
    override var loginStatus: Boolean
        get() = preferenceHelper.loginStatus
        set(value) { preferenceHelper.loginStatus=value}

    override var currentPage: Int
        get() = preferenceHelper.currentPage
        set(value) {preferenceHelper.currentPage=value}

    override var autoCacheState: Boolean
        get() = preferenceHelper.autoCacheState
        set(value) {preferenceHelper.autoCacheState=value}

    override var nightModeState: Boolean
        get() = preferenceHelper.nightModeState
        set(value) {preferenceHelper.nightModeState=value}

    override var projectCurrentPage: Int
        get() = preferenceHelper.projectCurrentPage
        set(value) {preferenceHelper.projectCurrentPage=value}

    override var imageState: Boolean
        get() = preferenceHelper.imageState
        set(value) {preferenceHelper.imageState =value}

}