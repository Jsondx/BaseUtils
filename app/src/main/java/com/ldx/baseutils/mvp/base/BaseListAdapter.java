package com.ldx.baseutils.mvp.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/6/27
 */

public abstract class BaseListAdapter<T> extends BaseAdapter {

    public List<T> list = new ArrayList<>();

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return setView(position, convertView, parent);
    }

    public abstract View setView(int position, View convertView, ViewGroup parent);

}
