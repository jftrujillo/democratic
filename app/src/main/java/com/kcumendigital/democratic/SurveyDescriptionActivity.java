package com.kcumendigital.democratic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.Models.SurveyOption;
import com.kcumendigital.democratic.Util.ColletionsStatics;

import java.util.List;

public class SurveyDescriptionActivity extends AppCompatActivity {
    List<SurveyOption> data;
    Survey survey;
    TextView titulo;
    TextView votes;
    TextView mostVotedOpcionText;
    TextView mostVotedOpcionPercentage;
    ListView list;
    ProgressBar progressBar;
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

        progressBar.setProgress((int) percentage);

        /*
        for (int i = 0; i<data.get(position).getOptions().size(); i++){
                if (BiggerOpcionNumber < data.get(position).getOptions().get(i).getVotes()){
                    BiggerOpcionNumber = data.get(position).getOptions().get(i).getVotes();
                    BiggerOpcion = data.get(position).getOptions().get(i).getDescription();
                }
                suma = suma + data.get(position).getOptions().get(i).getVotes();
            }
         */



        survey = ColletionsStatics.getDataSurvey().get(pos);
        data = survey.getOptions();


        titulo.setText(survey.getTitle());




    }
}
