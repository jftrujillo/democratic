package com.kcumendigital.democraticcauca;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kcumendigital.democraticcauca.Adapters.OptionListSurveyActivity;
import com.kcumendigital.democraticcauca.Models.Survey;
import com.kcumendigital.democraticcauca.Models.SurveyOption;
import com.kcumendigital.democraticcauca.Models.SurveyVote;
import com.kcumendigital.democraticcauca.Models.User;
import com.kcumendigital.democraticcauca.Util.AppUtil;
import com.kcumendigital.democraticcauca.Util.ColletionsStatics;
import com.kcumendigital.democraticcauca.parse.SunshineParse;
import com.kcumendigital.democraticcauca.parse.SunshineQuery;
import com.kcumendigital.democraticcauca.parse.SunshineRecord;
import com.parse.ParseException;

import java.text.DecimalFormat;
import java.util.List;

public class SurveyDescriptionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
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
    SunshineParse parse;
    SurveyVote surveyVote;
    User user;
    static final int REQUEST_OPTION_VOTE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_description);
        user = new User();
        user = AppUtil.getUserStatic();
        surveyVote = new SurveyVote();
        Bundle bundle = getIntent().getExtras();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        parse = new SunshineParse();
        pos = bundle.getInt("pos", 0);
        boolean pager = bundle.getBoolean("pager",false);

        if(!pager) {
            data = ColletionsStatics.getDataSurvey().get(pos).getOptions();
            survey = ColletionsStatics.getDataSurvey().get(pos);
        }else {
            data = ColletionsStatics.getHomeSurvey().get(pos).getOptions();
            survey = ColletionsStatics.getHomeSurvey().get(pos);
        }

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
            Log.i("sum",""+sum);
        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        percentage = biggerOpcionNumber*100f/sum;
        progressBar.setProgress((int) percentage);
        mostVotedOpcionPercentage.setText("" + df.format(percentage));
        mostVotedOpcionText.setText(mostVotedOpcionString);
        votes.setText(""+sum);

        data = survey.getOptions();
        titulo.setText(survey.getTitle());
        adapter = new OptionListSurveyActivity(data,this,sum);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);


    }


    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        SunshineParse parseVotes = new SunshineParse();
        SunshineQuery query = new SunshineQuery();
        query.addUser("user", user.getObjectId());
        parse.getAllRecords(query, new SunshineParse.SunshineCallback() {
            @Override
            public void done(boolean success, ParseException e) {

            }

            @Override
            public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

            }

            @Override
            public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
               boolean isvoted = false;
              for (int i = 0; i<data.size();i++){
                 for (int j = 0 ; j < records.size();j++){
                     SurveyVote vote = (SurveyVote) records.get(j);
                     if (vote.getSurveyOption().equals(data.get(i).getObjectId())){
                         isvoted = true;
                     }

                 }
             }
                if (isvoted == false){
                    Toast.makeText(getApplicationContext(),"selecionno "+survey.getOptions().get(position).getDescription(),Toast.LENGTH_SHORT).show();
                    parse.incrementField(data.get(position).getObjectId(), "votes", SurveyOption.class);
                    surveyVote.setSurveyOption(ColletionsStatics.getDataSurvey().get(pos).getOptions().get(position).getObjectId());
                    surveyVote.setUser(user.getObjectId());
                    parse.insert(surveyVote, new SunshineParse.SunshineCallback() {
                        @Override
                        public void done(boolean success, ParseException e) {
                            if (success == true){
                                Log.i("succes","true");
                            }

                            else{
                                Log.i("succes","false");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

                        }

                        @Override
                        public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {

                        }
                    });
                    ColletionsStatics.getDataSurvey().get(pos).getOptions().get(position).setVotes(ColletionsStatics.getDataSurvey().get(pos).getOptions().get(position).getVotes() + 1);
                    sum = sum + 1;
                    adapter.updateSum(sum);
                    adapter.notifyDataSetChanged();
                    long sum_2 = 0;
                    mostVotedOpcionString = null;
                    biggerOpcionNumber = 0;
                    for (int i = 0; i<data.size();i++){
                        sum_2 = sum_2 + data.get(i).getVotes();
                        if(biggerOpcionNumber < data.get(i).getVotes()){
                            biggerOpcionNumber = data.get(i).getVotes();
                            mostVotedOpcionString = data.get(i).getDescription();
                        }
                        Log.i("sum",""+sum);
                    }

                    percentage = biggerOpcionNumber*100f/sum_2;
                    progressBar.setProgress((int) percentage);
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    mostVotedOpcionText.setText(mostVotedOpcionString);
                    mostVotedOpcionPercentage.setText("" + df.format(percentage));
                    votes.setText("" + sum);
                }
                else {
                    Toast.makeText(getApplicationContext(),"ya ha votado aqui",Toast.LENGTH_SHORT).show();
                }
                }




        },null,SurveyVote.class);




}
}
