package com.example.wanandroidtest.util

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.ui.main.ArticleDetailActivity
import com.example.wanandroidtest.ui.KnowledgeHierarchy.KnowledgeHierarchyDetailActivity
import com.example.wanandroidtest.ui.main.SearchListActivity

/**
 * Created by Administrator on 2018/4/23 0023.
 */
object JudgeUtils {

    /**
     * @param isCommonSite 是否是从UsageDialogFragment 打开的
     */
    fun startArticleDetailActivity(context: Context, activityOptions: ActivityOptions?, id: Int, articleTitle: String,
                                   articleLink: String, isCollect: Boolean,
                                   isCollectPage: Boolean = false, isCommonSite: Boolean = false) {
        val intent = Intent(context, ArticleDetailActivity::class.java)
        with(intent) {
            putExtra(Constants.ARTICLE_ID, id)
            putExtra(Constants.ARTICLE_TITLE, articleTitle)
            putExtra(Constants.ARTICLE_LINK, articleLink)
            putExtra(Constants.IS_COLLECT, isCollect)
            putExtra(Constants.IS_COLLECT_PAGE, isCollectPage)
            putExtra(Constants.IS_COMMON_SITE, isCommonSite)
        }
        if (activityOptions != null) {
            context.startActivity(intent, activityOptions.toBundle())
        } else {
            context.startActivity(intent)
        }
    }

    fun startSearchListActivity(context: Context, searchText: String) {
        val intent = Intent(context, SearchListActivity::class.java)
        intent.putExtra(Constants.SEARCH_TEXT, searchText)
        context.startActivity(intent)
    }

    fun startKnowledgeHierarchyDetailActivity(context: Context, isSingleChapter: Boolean,
                                              superChapterName: String, chapterName: String, chapterId: Int) {
        val intent = Intent(context, KnowledgeHierarchyDetailActivity::class.java)
        intent.putExtra(Constants.IS_SINGLE_CHAPTER, isSingleChapter)
        intent.putExtra(Constants.SUPER_CHAPTER_NAME, superChapterName)
        intent.putExtra(Constants.CHAPTER_NAME, chapterName)
        intent.putExtra(Constants.CHAPTER_ID, chapterId)
        context.startActivity(intent)
    }
}