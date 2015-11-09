package com.kcumendigital.democratic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.parse.SunshineLogin;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SignInAcitivity extends AppCompatActivity implements View.OnClickListener, SunshineLogin.SunshineLoginCallback {

    static final int CAMERA = 101;

    TextInputLayout correo,nombre,contrasena,cContrasena;
    FloatingActionButton next;
    Button btn;
    ProgressDialog dialog;
    ImageView img;
    File imgF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitivity);
        correo = (TextInputLayout) findViewById(R.id.email_login_sign);
        nombre = (TextInputLayout) findViewById(R.id.name_sing);
        contrasena = (TextInputLayout) findViewById(R.id.password_sign);
        cContrasena= (TextInputLayout) findViewById(R.id.password_confirm_sign);
        next = (FloatingActionButton) findViewById(R.id.ingresar_sign);
        btn = (Button) findViewById(R.id.register_sign);
        next.setOnClickListener(this);
        btn.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.sigin_register));

        img = (ImageView) findViewById(R.id.picture);
        img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ingresar_sign:
                String email = correo.getEditText().getText().toString();
                String name = nombre.getEditText().getText().toString();
                String pass1 = contrasena.getEditText().getText().toString();
                String pass2 = cContrasena.getEditText().getText().toString();

                if(validate(email)&&validate(name)&&validate(pass1)&&validate(pass2)){
                    if(pass1.equals(pass2)){

                        User user = new User();
                        user.setPassword(pass1);
                        user.setEmail(email);
                        user.setName(name);
                        user.setUserName(email);
                        if(imgF!=null)
                            user.setImgPath(imgF.getPath());

                        SunshineLogin login = new SunshineLogin();
                        login.siginUp(user, this);
                        dialog.show();

                    }else{
                        Toast.makeText(getApplicationContext(), R.string.sigin_pass_fail,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.sigin_fail,Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.register_sign:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.picture:
                takePicture();
                break;
        }
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(Environment.getExternalStorageDirectory().getPath());
        imgF = new File(dir, "userPictureTemp.jpg");
        Uri uri = Uri.fromFile(imgF);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA);
    }

    public boolean validate(String txt){
        if(txt==null || txt.equals(""))
            return false;
        else
            return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA){
            if(resultCode == RESULT_OK){
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try {
                    scaleImage(400);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void scaleImage(int maxAxis) throws IOException {

        ExifInterface exif = new ExifInterface(imgF.getPath());
        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);


        BitmapFactory.Options o =  new BitmapFactory.Options();
        o.inJustDecodeBounds=true;

        BitmapFactory.decodeFile(imgF.getPath(), o);
        int w = o.outWidth;
        int h = o.outHeight;

        int a = w>h?w:h;
        int sampleSize=1;

        while(a>maxAxis){
            sampleSize = sampleSize*2;
            a=a/2;
        }

        o.inJustDecodeBounds=false;
        o.inSampleSize = sampleSize;


        Bitmap b = BitmapFactory.decodeFile(imgF.getPath(), o);

        imgF.delete();

        FileOutputStream out = new FileOutputStream(imgF);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, stream);


        out.write(stream.toByteArray());

        b.recycle();
        b= null;

        out.close();
        stream.close();

        ExifInterface exif2 = new ExifInterface(imgF.getPath());
        exif2.setAttribute(ExifInterface.TAG_ORIENTATION, ""+rotation);
        exif2.saveAttributes();

        Picasso.with(this).load(imgF).into(img);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void done(boolean success, ParseException e) {
        dialog.hide();
        if(success) {
            Toast.makeText(getApplicationContext(), R.string.sigin_success, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), R.string.sigin_error, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
