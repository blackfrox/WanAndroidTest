package com.example.wanandroidtest.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.wanandroidtest.R;
import com.example.wanandroidtest.app.GeeksApp;
import com.example.wanandroidtest.base.BaseView;
import com.example.wanandroidtest.base.presenter.AbstractPresenter;
import com.example.wanandroidtest.di.component.DaggerFragmentComponent;
import com.example.wanandroidtest.di.component.FragmentComponent;
import com.example.wanandroidtest.util.CommonUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/22 0022.
 */

public abstract class BaseFragment<T extends AbstractPresenter> extends AbstractFragment implements BaseView {

    @Inject
    protected T mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
//        if (mPresenter!=null)
//            mPresenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        if (mPresenter!=null)
            mPresenter.attachView(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mPresenter!=null)
            mPresenter.detachView();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
//        if (mPresenter!=null)
//            mPresenter.detachView();
        super.onDestroyView();
    }

    public FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(GeeksApp.Companion.getAppComponent())
                .build();
    }

    @Override
    public void showErrorMsg(@NotNull String msg) {
        if (isAdded())
            CommonUtil.INSTANCE.showSnackMessage(getActivity(),msg);
    }


    @Override
    public void showCollectFail(@NotNull String errorMsg) {
        CommonUtil.INSTANCE.showSnackMessage(getActivity(),errorMsg);
    }

    @Override
    public void showCancelCollectFail() {
        CommonUtil.INSTANCE.showSnackMessage(getActivity(),getString(R.string.cancel_collect_fail));
    }

    /**
     * 注入当前Fragment所需的依赖
     */
    protected abstract void initInject();



}
