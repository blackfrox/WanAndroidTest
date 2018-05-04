package com.example.wanandroidtest.ui.main

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wanandroidtest.R
import com.example.wanandroidtest.other.db.HistoryData
import com.example.wanandroidtest.util.CommonUtil
import org.jetbrains.anko.textColor

/**
 * Created by Administrator on 2018/4/26.
 */
class HistorySearchAdapter(layoutRes: Int,mList: List<HistoryData>): BaseQuickAdapter<HistoryData,BaseViewHolder>(layoutRes,mList){
    override fun convert(helper: BaseViewHolder, item: HistoryData) {
        with(helper){
            val tv=getView<TextView>(R.id.item_search_history_tv)
            tv.text=item.data
            tv.textColor=CommonUtil.randomColor()
        }
    }
}