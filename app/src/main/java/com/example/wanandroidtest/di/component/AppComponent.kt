package com.example.wanandroidtest.di.component

import com.example.wanandroidtest.app.GeeksApp
import com.example.wanandroidtest.di.module.AppModule
import com.example.wanandroidtest.di.module.HttpModule
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.db.LitepalHelper
import com.example.wanandroidtest.other.http.RetrofitHelper
import com.example.wanandroidtest.other.pref.PreferenceHelperImpl
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Administrator on 2018/4/20 0020.
 * 提供给外部的实例
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, HttpModule::class))
interface AppComponent {

    val context: GeeksApp

    val retrofitHelper: RetrofitHelper

    val dataManager: DataManager

    val litepalHelper: LitepalHelper

    val preferenceHelper: PreferenceHelperImpl


}