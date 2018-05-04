package com.example.wanandroidtest.app

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.wanandroidtest.R

/**
 * 配合PreferenceFragment使用，
 * 但是这里是用dataManager保存读取了
 */
class Settings(val context: Context) {

    private val mSharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    fun getAutoCache(): Boolean {
       return getBoolean(R.string.pref_key_auto_cache)
    }

    fun getNoImageMode()=
            getBoolean(R.string.pref_key_no_image)

    fun getNightMode()=
            getBoolean(R.string.pref_key_night_mode)


    private fun getString(id: Int)=
            context.getString(id)

    private fun getBoolean(id: Int)=
            mSharedPreferences.getBoolean(getString(id),false)
}