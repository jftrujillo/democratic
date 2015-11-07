package com.kcumendigital.democratic;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    static final String SAVED_EMAIL="email";
    static final String SAVED_PASS="pass";

    TextInputLayout email, pass;
    Button btnRegistro;
    FloatingActionButton ingresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (TextInputLayout) findViewById(R.id.email_login);
        pass = (TextInputLayout) findViewById(R.id.password);
        btnRegistro= (Button) findViewById(R.id.register);
        ingresar = (FloatingActionButton) findViewById(R.id.ingresar);

        btnRegistro.setOnClickListener(this);
        ingresar.setOnClickListener(this);

        if(savedInstanceState!=null){
            email.getEditText().setText(savedInstanceState.getString(SAVED_EMAIL));
            pass.getEditText().setText(savedInstanceState.getString(SAVED_PASS));
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_EMAIL,email.getEditText().getText().toString());
        outState.putString(SAVED_PASS,pass.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                break;

            case R.id.ingresar:
                break;
        }
    }
}
