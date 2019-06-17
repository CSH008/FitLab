package com.jq.app.ui.exercise.pojo;

public class VideoItem {
    private HeaderValuesItem header_values;
    private String id;
    private int order_number;

    public void setHeaderValues(HeaderValuesItem headerValues) {
        this.header_values = headerValues;
    }

    public HeaderValuesItem getHeaderValues() {
        return this.header_values;
    }

    public void setOrderNumber(int orderNumber) {
        this.order_number = orderNumber;
    }

    public int getOrderNumber() {
        return this.order_number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
