package com.kcumendigital.democratic.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class CreateForumAdapter extends BaseAdapter {

    Context context;
    List<CreateForumAdapter> data;

    public CreateForumAdapter(Context context, List<CreateForumAdapter> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {return data.size();}

    @Override
    public Object getItem(int position) {return data.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {


        return  null;
    }
}
