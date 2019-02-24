package com.vu.tab.servy.bean;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    List<CommonBean> bankBranchList=new ArrayList<>();
    String status;

    public List<CommonBean> getList() {
        return bankBranchList;
    }

    public void setList(List<CommonBean> list) {
        this.bankBranchList = list;
    }
}
