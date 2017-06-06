package com.paringer.newsapireader.view.interfaces;

import com.paringer.newsapireader.viewmodel.MyAdapter;

/**
 * Created by Zhenya on 02.06.2017.
 */

public interface AdapterClickListener<T> {
    void onItemClick(int which, T data, MyAdapter.ViewHolder v);
}
