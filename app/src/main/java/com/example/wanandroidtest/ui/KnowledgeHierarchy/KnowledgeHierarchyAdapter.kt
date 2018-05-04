package com.example.wanandroidtest.ui.KnowledgeHierarchy

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wanandroidtest.R
import com.example.wanandroidtest.other.bean.hierarchy.KnowledgeHierarchyData
import com.example.wanandroidtest.util.CommonUtil
import kotlinx.android.synthetic.main.item_knowledge_hierarchy.view.*

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class KnowledgeHierarchyAdapter(layoutResId: Int,val mList: List<KnowledgeHierarchyData>)
    :BaseQuickAdapter<KnowledgeHierarchyData,BaseViewHolder>(layoutResId,mList) {

    override fun convert(helper: BaseViewHolder, item: KnowledgeHierarchyData) {
        with(item){
            if (name==null)
                return
            with(helper){
                setText(R.id.item_knowledge_hierarchy_title,name)
                setTextColor(R.id.item_knowledge_hierarchy_title,CommonUtil.randomColor())
                if (children==null)
                    return
                val content=StringBuilder()
                for (data in children)
                    content.append(data.name).append(" ")
                setText(R.id.item_knowledge_hierarchy_content,content.toString())
            }
        }
    }
}