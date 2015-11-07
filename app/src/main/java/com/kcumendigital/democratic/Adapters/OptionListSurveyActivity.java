package com.kcumendigital.democratic.Adapters;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kcumendigital.democratic.Models.SurveyOption;
import com.kcumendigital.democratic.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by asus on 03/11/2015.
 */
public class OptionListSurveyActivity extends BaseAdapter {
    List<SurveyOption> data;
    Context context;
    long sum;
    public Handler handler = new Handler();

    public OptionListSurveyActivity(List<SurveyOption> data, Context context, long sum) {
        this.data = data;
        this.context = context;
        this.sum = sum;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView != null) {
            v = convertView;
        }

        v = View.inflate(context, R.layout.template_survey_opcions, null);
        TextView opcion = (TextView) v.findViewById(R.id.text_opcion_survey);
        TextView percentage = (TextView) v.findViewById(R.id.percentage_value);
        float porcentaje;
        if (sum != 0) {

             porcentaje = data.get(position).getVotes() * 100f/sum;
        }
        else {
             porcentaje = 0;
        }
        opcion.setText(data.get(position).getDescription());
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        percentage.setText("" + df.format(porcentaje));
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setProgress((int) porcentaje);
        return v;

    }

    public void updateSum(long sum){
        this.sum = sum;
    }
}
