package com.example.wanandroidtest.other.http

import com.example.wanandroidtest.other.http.api.GeeksApis

import javax.inject.Inject

import io.reactivex.Observable
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
import javax.inject.Singleton


/**
 * @author quchao
 * @date 2017/11/27
 */
class RetrofitHelper @Inject
internal constructor(val mGeeksApis: GeeksApis) : HttpHelper {

    override fun getFeedArticleList(pageNum: Int): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.getFeedArticleList(pageNum)
    }

    override fun getSearchList(pageNum: Int, k: String): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.getSearchList(pageNum, k)
    }

    override fun getTopSearchData(): Observable<BaseResponse<List<TopSearchData>>> {
        return mGeeksApis.topSearchData
    }

    override fun getUsefulSites(): Observable<BaseResponse<List<UsefulSiteData>>> {
        return mGeeksApis.usefulSites
    }

    override fun getKnowledgeHierarchyData(): Observable<BaseResponse<List<KnowledgeHierarchyData>>> {
        return mGeeksApis.knowledgeHierarchyData
    }

    override fun getKnowledgeHierarchyDetailData(page: Int, cid: Int): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.getKnowledgeHierarchyDetailData(page, cid)
    }

    override fun getNavigationListData(): Observable<BaseResponse<List<NavigationListData>>> {
        return mGeeksApis.navigationListData
    }

    override fun getProjectClassifyData(): Observable<BaseResponse<List<ProjectClassifyData>>> {
        return mGeeksApis.projectClassifyData
    }

    override fun getProjectListData(page: Int, cid: Int): Observable<BaseResponse<ProjectListData>> {
        return mGeeksApis.getProjectListData(page, cid)
    }

    override fun getLoginData(username: String, password: String): Observable<BaseResponse<LoginData>> {
        return mGeeksApis.getLoginData(username, password)
    }

    override fun getRegisterData(username: String, password: String, repassword: String): Observable<BaseResponse<LoginData>> {
        return mGeeksApis.getRegisterData(username, password, repassword)
    }

    override fun addCollectArticle(id: Int): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.addCollectArticle(id)
    }

    override fun addCollectOutsideArticle(title: String, author: String, link: String): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.addCollectOutsideArticle(title, author, link)
    }

    override fun getCollectList(page: Int): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.getCollectList(page)
    }

    override fun cancelCollectPageArticle(id: Int): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.cancelCollectPageArticle(id, -1)
    }

    override fun cancelCollectArticle(id: Int): Observable<BaseResponse<FeedArticleListData>> {
        return mGeeksApis.cancelCollectArticle(id, -1)
    }

    override fun getBannerData(): Observable<BaseResponse<List<BannerData>>> {
        return mGeeksApis.bannerData
    }


}
