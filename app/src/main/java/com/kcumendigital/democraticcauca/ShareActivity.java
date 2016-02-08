package com.kcumendigital.democraticcauca;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.kcumendigital.democraticcauca.Models.Comment;
import com.kcumendigital.democraticcauca.Models.Discussion;
import com.kcumendigital.democraticcauca.Util.ColletionsStatics;


public class ShareActivity extends AppCompatActivity{

    public static final String EXTRA_POS_COMMENT = "pos_comment";
    public static final String EXTRA_POS_DISCUSSION= "pos_discussion";
    static final long TIME_WAIT = 1000;

    int posC, posD;
    FrameLayout imgC;
    ImageView imgCategory;

    TextView title, comment;

    ShareButton sb;

    SharePhotoContent content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            posD = bundle.getInt(EXTRA_POS_DISCUSSION);
            posC = bundle.getInt(EXTRA_POS_COMMENT);
        }

        else {
            posD = savedInstanceState.getInt(EXTRA_POS_DISCUSSION);
            posC = savedInstanceState.getInt(EXTRA_POS_COMMENT);
        }


        Discussion discussion = ColletionsStatics.getDataDiscusion().get(posD);
        Comment commentD = ColletionsStatics.getDataComments().get(posC);

        sb = (ShareButton) findViewById(R.id.share);

        imgC = (FrameLayout) findViewById(R.id.imgCategory);
        imgCategory = (ImageView) findViewById(R.id.category);
        //Colocar imagen de la categoria en imgCategory

        String categori = discussion.getCategory();

        if (categori.equals("Gobierno")){
            imgCategory.setBackgroundColor(getResources().getColor(R.color.gobierno));

        }

        if (categori.equals("Salud")){
            imgCategory.setBackgroundColor(getResources().getColor(R.color.salud));
        }

        if (categori.equals("Educación")){
            imgCategory.setBackgroundColor(getResources().getColor(R.color.educacion));

        }

        if (categori.equals("Medio Ambiente")){
            imgCategory.setBackgroundColor(getResources().getColor(R.color.medio_ambiente));

        }

        title = (TextView) findViewById(R.id.title);
        comment = (TextView) findViewById(R.id.comment);

        title.setText(discussion.getTitle());
        comment.setText(commentD.getDescription());




        TimeWait wait =  new TimeWait();
        wait.execute();


    }

    private void prepareShare() {
        imgC.setDrawingCacheEnabled(true);
        Bitmap bitmap = imgC.getDrawingCache();

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption("Democratic")
                .build();


        content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        sb.setShareContent(content);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_POS_DISCUSSION, posD);
        outState.putInt(EXTRA_POS_COMMENT, posC);
        super.onSaveInstanceState(outState);
    }





    private class TimeWait extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                Thread.sleep(TIME_WAIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            prepareShare();
        }
    }
}

