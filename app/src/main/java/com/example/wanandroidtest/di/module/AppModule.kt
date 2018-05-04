package com.example.wanandroidtest.di.module

import com.example.wanandroidtest.app.GeeksApp
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.db.DbHelper
import com.example.wanandroidtest.other.db.LitepalHelper
import com.example.wanandroidtest.other.http.HttpHelper
import com.example.wanandroidtest.other.http.RetrofitHelper
import com.example.wanandroidtest.other.http.api.GeeksApis
import com.example.wanandroidtest.other.pref.PreferenceHelper
import com.example.wanandroidtest.other.pref.PreferenceHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Administrator on 2018/4/20 0020.
 * 除了第一个，第二~第四个方法都是在该类中直接实例化(通过@Inject在构造方法中)
 */
@Module
class AppModule(private val application: GeeksApp) {

    @Singleton
    @Provides
    internal fun provideApplication() = application

    /**
     * 因为下面三个实例是通过接口实现的，所以返回类型也需要变为接口类型，
     * 不然dagger2无法找到@Inject标注的构造方法实例化该类
     * //实例化两种思路： 1 直接类实例化
     *                    2 在该类的构造方法中@Inject注解
     */
    @Singleton
    @Provides
    internal fun provideHttpHelper(retrofitHelper: RetrofitHelper): HttpHelper {
        return retrofitHelper
    }

    @Singleton
    @Provides
    internal fun provideDbHelper(litepalHelper: LitepalHelper): DbHelper = litepalHelper

    @Singleton
    @Provides
    internal fun providePreferenceHelper(preferenceHelperImpl: PreferenceHelperImpl): PreferenceHelper =
         preferenceHelperImpl

    @Singleton
    @Provides
    internal fun provideDataManager(httpHelper: HttpHelper, dbHelper: DbHelper, preferenceHelper: PreferenceHelper): DataManager =
            DataManager(httpHelper, dbHelper, preferenceHelper)

}