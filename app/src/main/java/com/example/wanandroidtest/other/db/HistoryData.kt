package com.example.wanandroidtest.other.db


import org.litepal.crud.DataSupport

/**
 * @author quchao
 * @date 2018/3/5
 */

class HistoryData(var date: Long = 0L, var data: String?=null, var id: Int = 0) : DataSupport()
