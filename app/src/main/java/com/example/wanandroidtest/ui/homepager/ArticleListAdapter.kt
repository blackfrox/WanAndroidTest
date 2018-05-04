package com.example.wanandroidtest.ui.homepager

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wanandroidtest.R
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.jakewharton.rxbinding2.view.ViewGroupHierarchyChildViewAddEvent
import kotlinx.android.synthetic.main.item_search_pager.view.*

/**
 * Created by Administrator on 2018/4/22 0022.
 */
class ArticleListAdapter(layoutResId: Int,data: List<FeedArticleData>): BaseQuickAdapter<FeedArticleData,BaseViewHolder>(layoutResId,data) {

    private var isCollectPage=false
    private var isSearchPage =false
//    private var isNightMode =false

    fun isCollectPage(){
        isCollectPage=true
        notifyDataSetChanged()
    }

    fun isSearchPage(){
        isSearchPage=true
        notifyDataSetChanged()
    }

//    fun isNightMode(){
//        isNightMode=true
//        notifyDataSetChanged()
//    }

    override fun convert(helper: BaseViewHolder, item: FeedArticleData) {
        with(item ){
          with(helper){
              if (!TextUtils.isEmpty(title))
                  setText(R.id.item_search_pager_title,Html.fromHtml(title))
              setImageResource(R.id.item_search_pager_like_iv,
                      if (isCollect || isCollectPage) R.drawable.icon_like
                                               else R.drawable.icon_like_article_not_selected)
              if (!TextUtils.isEmpty(author))
                  setText(R.id.item_search_pager_author,author)
              setTag(helper,item)
              if (!TextUtils.isEmpty(chapterName)){
                  if (isCollectPage)
                      setText(R.id.item_search_pager_chapterName,chapterName)
                  else{
                      val classifyName="$superChapterName/$chapterName"
                      setText(R.id.item_search_pager_chapterName,classifyName)
                  }
              }
              if (!TextUtils.isEmpty(niceDate))
                  setText(R.id.item_search_pager_niceDate,niceDate)
//              if (isSearchPage){
//                  val cardView=getView<CardView>(R.id.item_search_pager_group)
//                  cardView.foreground=null
//                  if (isNightMode)
//                      cardView.setBackgroundColor(R.color.card_color.toInt())
//                  else
//                      cardView.setBackground(ContextCompat.getDrawable(mContext,R.drawable.selector_search_item_bac))
//              }
              addOnClickListener(R.id.item_search_pager_chapterName)
              addOnClickListener(R.id.item_search_pager_like_iv)
              addOnClickListener(R.id.item_search_pager_tag_red_tv)
          }
        }
    }

    private fun setTag(helper: BaseViewHolder, item: FeedArticleData) {
        val greenTv=helper.getView<TextView>(R.id.item_search_pager_tag_green_tv)
        val redTv=helper.getView<TextView>(R.id.item_search_pager_tag_red_tv)
        greenTv.visibility=View.GONE
        redTv.visibility=View.GONE
        if (isCollectPage)
            return
        if (item.superChapterName.contains(mContext.getString(R.string.open_project))){
            redTv.visibility=View.VISIBLE
            redTv.setText(R.string.project)
//            redTv.setTextColor(ContextCompat.getColor(mContext,R.color.light_deep_red))
//            redTv.setBackgroundResource(R.drawable.shape_tag_red_background_normal)
        }
        if (item.superChapterName.contains(mContext.getString(R.string.navigation))){
            redTv.visibility=View.VISIBLE
            redTv.setText(R.string.navigation)
//            redTv.setTextColor(ContextCompat.getColor(mContext,R.color.light_deep_red))
//            redTv.setBackgroundResource(R.drawable.shape_tag_red_background_normal)
        }

       with(item.niceDate){
           //一天之内显示 新tag标签
           if (contains(mContext.getString(R.string.minute))||
                   contains(mContext.getString(R.string.hour))||
                   contains(mContext.getString(R.string.one_day))){
               with(greenTv){
                   visibility=View.VISIBLE
//                   setTextColor(ContextCompat.getColor(mContext,R.color.light_green))
//                   setBackgroundResource(R.drawable.shape_tag_green_background)
               }
           }
       }
    }

}