package com.jq.app.ui.noteList;

import java.util.ArrayList;
import java.util.List;

public class Response {
    private List<DataItem> data = new ArrayList();
    private String message;
    private String status;
    private String userId;

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public List<DataItem> getData() {
        return this.data;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Response{data = '");
        stringBuilder.append(this.data);
        stringBuilder.append('\'');
        stringBuilder.append(",user_id = '");
        stringBuilder.append(this.userId);
        stringBuilder.append('\'');
        stringBuilder.append(",message = '");
        stringBuilder.append(this.message);
        stringBuilder.append('\'');
        stringBuilder.append(",status = '");
        stringBuilder.append(this.status);
        stringBuilder.append('\'');
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
