package com.example.wanandroidtest.di.component

import com.example.wanandroidtest.SettingFragment
import com.example.wanandroidtest.di.scope.FragmentScope
import com.example.wanandroidtest.ui.KnowledgeHierarchy.KnowledgeHierarchyFragment
import com.example.wanandroidtest.ui.KnowledgeHierarchy.KnowledgeHierarchyListFragment
import com.example.wanandroidtest.ui.navigation.NavigationFragment
import com.example.wanandroidtest.ui.homepager.MainPagerFragment
import com.example.wanandroidtest.ui.main.CollectFragment
import com.example.wanandroidtest.ui.main.SearchDialogFragment
import com.example.wanandroidtest.ui.main.UsageDialogFragment
import com.example.wanandroidtest.ui.project.ProjectFragment
import com.example.wanandroidtest.ui.project.ProjectListFragment
import dagger.Component

/**
 * Created by Administrator on 2018/4/21 0021.
 */
@FragmentScope
@Component(dependencies = arrayOf(AppComponent::class))
interface FragmentComponent {

//    val activity: Activity

    fun inject(mainPagerFragment: MainPagerFragment)

    fun inject(knowledgeHierarchyFragment: KnowledgeHierarchyFragment)

    fun inject(knowledgeHierarchyListFragment: KnowledgeHierarchyListFragment)

    fun inject(usageDialogFragment: UsageDialogFragment)

    fun inject(searchDialogFragment: SearchDialogFragment)

    fun inject(navigationFragment: NavigationFragment)

    fun inject(projectFragment: ProjectFragment)

    fun inject(projectListFragment: ProjectListFragment)

    fun inject(collectFragment: CollectFragment)

    fun inject(settingFragment: SettingFragment)
}