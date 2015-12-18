package com.kcumendigital.democraticcauca;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    int posC, posD;
    FrameLayout imgC;
    ImageView imgCategory;

    TextView title, comment;

    ShareButton sb;


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
            imgCategory.setImageResource(R.drawable.screengobierno);

        }

        if (categori.equals("Salud")){
            imgCategory.setImageResource(R.drawable.screensalud);
        }

        if (categori.equals("Educación")){
            imgCategory.setImageResource(R.drawable.screeneducacion);

        }

        if (categori.equals("Medio Ambiente")){
            imgCategory.setImageResource(R.drawable.screenmedio);

        }

        title = (TextView) findViewById(R.id.title);
        comment = (TextView) findViewById(R.id.comment);

        title.setText(discussion.getTitle());
        comment.setText(commentD.getDescription());

        prepareShare();

    }

    private void prepareShare() {
        imgC.setDrawingCacheEnabled(true);
        Bitmap bitmap = imgC.getDrawingCache();

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption("Democratic")
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
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


}

