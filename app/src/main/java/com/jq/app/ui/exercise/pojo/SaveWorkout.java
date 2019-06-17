package com.jq.app.ui.exercise.pojo;

import java.util.List;

public class SaveWorkout {
    private List<DataItem> data;
    private String email;
    private String planid;

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public List<DataItem> getData() {
        return this.data;
    }

    public void setPlanid(String planid) {
        this.planid = planid;
    }

    public String getPlanid() {
        return this.planid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
