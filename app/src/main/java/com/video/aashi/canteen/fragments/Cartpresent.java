package com.video.aashi.canteen.fragments;

import com.video.aashi.canteen.arrays.ItemList;

import java.util.List;

public interface Cartpresent {

    void showMesage(String s);
    void dismissProgress();
    void showProgress();
    void  addItems(List<ItemList> itemListList);
    void showLocal();




}
