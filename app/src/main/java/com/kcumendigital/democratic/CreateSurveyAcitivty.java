package com.kcumendigital.democratic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kcumendigital.democratic.Adapters.OptionListAdapter;
import com.kcumendigital.democratic.Models.Survey;
import com.kcumendigital.democratic.Models.SurveyOption;
import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.Util.AppUtil;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class CreateSurveyAcitivty extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    TextInputLayout titulo,description;
    Button button;
    Button create_new_survey;
    TextView resultText;
    ArrayList<SurveyOption> opciones;
    ListView list;
    Spinner spinner;
    OptionListAdapter adapter;
    ProgressDialog dialgog;
    User user;
    public static String DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey_acitivty);
        user = new User();




        user = AppUtil.getUserStatic();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        titulo = (TextInputLayout) findViewById(R.id.title_new_survey);
        description = (TextInputLayout) findViewById(R.id.description_create_survey);
        button = (Button) findViewById(R.id.add_options);
        create_new_survey = (Button) findViewById(R.id.crear_new_survey);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapters = ArrayAdapter.createFromResource(this,R.array.Categorias,android.R.layout.simple_spinner_item);
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapters);

        opciones = new ArrayList<>();
        if (savedInstanceState != null){
            opciones = (ArrayList<SurveyOption>) getLastCustomNonConfigurationInstance();
        }

        list = (ListView) findViewById(R.id.list);
        adapter = new OptionListAdapter(opciones,this);
        list.setAdapter(adapter);
        create_new_survey.setOnClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });








    }

    @Override
    public void onClick(View v) {
        if (opciones.size()<2){
            Toast.makeText(getApplicationContext(),"Ingrese por lo menos dos opciones",Toast.LENGTH_SHORT).show();
        }
        else {
            dialgog = ProgressDialog.show(this, "Creando Encuesta",
                    "dialog message", true);
            Survey survey = new Survey();
            survey.setTitle(titulo.getEditText().getText().toString());
            survey.setDescription(description.getEditText().getText().toString());
            survey.setCategory(spinner.getSelectedItem().toString());
            survey.setOptions(opciones);
            survey.setUser(user);
            SunshineParse parse = new SunshineParse();
            parse.insert(survey, new SunshineParse.SunshineCallback() {
                @Override
                public void done(boolean success, ParseException e) {
                    if (success == true){
                        dialgog.dismiss();
                        Toast.makeText(getApplicationContext(),"Encuesta Creada",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {

                        dialgog.dismiss();
                        Toast.makeText(getApplicationContext(),"Encuesta no Creada",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

                }

                @Override
                public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {

                }
            });

        }
    }

    protected void showInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(CreateSurveyAcitivty.this);
        View prontView = layoutInflater.inflate(R.layout.input_dialog_opctions_survey, null);
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(CreateSurveyAcitivty.this);
        alertdialogbuilder.setView(prontView);
        final TextInputLayout textInputLayout = (TextInputLayout) prontView.findViewById(R.id.dialog_input_text);

        //setuo dialog window
        alertdialogbuilder.setCancelable(false).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SurveyOption surveyOption = new SurveyOption();
                surveyOption.setDescription(textInputLayout.getEditText().getText().toString());
                opciones.add(surveyOption);
                adapter.notifyDataSetChanged();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertdialogbuilder.create();
        alertDialog.show();

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return opciones;
    }
}
