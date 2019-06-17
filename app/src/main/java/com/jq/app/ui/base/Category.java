package com.jq.app.ui.base;

public class Category {
    String cat_name;
    boolean selected_status;

    public Category( String cat_name, boolean selected_status) {

        this.cat_name = cat_name;
        this.selected_status = selected_status;
    }


    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public boolean isSelected_status() {
        return selected_status;
    }

    public void setSelected_status(boolean selected_status) {
        this.selected_status = selected_status;
    }
}
