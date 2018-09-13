package com.example.wanandroidtest.di.component

import com.example.wanandroidtest.ui.main.MainActivity
import com.example.wanandroidtest.di.scope.ActivityScope
import com.example.wanandroidtest.ui.main.ArticleDetailActivity
import com.example.wanandroidtest.ui.KnowledgeHierarchy.KnowledgeHierarchyDetailActivity
import com.example.wanandroidtest.ui.main.LoginActivity
import com.example.wanandroidtest.ui.main.SearchListActivity
import dagger.Component

/**
 * Created by Administrator on 2018/4/21 0021.
 */
//@ActivityScope 因为没有scope的component不能依赖父component，所以添加自定义scope
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class))
interface ActivityComponent {

//    val activity: Activity

    fun inject(mainActivity: MainActivity)

    fun inject(articleDetailActivity: ArticleDetailActivity)

    fun inject(knowledgeHierarchyDetailActivity: KnowledgeHierarchyDetailActivity)

    fun inject(searchListActivity: SearchListActivity)

    fun inject(loginActivity: LoginActivity)
}