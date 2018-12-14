package com.ldx.baseutils.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldx.baseutils.R;
import com.ldx.baseutils.mvp.base.BaseListAdapter;

/**
 * Created by babieta on 2018/12/13.
 */

public class MainAdapter extends BaseListAdapter<String> {
    private Context mContext;

    public  MainAdapter(Context context){
        this.mContext =context;
    }

    @Override
    public View setView(int position, View convertView, ViewGroup parent) {
        MainHolder holder = null;
        if (convertView == null){
            holder = new MainHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item,null,false);
            holder.text = convertView.findViewById(R.id.item_text);
            convertView.setTag(holder);
        }else {
            holder = (MainHolder) convertView.getTag();
        }
            String s = list.get(position);
            holder.text.setText(s);
        return convertView;
    }
    class  MainHolder {
        TextView text;
    }
}
