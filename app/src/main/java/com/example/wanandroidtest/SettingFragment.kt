package com.example.wanandroidtest

import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.app.GeeksApp
import com.example.wanandroidtest.component.ACache
import com.example.wanandroidtest.component.RxBus
import com.example.wanandroidtest.component.event.NightModeEvent
import com.example.wanandroidtest.di.component.DaggerFragmentComponent
import com.example.wanandroidtest.di.component.FragmentComponent
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.http.api.GeeksApis
import com.youth.banner.loader.ImageLoader
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.io.File
import javax.inject.Inject

class SettingFragment: PreferenceFragment(){

    companion object {
        val instance by lazy { SettingFragment() }
    }

    @Inject
    lateinit var mDataManager: DataManager
    private val cacheFile by lazy { File(Constants.PATH_CACHE) }
    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerFragmentComponent.builder()
                .appComponent(GeeksApp.appComponent)
                .build()
                .inject(this)
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_settings)

        val cachePreference=findPreference(getString(R.string.pref_key_cache_clean))
        cachePreference.summary=ACache.getCacheSize(cacheFile)
    }


    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference): Boolean {
        val key=preference.key
        when{
            //自动缓存
            key==getString(R.string.pref_key_auto_cache)->{
                val checkBoxPreference=findPreference(key) as CheckBoxPreference
                mDataManager.autoCacheState=checkBoxPreference.isChecked
            }
            //无图模式
            key == getString(R.string.pref_key_no_image) ->{
                val checkBoxPreference=findPreference(key) as CheckBoxPreference
                mDataManager.imageState=checkBoxPreference.isChecked
            }
//            //夜间模式
//            key ==getString(R.string.pref_key_night_mode) -> {
//               val checkBoxPreference= findPreference(key) as CheckBoxPreference
//                if (checkBoxPreference.isChecked)
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                else
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                mDataManager.nightModeState=checkBoxPreference.isChecked
////                RxBus.instance.post(NightModeEvent()) //通知MainActivity切换
//                activity.recreate()
//            }
            //缓存清理
            key == getString(R.string.pref_key_cache_clean) ->{
                if(ACache.deleteDir(cacheFile)) {
                    val preference=findPreference(key)
                    preference.summary="0k"
                    toast(R.string.cache_clean_success)
                }
                else
                    toast(R.string.cache_clean_failed)
            }
            //意见反馈
            key == getString(R.string.pref_key_feedback) ->{ }
        }
        return false
    }
}