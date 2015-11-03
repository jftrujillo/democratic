package com.kcumendigital.democratic.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.Models.SurveyOption;
import com.kcumendigital.democratic.R;

import java.util.List;

/**
 * Created by asus on 13/10/2015.
 */
public class OptionListAdapter extends BaseAdapter {
    List<SurveyOption> opcions;
    Context context;

    public OptionListAdapter(List<SurveyOption> options,Context context) {
        this.opcions = options;
        this.context = context;
    }

    @Override
    public int getCount() {
        return opcions.size();
    }

    @Override
    public Object getItem(int position) {
        return opcions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
          View v ;
       if (convertView != null){
        v = convertView;
        }

        v = View.inflate(context, R.layout.template_list_add_options,null);
        TextView txt = (TextView) v.findViewById(R.id.opcion_list);
        txt.setText(opcions.get(position).getDescription());
        return v;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
