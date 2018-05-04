package com.example.wanandroidtest.ui.main

import android.app.DialogFragment
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import com.example.wanandroidtest.R
import com.example.wanandroidtest.app.Constants
import com.example.wanandroidtest.base.fragment.BaseDialogFragment
import com.example.wanandroidtest.contract.main.SearchContract
import com.example.wanandroidtest.other.DataManager
import com.example.wanandroidtest.other.bean.BaseResponse
import com.example.wanandroidtest.other.bean.main.search.TopSearchData
import com.example.wanandroidtest.other.db.HistoryData
import com.example.wanandroidtest.presenter.main.SearchPresenter
import com.example.wanandroidtest.util.CommonUtil
import com.example.wanandroidtest.util.JudgeUtils
import com.example.wanandroidtest.util.KeyBoardUtils
import com.example.wanandroidtest.widget.CircularRevealAnim
import com.jakewharton.rxbinding2.view.RxView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.search_toolbar.*
import org.jetbrains.anko.act
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Administrator on 2018/4/26.
 */
class SearchDialogFragment : BaseDialogFragment<SearchPresenter>(), SearchContract.View, ViewTreeObserver.OnPreDrawListener {

    @Inject
    lateinit var dataManager: DataManager

    override fun initInject() {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle)
        fragmentComponent.inject(this)
    }

    override fun onStart() {
        super.onStart()
        initDialog()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun initEvent(savedInstanceState: Bundle?) {
        initCircleAnimation()
        //是否显示tintText
        searchEdit.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
//                if (TextUtils.isEmpty(searchEdit.text.toString()))
                if (TextUtils.isEmpty(s))
                    searchTintTv.setText(R.string.search_tint)
                else
                    searchTintTv.text=""
            }
        })
        RxView.clicks(searchTv) //防止连续点击
                .throttleFirst(Constants.CLICK_TIME_AREA, TimeUnit.MILLISECONDS)
                .filter { !TextUtils.isEmpty(searchEdit.text.toString()) }
                .subscribe {
                        mPresenter.addHistoryData(searchEdit.text.toString().trim())
//                        setHistoryTvStatus(false) 这一步不用写，因为反正是跳转到一个activity，dialogFragment是关闭的，何必多此一举，还更新一下历史记录?
                }

//        searchTv.setOnClickListener {
//            if (!TextUtils.isEmpty(searchEdit.text.toString().trim())){
//                mPresenter.addHistoryData(searchEdit.text.toString().trim())
//                setHistoryTvStatus(false)
//            }
//        }
        search_back_ib.setOnClickListener {
            backEvent()
        }

