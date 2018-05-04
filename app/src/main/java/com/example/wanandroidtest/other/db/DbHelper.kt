package com.example.wanandroidtest.other.db


/**
 * @author quchao
 * @date 2017/11/27
 */

interface DbHelper {

    /**
     * 增加历史数据
     *
     * @param data  added string
     * @return  List<HistoryData>
    </HistoryData> */
    fun addHistoryData(data: String): List<HistoryData>

    /**
     * Clear search history data
     */
    fun clearHistoryData()

    /**
     * Load all history data
     *
     * @return List<HistoryData>
    </HistoryData> */
    fun loadAllHistoryData(): List<HistoryData>

}
