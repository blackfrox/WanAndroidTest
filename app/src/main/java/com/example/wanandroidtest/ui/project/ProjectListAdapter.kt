package com.example.wanandroidtest.ui.project

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wanandroidtest.R
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.main.collect.FeedArticleData
import com.example.wanandroidtest.other.bean.project.ProjectListData
import com.example.wanandroidtest.util.ImageLoader
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/28.
 */
class ProjectListAdapter(layoutResId: Int, mList: List<FeedArticleData>): BaseQuickAdapter<FeedArticleData,BaseViewHolder>(layoutResId,mList){

    override fun convert(helper: BaseViewHolder, item: FeedArticleData) {
        with(helper){
            with(item){
                ImageLoader.load(mContext,envelopePic,helper.getView(R.id.item_project_list_iv))
                setText(R.id.item_project_list_time_tv,niceDate)
                setText(R.id.item_project_list_title_tv,title)
                setText(R.id.item_project_list_content_tv,desc)
                setText(R.id.item_project_list_author_tv,author)
                if (!TextUtils.isEmpty(apkLink))
                    helper.getView<TextView>(R.id.item_project_list_install_tv).visibility= View.VISIBLE
                else
                    helper.getView<TextView>(R.id.item_project_list_install_tv).visibility= View.GONE
                helper.addOnClickListener(R.id.item_project_list_install_tv)
            }
        }
    }


}