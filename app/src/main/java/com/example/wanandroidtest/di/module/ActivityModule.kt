package com.example.wanandroidtest.di.module

import android.app.Activity
import com.example.wanandroidtest.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * Created by Administrator on 2018/4/21 0021.
 */
@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    @ActivityScope
    internal fun provideActivity()=activity

}