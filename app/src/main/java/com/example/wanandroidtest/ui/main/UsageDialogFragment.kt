package com.example.wanandroidtest.ui.main

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.TextView
import com.example.wanandroidtest.R
import com.example.wanandroidtest.base.fragment.BaseDialogFragment
import com.example.wanandroidtest.contract.main.UsageDialogContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.search.UsefulSiteData
import com.example.wanandroidtest.presenter.main.UsageDialogPresenter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.example.wanandroidtest.widget.CircularRevealAnim
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_usage.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.backgroundColor
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/25 0025.
 */
class UsageDialogFragment : BaseDialogFragment<UsageDialogPresenter>(), UsageDialogContract.View, ViewTreeObserver.OnPreDrawListener {

    @Inject
    lateinit var mDataManager: DataManager

    override fun initInject() {
        fragmentComponent.inject(this)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)
    }

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_usage
    }

    private lateinit var mList: MutableList<UsefulSiteData>
    override fun initEvent(savedInstanceState: Bundle?) {
        initCircleAnimation()
        initToolbar()
        mPresenter.getUsefulSites()
    }

    override fun showUsefulSites(usefulSitesResponse: BaseResponse<MutableList<UsefulSiteData>>) {
        mList = usefulSitesResponse.data
        tagFlowLayout.adapter = object : TagAdapter<UsefulSiteData>(mList) {
            override fun getView(parent: FlowLayout?, position: Int, data: UsefulSiteData?): View {
                assert(activity != null)
                val tv = LayoutInflater.from(activity).inflate(R.layout.flow_layout_tv, parent, false) as TextView
                assert(data != null)
                val name = data!!.name
                tv.text = name
                setItemBackground(tv)
                return tv
            }
        }
        tagFlowLayout.setOnTagClickListener { view, position, _ ->
            val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                ActivityOptions.makeSceneTransitionAnimation(activity, view, getString(R.string.share_view))
            else null
            JudgeUtils.startArticleDetailActivity(activity,
                    options,
                    mList[position].id,
                    mList[position].name.trim(),
                    mList[position].link.trim(),
                    false,
                    false,
                    true)
            true
        }

    }

    override fun showUsefulSitesDataFail() {
        CommonUtil.showSnackMessage(activity, getString(R.string.failed_to_obtain_useful_sites_data))
    }

    private fun initToolbar() {
        R.string.useful_sites
        mTitleTv.setText(R.string.useful_sites)
        if (mDataManager.nightModeState) {
            mTitleTv.setTextColor(ContextCompat.getColor(activity, R.color.comment_text))
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorCard))
            toolbar.setNavigationIcon(ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_white_24dp))
        } else {
            mTitleTv.setTextColor(ContextCompat.getColor(activity, R.color.title_black))
            toolbar.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))
            toolbar.setNavigationIcon(ContextCompat.getDrawable(activity, R.drawable.ic_arrow_back_grey_24dp))
        }
        toolbar.setNavigationOnClickListener({ v -> mCircularRevealAnim.hide(toolbar, mRootView) })
    }

    //    private val mPreDrawListener = object : ViewTreeObserver.OnPreDrawListener{
//        override fun onPreDraw(): Boolean {
//            mTitleTv.viewTreeObserver.removeOnPreDrawListener(this)
//            mCircularRevealAnim.show(mTitleTv,mRootView)
//            return true
//        }
//    }
    override fun onPreDraw(): Boolean {
        mTitleTv.viewTreeObserver.removeOnPreDrawListener(this)
        mCircularRevealAnim.show(mTitleTv, mRootView)
        return true
    }

    private fun setItemBackground(tv: TextView) {
        tv.backgroundColor = CommonUtil.randomTagColor()
    }
    private lateinit var mCircularRevealAnim: CircularRevealAnim

    private fun initCircleAnimation() {
        mCircularRevealAnim = CircularRevealAnim()
        mCircularRevealAnim.setAnimListener(object : CircularRevealAnim.AnimListener {
            override fun onHideAnimationEnd() {
                dismiss()
            }

            override fun onShowAnimationEnd() {

            }
        })
//        mTitleTv.viewTreeObserver.addOnPreDrawListener(this)
    }





    private fun initDialog() {
        val window = dialog.window
        val metrics = resources.displayMetrics
        //DialogSearch的宽
        val width = metrics.widthPixels * 0.98
        assert(window != null)
        window.setLayout(width.toInt(), WindowManager.LayoutParams.MATCH_PARENT)
        window.setGravity(Gravity.TOP)
        //取消过渡动画，使DialogSearch的出现更加平滑
        window.setWindowAnimations(R.style.DialogEmptyAnimation)
    }

}