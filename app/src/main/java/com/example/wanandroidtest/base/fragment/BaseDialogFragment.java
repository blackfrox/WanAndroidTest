package com.example.wanandroidtest.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.wanandroidtest.R;
import com.example.wanandroidtest.app.GeeksApp;
import com.example.wanandroidtest.base.BaseView;
import com.example.wanandroidtest.base.presenter.AbstractPresenter;
import com.example.wanandroidtest.di.component.DaggerFragmentComponent;
import com.example.wanandroidtest.di.component.FragmentComponent;
import com.example.wanandroidtest.util.CommonUtil;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public abstract class BaseDialogFragment<T extends AbstractPresenter> extends AbstractDialogFragment implements BaseView {

    @Inject
    public T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInject();
        if (mPresenter!=null)
            mPresenter.attachView(this);
    }


    @Override
    public void onDestroyView() {
        if (mPresenter!=null)
            mPresenter.detachView();
        super.onDestroyView();
    }

    public FragmentComponent getFragmentComponent(){
        return DaggerFragmentComponent.builder()
                .appComponent(GeeksApp.Companion.getAppComponent())
                .build();
    }

    @Override
    public void showErrorMsg(@NotNull String msg) {
        if (getActivity()!=null)
            CommonUtil.INSTANCE.showSnackMessage(getActivity(),msg);
    }

    @Override
    public void showCollectFail(@NotNull String errorMsg) {
        if (getActivity()!=null)
            CommonUtil.INSTANCE.showSnackMessage(getActivity(),errorMsg);
    }

    @Override
    public void showCancelCollectFail() {
        if (getActivity()!=null)
            CommonUtil.INSTANCE.showSnackMessage(getActivity(),getString(R.string.cancel_collect_fail));
    }

    //注入当前fragment所需的依赖
    protected abstract void initInject();
}
