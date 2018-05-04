package com.example.wanandroidtest.di.module

import com.example.wanandroidtest.BuildConfig
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.di.qualifier.WanAndroidUrl
import com.example.wanandroidtest.other.http.api.GeeksApis
import com.example.wanandroidtest.other.http.cookies.CookiesManager
import com.example.wanandroidtest.util.CommonUtil
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Administrator on 2018/4/20 0020.
 */
@Module
class HttpModule {

    /***
     * @param retrofit 该参数是通过AppModule传递进来的
     */
    @Singleton
    @Provides
    internal fun provideGeeksApi(@WanAndroidUrl retrofit: Retrofit): GeeksApis {
        return retrofit.create(GeeksApis::class.java)
    }

    @Singleton
    @Provides
    @WanAndroidUrl
    internal fun provideGeeksRetrofit(builder: Retrofit.Builder, client: OkHttpClient): Retrofit {
        return  createRetrofit(builder,client,GeeksApis.BASE_URL)
    }

    private fun createRetrofit(builder: Retrofit.Builder, client: OkHttpClient, url: String): Retrofit {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    @Singleton
    @Provides
    internal fun provideRetrofitBuilder() :Retrofit.Builder=
            Retrofit.Builder()

    @Singleton
    @Provides
    internal fun provideOkHttpBuilder():OkHttpClient.Builder=OkHttpClient.Builder()

    @Singleton
    @Provides
    internal fun provideClient(builder: OkHttpClient.Builder): OkHttpClient {
        if (BuildConfig.DEBUG){
            val loggingInterceptor=HttpLoggingInterceptor()
            loggingInterceptor.level=HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(loggingInterceptor)
        }
        val file= File(Constants.PATH_CACHE)
        val cache=Cache(file,1024*1024*50)
        val cacheInterceptor=Interceptor{
            var request=it.request()
            if (!CommonUtil.isNetworkConnected())
                request=request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            val response=it.proceed(request)
            if (CommonUtil.isNetworkConnected()){
                //有网络时，不缓存，最大保存时长为0
                val maxAge=0
                response.newBuilder()
                        .header("Cache-Control","public,max-age=$maxAge")
                        .removeHeader("Pragma")
                        .build()
            }else{
                //无网络时，设置超时为4周
                val maxStale=60*60*24*28
                response.newBuilder()
                        .header("Cache-Control","public,only-if-cached,max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
            }
            response
        }
        with(builder){
            //设置缓存
            addNetworkInterceptor(cacheInterceptor)
            addInterceptor(cacheInterceptor)
            this.cache(cache)
            //设置超时
            connectTimeout(10,TimeUnit.SECONDS)
            readTimeout(20,TimeUnit.SECONDS)
            writeTimeout(20,TimeUnit.SECONDS)
            //错误重连
            retryOnConnectionFailure(true)
            //cookie认证,用户账号和密码保存，便于自动登陆之类的
            cookieJar(CookiesManager())
            return build()
        }
    }
}