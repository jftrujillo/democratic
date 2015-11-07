package com.kcumendigital.democratic;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignInAcitivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout correo,nombre,contraseña,confirmar_contraseña;
    FloatingActionButton next;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitivity);
        correo = (TextInputLayout) findViewById(R.id.email_login_sign);
        nombre = (TextInputLayout) findViewById(R.id.name_sing);
        contraseña = (TextInputLayout) findViewById(R.id.password_sign);
        confirmar_contraseña = (TextInputLayout) findViewById(R.id.password_confirm_sign);
        next = (FloatingActionButton) findViewById(R.id.ingresar_sign);
        btn = (Button) findViewById(R.id.register_sign);
        next.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ingresar_sign:
                if (contraseña.getEditText().getText().toString().equals(confirmar_contraseña.getEditText().getText().toString())){
                    Toast.makeText(getApplicationContext(),"Contraseña iguales",Toast.LENGTH_SHORT).show();
                    //Aqui se validan igualdad de contraseñas, entonces aqui se puede hacer la logica para crear usuario
                }
                else
                    Toast.makeText(getApplicationContext(),"Contraseña no iguales",Toast.LENGTH_SHORT).show();
                break;

            case R.id.register_sign:
                startActivity(new Intent(getApplication(),LoginActivity.class));
                break;
        }
    }
}
