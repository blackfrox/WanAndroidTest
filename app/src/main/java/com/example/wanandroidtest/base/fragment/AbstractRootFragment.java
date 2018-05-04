package com.example.wanandroidtest.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.wanandroidtest.R;
import com.example.wanandroidtest.base.BaseView;
import com.example.wanandroidtest.base.presenter.BasePresenter;
import com.example.wanandroidtest.other.bean.BaseResponse;
import com.example.wanandroidtest.util.CommonUtil;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 为什么java中BasePresenter后面的泛型可以省略
 * Created by Administrator on 2018/4/22 0022.
 */

public abstract class AbstractRootFragment<T extends BasePresenter> extends BaseFragment<T> implements BaseView{

    private static final int STATE_NORMAL=0;
    private static final int STATE_LOADING=1;
    private static final int STATE_ERROR=2;
    private int currentState=STATE_NORMAL;

    private LottieAnimationView mLottieAnimationView;
    private View mLoadingView;
    private View mErrorView;
    private ViewGroup mNormalView;


    @Override
    public void initEvent(@Nullable Bundle savedInstanceState) {
        if (getView()==null)
            return;
        mNormalView=getView().findViewById(R.id.normal_view);
        if (mNormalView==null)
            throw new IllegalStateException(
                    "The subclass of RootActivity must contain a View named" +
                            "normal_view");
        if (!(mNormalView.getParent() instanceof ViewGroup))
            throw new IllegalStateException("mNormalView's parentView must be a ViewGroup");
        ViewGroup mParent= (ViewGroup) mNormalView.getParent();
        //省略mNormalView需要父布局
        View.inflate(getActivity(),R.layout.loading_view,mParent);
        View.inflate(getActivity(),R.layout.error_view,mParent);
        mLoadingView=mParent.findViewById(R.id.loading_group);
        mErrorView=mParent.findViewById(R.id.error_group);
        TextView reloadTv=mErrorView.findViewById(R.id.error_reload_tv);
        reloadTv.setOnClickListener(v -> reload());
        mLottieAnimationView=mLoadingView.findViewById(R.id.loading_animation);
        mErrorView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mNormalView.setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        if (currentState==STATE_LOADING)
            return;
        hideCurrentView();
        mLoadingView.setVisibility(View.VISIBLE);
        mLottieAnimationView.setAnimation("loading_bus.json");
        mLottieAnimationView.playAnimation();
        currentState=STATE_LOADING;
    }

    public void showError() {
        if (currentState==STATE_ERROR)
            return;
        hideCurrentView();
        hideCurrentView();
        mErrorView.setVisibility(View.VISIBLE);
        currentState=STATE_ERROR;
    }

    public void showNormal() {
        if (currentState==STATE_NORMAL)
            return;
        hideCurrentView();
        mNormalView.setVisibility(View.VISIBLE);
        currentState=STATE_NORMAL;
    }

    private void hideCurrentView(){
        switch (currentState){
            case STATE_NORMAL:
                mNormalView.setVisibility(View.INVISIBLE);
            case STATE_LOADING:
                mLottieAnimationView.cancelAnimation();
                mLoadingView.setVisibility(View.GONE);
            case STATE_ERROR:
                mErrorView.setVisibility(View.GONE);
            default:
                break;
        }
    }

    public abstract void reload();
}

