package com.example.wanandroidtest.app

import android.app.Application
import com.example.wanandroidtest.di.component.DaggerAppComponent
import com.example.wanandroidtest.di.module.AppModule
import com.example.wanandroidtest.di.module.HttpModule
import com.squareup.leakcanary.LeakCanary
import org.litepal.LitePal
import kotlin.properties.Delegates

/**
 * Created by Administrator on 2018/4/20 0020.
 */
class GeeksApp : Application() {

    companion object {
        var instance by Delegates.notNull<GeeksApp>()
        val refWatcher by lazy {
           LeakCanary.install(instance)
        }
        val appComponent by lazy {
            DaggerAppComponent
                    .builder()
                    .appModule(AppModule(instance))
                    .httpModule(HttpModule())
                    .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance=this
        LitePal.initialize(this)

    }
}