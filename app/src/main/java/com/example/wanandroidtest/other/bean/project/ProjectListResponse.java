package com.example.wanandroidtest.other.bean.project;


import com.example.wanandroidtest.other.bean.BaseResponse;

/**
 * @author quchao
 * @date 2018/2/24
 */

public class ProjectListResponse extends BaseResponse {

    private ProjectListData data;

    public ProjectListData getData() {
        return data;
    }

    public void setData(ProjectListData data) {
        this.data = data;
    }
}