//        search_floating_action_btn.setOnClickListener {
////            scrollView.smoothScrollTo(0,0)
//            historySearchAdapter.replaceData(arrayListOf())
////            setHistoryTvStatus(true)
//        }

        clearAllTv.setOnClickListener {
            dataManager.clearHistoryData()
            historySearchAdapter.replaceData(arrayListOf())
            setHistoryTvStatus(true)
        }

        showHistoryData(dataManager.loadAllHistoryData())
        mPresenter.getTopSearchData()
    }

    override fun showTopSearchData(topSearchDataResponse: BaseResponse<MutableList<TopSearchData>>) {

        val mList=topSearchDataResponse.data
        searchFlowLayout.adapter=object : TagAdapter<TopSearchData>(mList){
            override fun getView(parent: FlowLayout?, position: Int, data: TopSearchData?): View {
                assert(activity!=null)
                val tv=LayoutInflater.from(activity).inflate(R.layout.flow_layout_tv,parent,false) as TextView
                assert(data!=null)
                val name=data!!.name
                tv.text=name
                setItemBackground(tv)
                return tv
            }
        }
        searchFlowLayout.setOnTagClickListener { view, position, parent ->
            mPresenter.addHistoryData(mList[position].name.trim())
            setHistoryTvStatus(false)
            searchEdit.setText(mList[position].name.trim())
            searchEdit.setSelection(searchEdit.text.length)
            true
        }
    }

    private lateinit var historySearchAdapter: HistorySearchAdapter
    override fun showHistoryData(historyDataList: List<HistoryData>) {
        if (historyDataList.size<1){
            setHistoryTvStatus(true)
            return
        }
        setHistoryTvStatus(false)
        Collections.reverse(historyDataList) //按原顺序的反向顺序返回list
        historySearchAdapter=HistorySearchAdapter(R.layout.item_search_history,historyDataList)
        historySearchAdapter.setOnItemClickListener { adapter, view, position ->
            val item=adapter.data[position] as HistoryData
            if (item.data!=null)
                mPresenter.addHistoryData(item.data!!)
            searchEdit.setText(item.data)
            searchEdit.setSelection(searchEdit.text.length)
        }
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.adapter=historySearchAdapter
    }

    override fun showTopSearchDataFail() {
        CommonUtil.showSnackMessage(activity,getString(R.string.failed_to_obtain_top_data))
    }

    override fun judgeToTheSearchListActivity(text: String) {
        backEvent()
        JudgeUtils.startSearchListActivity(activity,text)
    }

    private fun setItemBackground(tv: TextView){
        tv.setBackgroundColor(CommonUtil.randomTagColor())
        tv.setTextColor(ContextCompat.getColor(activity,R.color.white))
    }

    private lateinit var mCircularRevealAnim: CircularRevealAnim
    private fun initCircleAnimation(){
        mCircularRevealAnim= CircularRevealAnim()
        mCircularRevealAnim.setAnimListener(object : CircularRevealAnim.AnimListener{
            override fun onHideAnimationEnd() {
                searchEdit.setText("")
                dismiss()
            }

            override fun onShowAnimationEnd() {
                KeyBoardUtils.openKeyboard(activity,searchEdit)
            }
        })
        searchEdit.viewTreeObserver.addOnPreDrawListener(this)
    }


    override fun onPreDraw(): Boolean {
        searchEdit.viewTreeObserver.removeOnPreDrawListener(this)
        mCircularRevealAnim.show(searchEdit,mRootView)
        return true
    }

    private fun initDialog(){
        val window=dialog.window
        val metrics=resources.displayMetrics
        //DialogSearch的宽
        val width=(metrics.widthPixels*0.98).toInt()
        assert(window!=null)
        window.setLayout(width,WindowManager.LayoutParams.MATCH_PARENT)
        window.setGravity(Gravity.TOP)
        //取消过渡动画，使DialogSearch的出现更加平滑
        window.setWindowAnimations(R.style.DialogEmptyAnimation)
    }

    //关闭键盘和显示关闭动画
     fun backEvent(){
        KeyBoardUtils.closeKeyboard(activity,searchEdit)
        mCircularRevealAnim.hide(searchEdit,mRootView)
    }


    /** 什么时候调用:
     * 1 点击tag标签  2 点击搜索按钮 3showHistoryData() 即初始化
     * @param isClearAll 当前是否是清空状态
     */
    private fun setHistoryTvStatus(isClearAll: Boolean){
        val drawable: Drawable
        clearAllTv.isEnabled=!isClearAll
        if (isClearAll){
            search_history_null_tint_tv.visibility=View.VISIBLE
            clearAllTv.setTextColor(ContextCompat.getColor(act,R.color.search_grey_gone))
            drawable= ContextCompat.getDrawable(activity,R.drawable.ic_clear_all_gone)!!
        }else{
            search_history_null_tint_tv.visibility=View.GONE
            clearAllTv.setTextColor(ContextCompat.getColor(activity,R.color.search_grey))
            drawable= ContextCompat.getDrawable(activity,R.drawable.ic_clear_all)!!
        }
        drawable.setBounds(0,0,drawable.minimumWidth,drawable.minimumHeight)
        clearAllTv.setCompoundDrawables(drawable,null,null,null)
        clearAllTv.compoundDrawablePadding=CommonUtil.dp2px(6f)
    }

}