package com.prolificinteractive.materialcalendarview;

import java.util.List;

/**
 * Created by cuonghc on 4/10/18.
 */

public class CustomDay {

    private Integer mDay;
    private List<String> mNote;
    private Integer mPrice;
    private Boolean mIsEnable = true;

    public CustomDay(Integer day, List<String> note, Integer price) {
        mDay = day;
        mNote = note;
        mPrice = price;
    }

    public CustomDay(Integer day, List<String> note, Integer price, Boolean isEnable) {
        mDay = day;
        mNote = note;
        mPrice = price;
        mIsEnable = isEnable;
    }

    public Integer getDay() {
        return mDay;
    }

    public void setDay(Integer day) {
        mDay = day;
    }

    public List<String> getNote() {
        return mNote;
    }

    public void setNote(List<String> note) {
        mNote = note;
    }

    public Integer getPrice() {
        return mPrice;
    }

    public void setPrice(Integer price) {
        mPrice = price;
    }

    public Boolean getEnable() {
        return mIsEnable;
    }

    public void setEnable(Boolean enable) {
        mIsEnable = enable;
    }
}
