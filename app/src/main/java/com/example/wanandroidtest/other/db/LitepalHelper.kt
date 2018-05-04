package com.example.wanandroidtest.other.db

import org.litepal.crud.DataSupport
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/21 0021.
 */
class LitepalHelper @Inject internal constructor() : DbHelper {
    override fun addHistoryData(data: String): List<HistoryData> {
        //存在，更新
        var mList = DataSupport.findAll(HistoryData::class.java)
        mList.filter { it.data == data }
                .forEach {
                    it.date = System.currentTimeMillis()
                    it.save()
                    return mList
                }
        //不存在，添加
        HistoryData(System.currentTimeMillis(), data)
                .save()
        mList = DataSupport.findAll(HistoryData::class.java)
        if (mList.size > 10) //只允许保存10条数据
            mList.removeAt(0)
        return mList
    }

    override fun clearHistoryData() {
        DataSupport.deleteAll(HistoryData::class.java)
    }

    override fun loadAllHistoryData(): List<HistoryData> {
        return DataSupport.findAll(HistoryData::class.java)
    }

}