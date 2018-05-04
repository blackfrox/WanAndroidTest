package com.example.wanandroidtest.ui.navigation

import android.app.ActivityOptions
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wanandroidtest.R
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.navigation.NavigationListData
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

/**
 * Created by Administrator on 2018/4/28.
 */
class NavigationAdapter(layoutRes: Int,mList: List<NavigationListData>): BaseQuickAdapter<NavigationListData,BaseViewHolder>(layoutRes,mList) {

    override fun convert(helper: BaseViewHolder, item: NavigationListData) {
        with(helper){
            with(item){
                if (!TextUtils.isEmpty(name))
                    setText(R.id.item_navigation_tv,name)
                val mTagFlowLayout=getView<TagFlowLayout>(R.id.item_navigation_flow_layout)
                mTagFlowLayout.adapter=object : TagAdapter<FeedArticleData>(articles){
                    override fun getView(parent: FlowLayout, position: Int, t: FeedArticleData?): View? {
                        if (t==null) return null
                        val tv=LayoutInflater.from(parent.context)
                                .inflate(R.layout.flow_layout_tv,mTagFlowLayout,false) as TextView
                        with(tv){
                            setPadding(CommonUtil.dp2px(10f),CommonUtil.dp2px(10f),
                                    CommonUtil.dp2px(10f),CommonUtil.dp2px(10f))
                            text=t.title
                            setTextColor(CommonUtil.randomColor())
                        }
                        return tv
                    }
                }
                mTagFlowLayout.setOnTagClickListener { view, position, parent ->
                    val options=ActivityOptions.makeScaleUpAnimation(view,
                            view.width/2,
                            view.height/2,
                            0,0)
                    JudgeUtils.startArticleDetailActivity(parent.context,
                            options,
                            articles[position].id,
                            articles[position].title,
                            articles[position].link,
                            articles[position].isCollect,
                            false,
                            false)
                    true
                }

            }
        }
    }
}