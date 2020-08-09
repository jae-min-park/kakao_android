package com.kakao.gridviewapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context mContext;
    int mLayout;
    ArrayList<String> mList;
    LayoutInflater mInf;

    public MyAdapter(Context context, int layout, ArrayList<String> imgs) {
        mContext = context;
        mLayout = layout;
        mList = imgs;
        mInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInf.inflate(mLayout, null);
        }
        ImageView iv = convertView.findViewById(R.id.iv);
        /*
         *  큰 차이는 없지만 Glide가 Picasso보다 약간 빨리 화면에 보여짐.
         */
        Long tempTime = System.nanoTime();
        Glide.with(mContext).load(mList.get(position)).into(iv);
//        Picasso.get().load(mList.get(position)).into(iv);

        Log.d("!TIME_IMG",mList.get(position)+" " +(System.nanoTime()-tempTime)+"\n");
        return convertView;
    }
}