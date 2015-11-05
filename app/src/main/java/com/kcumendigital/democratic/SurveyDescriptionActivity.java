package com.kcumendigital.democratic;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kcumendigital.democratic.Adapters.OptionListSurveyActivity;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.Models.SurveyOption;
import com.kcumendigital.democratic.Util.ColletionsStatics;

import java.util.List;

public class SurveyDescriptionActivity extends AppCompatActivity {
    List<SurveyOption> data;
    Survey survey;
    Toolbar mToolbar;
    TextView titulo;
    TextView votes;
    TextView mostVotedOpcionText;
    TextView mostVotedOpcionPercentage;
    ListView list;
    ProgressBar progressBar;
    OptionListSurveyActivity adapter;
    int pos;
    long sum= 0;
    long biggerOpcionNumber = 0;
    String mostVotedOpcionString;
    float percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_description);
        Bundle bundle = getIntent().getExtras();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        pos = bundle.getInt("pos", 0);
        data = ColletionsStatics.getDataSurvey().get(pos).getOptions();
        titulo = (TextView) findViewById(R.id.TitleSurvey);
        votes = (TextView) findViewById(R.id.VotesCount);
        list = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mostVotedOpcionPercentage = (TextView) findViewById(R.id.percentage_value_description);
        mostVotedOpcionText = (TextView) findViewById(R.id.most_voted_opcion_text);
        mostVotedOpcionString = null;
        for (int i = 0; i<data.size();i++){
            sum = sum + data.get(i).getVotes();
            if(biggerOpcionNumber < data.get(i).getVotes()){
                biggerOpcionNumber = data.get(i).getVotes();
                mostVotedOpcionString = data.get(i).getDescription();
            }
            sum = sum + data.get(i).getVotes();
        }
        percentage = biggerOpcionNumber*sum/100;
        progressBar.setProgress(50);
        survey = ColletionsStatics.getDataSurvey().get(pos);
        data = survey.getOptions();
        titulo.setText(survey.getTitle());
        adapter = new OptionListSurveyActivity(data,this,sum);
        list.setAdapter(adapter);




    }
}
