package com.example.wanandroidtest.base.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.example.wanandroidtest.app.GeeksApp;
import com.example.wanandroidtest.base.BaseView;
import com.example.wanandroidtest.base.presenter.BasePresenter;
import com.example.wanandroidtest.di.component.ActivityComponent;
import com.example.wanandroidtest.di.component.DaggerActivityComponent;
import com.example.wanandroidtest.di.module.ActivityModule;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AbstractActivity implements BaseView {

    @Inject
    public T mPresenter;

    protected ActivityComponent getActivityComponent(){
        return DaggerActivityComponent.builder()
                .appComponent(GeeksApp.Companion.getAppComponent())
                .build();
    }

    @Override
    public void onViewCreated() {
        if (mPresenter!=null)
            mPresenter.attachView(this);
        super.onViewCreated();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }



    @Override
    public void setNightMode(boolean nightModeState) {
        if (nightModeState){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        recreate();
    }
}
