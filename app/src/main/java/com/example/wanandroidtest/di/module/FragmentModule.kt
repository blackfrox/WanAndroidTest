package com.example.wanandroidtest.di.module

import android.support.v4.app.Fragment
import com.example.wanandroidtest.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * Created by Administrator on 2018/4/21 0021.
 */
@Module
class FragmentModule(val fragment: Fragment) {

    @Provides
    @FragmentScope
    internal fun provideActivity() = fragment.activity
}