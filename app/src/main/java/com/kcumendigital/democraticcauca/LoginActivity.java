package com.kcumendigital.democraticcauca;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kcumendigital.democraticcauca.Models.User;
import com.kcumendigital.democraticcauca.Util.AppUtil;
import com.kcumendigital.democraticcauca.library.Techniques;
import com.kcumendigital.democraticcauca.library.YoYo;
import com.kcumendigital.democraticcauca.parse.SunshineLogin;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, SunshineLogin.SunshineLoginCallback, SunshineLogin.SunshineFacebookLoginCallback {

    static final int REQUEST_SIGIN=102;

    static final String SAVED_EMAIL="email";
    static final String SAVED_PASS="pass";

    TextInputLayout email, pass;
    Button btnRegistro, btnFacebook;
    FloatingActionButton ingresar;
    ProgressDialog dialog;
    SunshineLogin login;
    LinearLayout cordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = new SunshineLogin();

        email = (TextInputLayout) findViewById(R.id.email_login);
        pass = (TextInputLayout) findViewById(R.id.password);
        btnRegistro= (Button) findViewById(R.id.register);
        ingresar = (FloatingActionButton) findViewById(R.id.ingresar);
        btnFacebook = (Button) findViewById(R.id.login_facebook_btn);

        btnFacebook.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
        ingresar.setOnClickListener(this);

        dialog= new ProgressDialog(this);
        dialog.setMessage(getString(R.string.login_validate));

        if(savedInstanceState!=null){
            email.getEditText().setText(savedInstanceState.getString(SAVED_EMAIL));
            pass.getEditText().setText(savedInstanceState.getString(SAVED_PASS));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_EMAIL, email.getEditText().getText().toString());
        outState.putString(SAVED_PASS, pass.getEditText().getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Intent intent = new Intent(this,SignInAcitivity.class);
                startActivityForResult(intent, REQUEST_SIGIN);
                break;

            case R.id.ingresar:
                dialog.show();
                String emailT=email.getEditText().getText().toString().toLowerCase();
                String passT = pass.getEditText().getText().toString();

                if(validate(emailT)&&validate(passT))
                    login.login(emailT, passT, this);
                else {
                    dialog.dismiss();
                    YoYo.with(Techniques.Tada).duration(700).playOn(findViewById(R.id.containerLogin));
                    Toast.makeText(getApplicationContext(), R.string.sigin_fail, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_facebook_btn:
                login.loginByFacebook(this,null,this);
            break;
        }
    }

    public boolean validate(String txt){
        if(txt==null || txt.equals(""))
            return false;
        else
            return true;
    }

    @Override
    public void done(boolean success, ParseException e) {
        dialog.dismiss();
        if(success){
            inApp();
        }else{
            Toast.makeText(this,"Email o password incorrectos",Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Tada).duration(700).playOn(findViewById(R.id.containerLogin));
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_SIGIN){
            if(resultCode == RESULT_OK){
                inApp();
            }
        }else{
            ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void inApp(){
        Intent intent = new Intent(this, AppIntroActivity.class);
        startActivity(intent);
        AppUtil.setUser(SunshineLogin.getLoggedUser(User.class));
        finish();
    }

    @Override
    public void done(int type, ParseException e) {
        if(type == LOGIN || type == SIGINUP)
            inApp();
        else
            Toast.makeText(this,"Error al ingresar con Facebook",Toast.LENGTH_SHORT).show();
    }
}
