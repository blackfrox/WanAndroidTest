package com.example.wanandroidtest.other.pref

import android.content.Context
import android.content.SharedPreferences
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.app.GeeksApp
import javax.inject.Inject

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
/**
* Created by Administrator on 2018/4/20 0020.
*/
class PreferenceHelperImpl @Inject
internal constructor(): PreferenceHelper {

//    private val SHARED_PREFERENCE = "my_shared_preference"

    private val mPreferences: SharedPreferences
            by lazy { GeeksApp.instance.getSharedPreferences( "my_shared_preference", Context.MODE_PRIVATE) }


    override var loginAccount: String
        get() = mPreferences.getString(Constants.ACCOUNT)
        set(value) = mPreferences.putString(Constants.ACCOUNT, value)

    override var loginPassword: String
        get() = mPreferences.getString(Constants.PASSWORD)
        set(value) {
            mPreferences.putString(Constants.PASSWORD, value)
        }

    override var loginStatus: Boolean
        get() = mPreferences.getBoolean(Constants.LOGIN_STATUS)
        set(value) {
            mPreferences.putBoolean(Constants.LOGIN_STATUS, value)
        }

    override var currentPage: Int
        get() = mPreferences.getInt(Constants.CURRENT_PAGE)
        set(value) = mPreferences.putInt(Constants.CURRENT_PAGE, value)

    override var projectCurrentPage: Int
        get() = mPreferences.getInt(Constants.PROJECT_CURRENT_PAGE)
        set(value) = mPreferences.putInt(Constants.PROJECT_CURRENT_PAGE, value)

    override var autoCacheState: Boolean
        get() = mPreferences.getBoolean(Constants.AUTO_CACHE_STATE)
        set(value) = mPreferences.putBoolean(Constants.AUTO_CACHE_STATE, value)

    override var nightModeState: Boolean
        get() = mPreferences.getBoolean(Constants.NIGHT_MODE_STATE)
        set(value) = mPreferences.putBoolean(Constants.NIGHT_MODE_STATE, value)

    override var imageState: Boolean
        get() = mPreferences.getBoolean(Constants.NO_IMAGE_STATE)
        set(value) = mPreferences.putBoolean(Constants.NO_IMAGE_STATE, value)

    private fun SharedPreferences.getInt(key: String, value: Int = 0): Int =
            getInt(key, value)


    private fun SharedPreferences.putInt(key: String, value: Int) =
            edit().putInt(key, value).apply()

    private fun SharedPreferences.getBoolean(key: String, value: Boolean = false): Boolean =
            getBoolean(key, value)


    private fun SharedPreferences.putBoolean(key: String, value: Boolean) =
            edit().putBoolean(key, value).apply()

    private fun SharedPreferences.getString(key: String, value: String = "") =
            getString(key, value)

    private fun SharedPreferences.putString(key: String, value: String) =
            edit().putString(key, value).apply()
}