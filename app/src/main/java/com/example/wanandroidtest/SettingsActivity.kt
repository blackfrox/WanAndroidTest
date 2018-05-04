package com.example.wanandroidtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.wanandroidtest.util.StatusBarUtil
import kotlinx.android.synthetic.main.toolbar.*

/**
 */
class SettingsActivity : AppCompatActivity() {

    private val fragment = SettingFragment.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupActionBar()
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        mTitleTv.setText(R.string.setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

}
