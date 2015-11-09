package com.kcumendigital.democratic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kcumendigital.democratic.Adapters.CommentAdapter;
import com.kcumendigital.democratic.LayoutManagers.commentsLayoutManager;
import com.kcumendigital.democratic.Models.Comment;
import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.Models.DiscussionScore;
import com.kcumendigital.democratic.Models.User;
import com.kcumendigital.democratic.Util.AppUtil;
import com.kcumendigital.democratic.Util.ColletionsStatics;
import com.kcumendigital.democratic.parse.SunshinePageControl;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineQuery;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForumsActivity extends AppCompatActivity implements SunshineParse.SunshineCallback, View.OnClickListener, DialogInterface.OnClickListener, View.OnTouchListener,CommentAdapter.OnItemClickListener {

    static final int REQUEST_DISCUSION_SCORE_LIKE = 2;
    static final int REQUEST_DISCUSION_SCORE_DISLIKE = 1;
    static final int REQUEST_COMMENT_SCORE = 4;

    static boolean IS_STOP;

    private Toolbar mToolbar;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Transformation transformation;

    RecyclerView recyclerView;
    CommentAdapter adapter;

    int pos;
    TextView tittle, likes, dislikes, categoria;
    LinearLayout btnLike, btnDislike;
    ImageView btn_record;
    ImageView discussionUser, imgCategory;

    User user;
    MediaRecorder recorder;
    File archivo;

    SunshineParse parse;
    SunshinePageControl control;

    Discussion discussion;
    DiscussionScore score;

    String scoreD;

    EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        user = AppUtil.getUserStatic();
        comentario = (EditText) findViewById(R.id.comentario);

        btn_record = (ImageView) findViewById(R.id.btnRecord);
        btn_record.setOnTouchListener(this);

        imgCategory = (ImageView) findViewById(R.id.imgCategories);



        Bundle bundle = getIntent().getExtras();
        pos = (int) bundle.get("pos");
        boolean pager = bundle.getBoolean("pager",false);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tittle = (TextView) findViewById(R.id.txttittle);
        likes = (TextView) findViewById(R.id.value_like);
        dislikes = (TextView) findViewById(R.id.value_dislike);
        categoria = (TextView) findViewById(R.id.tittleCategorie);
        discussionUser = (ImageView) findViewById(R.id.imperfiluser);

        btnLike = (LinearLayout) findViewById(R.id.btn_like);
        btnDislike = (LinearLayout) findViewById(R.id.btn_dislike);
        btnLike.setOnClickListener(this);
        btnDislike.setOnClickListener(this);


        ArrayList<Discussion> dataDiscusion = null;
        if(!pager)
            dataDiscusion = ColletionsStatics.getDataDiscusion();
        else
            dataDiscusion = ColletionsStatics.getHomeDiscusion();

        discussion = dataDiscusion.get(pos);
        tittle.setText(discussion.getTitle());
        likes.setText("" + discussion.getLikes());
        dislikes.setText("" + discussion.getDislikes());
        categoria.setText(discussion.getCategory());

        if(discussion.getCategory().equals(getString(R.string.c_gobierno))){
            Picasso.with(this).load(R.drawable.ic_account_balance_white_36dp).into(imgCategory);

        }else if(discussion.getCategory().equals(getString(R.string.c_educacion))) {
            Picasso.with(this).load(R.drawable.ic_school_white_18dp).into(imgCategory);

        }else if (discussion.getCategory().equals(getString(R.string.c_salud))){
                Picasso.with(this).load(R.drawable.ic_local_hospital_white_18dp).into(imgCategory);
        }else if (discussion.getCategory().equals(getString(R.string.c_ambiente))){
            Picasso.with(this).load(R.drawable.ic_local_hospital_white_18dp).into(imgCategory);
        }

        transformation = new RoundedTransformationBuilder().oval(true).build();

        int avatar = getResources().getDimensionPixelSize(R.dimen.forum_avatar);
        Picasso.with(this).load(Uri.parse(discussion.getUser().getImg()))
                .resize(avatar, avatar)
                .centerCrop()
                .transform(transformation).into(discussionUser);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewForumsComents);


        adapter = new CommentAdapter(this,this, ColletionsStatics.getDataComments(),recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new commentsLayoutManager(this));

        parse = new SunshineParse();
        SunshineQuery query = new SunshineQuery();
        query.addPointerValue("discussion",discussion.getObjectId());

        control =  new SunshinePageControl(SunshinePageControl.ORDER_ASCENING
                ,recyclerView,null,ColletionsStatics.getDataComments(),query,Comment.class);

        if(savedInstanceState==null)
            control.nextPage();

        comentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                if (comentario.getText().toString().equals("")){
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_mic_white_24dp).transform(transformation).into(btn_record);
                }

                else {
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_send_white_24dp).transform(transformation).into(btn_record);
                }
            }
        });
    }

    //region Voice Recorder
    public void grabar() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File path = new File(Environment.getExternalStorageDirectory()
                .getPath());
        try {
            archivo = File.createTempFile("temporal", ".3gp", path);
        } catch (IOException e) {
        }
        recorder.setOutputFile(archivo.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
        }
        recorder.start();
    }

    public void detener() {
        if (IS_STOP == false) {
            recorder.stop();
            recorder.release();
            createNewVoiceComent(archivo.getPath());
            btn_record.setEnabled(true);
            IS_STOP = true;
        }
    }

    public void tiempoGrabacion() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "se termino el tiempo de grabacion", Toast.LENGTH_SHORT).show();
                detener();
            }
        }.start();
    }
    //endregion

    //region Enviar Comentario
    private void createNewVoiceComent(String absolutePath) {
        final Comment comment = new Comment();
        comment.setRecord(true);
        comment.setDiscussion(ColletionsStatics.getDataDiscusion().get(pos).getObjectId());
        comment.setUser(user);
        comment.setFilePath(absolutePath);
        final SunshineParse parseVoice = new SunshineParse();
        parseVoice.insert(comment, new SunshineParse.SunshineCallback() {
            @Override
            public void done(boolean success, ParseException e) {
                if (success == true) {
                    Toast.makeText(getApplicationContext(), "Nota de Voz Creada al final de comentarios", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    parseVoice.incrementField(discussion.getObjectId(), "comments", Discussion.class);
                    ColletionsStatics.getDataDiscusion().get(pos).setComments(ColletionsStatics.getDataDiscusion().get(pos).getComments() + 1);
                    if(ColletionsStatics.getDataComments().size()==5)
                        control.nextPage();

                } else {
                    Toast.makeText(getApplicationContext(), "Nota de Voz no Creada", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (comentario.getText().toString().equals("")) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    IS_STOP = false;
                    tiempoGrabacion();
                    grabar();
                    Toast.makeText(this, "grabando Audio", Toast.LENGTH_LONG).show();
                    return true;
                case MotionEvent.ACTION_UP:
                    detener();
                    Toast.makeText(this, "Audio Finalizado", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
        else
        {
            Comment comment = new Comment();
            comment.setDescription(comentario.getText().toString());
            User user = AppUtil.getUserStatic();
            comment.setUser(user);
            comment.setDiscussion(ColletionsStatics.getDataDiscusion().get(pos).getObjectId());
            comment.setRecord(false);
            final SunshineParse parse = new SunshineParse();
            parse.insert(comment, new SunshineParse.SunshineCallback() {
                @Override
                public void done(boolean success, ParseException e) {
                    if (success == true){
                        SunshineQuery query = new SunshineQuery();
                        query.addPointerValue("discussion", discussion.getObjectId());
                        comentario.setText("");
                        Date date = null;
                        Toast.makeText(ForumsActivity.this,"Comentado creado al final",Toast.LENGTH_SHORT).show();
                        parse.incrementField(discussion.getObjectId(), "comments", Discussion.class);
                        ColletionsStatics.getDataDiscusion().get(pos).setComments(ColletionsStatics.getDataDiscusion().get(pos).getComments() + 1);
                        adapter.notifyDataSetChanged();
                        if(ColletionsStatics.getDataComments().size()<5)
                            control.nextPage();
                    }

                    else {
                        Toast.makeText(getApplicationContext(),"Comentario No Creado",Toast.LENGTH_SHORT).show();
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
        return false;
    }

    //endregion

    //region Votar Discusion
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_like:
                likeDiscussion(user.getObjectId());
                break;
            case R.id.btn_dislike:
                dislikeDiscussion(user.getObjectId());
                break;
        }
    }

    public void likeDiscussion(String id) {
        SunshineQuery queryVotes = new SunshineQuery();
        queryVotes.addUser("user", id);
        parse.getAllRecords(queryVotes, this, REQUEST_DISCUSION_SCORE_LIKE, DiscussionScore.class);
    }

    public void dislikeDiscussion(String id) {
        SunshineQuery queryVotes = new SunshineQuery();
        queryVotes.addUser("user", id);
        parse.getAllRecords(queryVotes, this, REQUEST_DISCUSION_SCORE_DISLIKE, DiscussionScore.class);
    }

    private void processDiscussionScore(List<SunshineRecord> records, String like) {

        if (records.size() > 0) {
            score = (DiscussionScore) records.get(0);
            if (score.getType().equals(like)) {
                Toast.makeText(this, R.string.voted, Toast.LENGTH_SHORT).show();

            } else {
                scoreD = like;
                AlertDialog alert = new AlertDialog.Builder(this)
                        .setMessage(R.string.change_vote)
                        .setPositiveButton(R.string.dialog_positive, this)
                        .setNegativeButton(R.string.dialog_negative, this)
                        .create();
                alert.show();
            }

        } else {
            DiscussionScore discussionScore = new DiscussionScore();
            discussionScore.setDiscussion(discussion.getObjectId());
            discussionScore.setUser(user.getObjectId());
            discussionScore.setType(like);
            parse.insert(discussionScore);
            if (like.equals(DiscussionScore.LIKE)) {
                parse.incrementField(discussion.getObjectId(), "likes", Discussion.class);
                ColletionsStatics.getDataDiscusion().get(pos).setLikes(ColletionsStatics.getDataDiscusion().get(pos).getLikes() + 1);
                likes.setText("" + (Integer.valueOf("" + likes.getText()) + 1));
            } else {
                parse.incrementField(discussion.getObjectId(), "dislikes", Discussion.class);
                ColletionsStatics.getDataDiscusion().get(pos).setDislikes(ColletionsStatics.getDataDiscusion().get(pos).getDislikes() + 1);
                dislikes.setText("" + (Integer.valueOf("" + dislikes.getText()) + 1));
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            score.setType(scoreD);
            parse.update(score, false, false, this);
            if (scoreD.equals(DiscussionScore.LIKE)) {
                parse.decrementField(discussion.getObjectId(), "dislikes", Discussion.class);
                parse.incrementField(discussion.getObjectId(), "likes", Discussion.class);
                likes.setText("" + (Integer.valueOf("" + likes.getText()) + 1));
                int d = Integer.valueOf("" + dislikes.getText());
                if (d > 0)
                    dislikes.setText("" + (d - 1));
            } else {
                parse.decrementField(discussion.getObjectId(), "likes", Discussion.class);
                parse.incrementField(discussion.getObjectId(), "dislikes", Discussion.class);

                dislikes.setText("" + (Integer.valueOf("" + dislikes.getText()) + 1));
                int d = Integer.valueOf("" + likes.getText());
                if (d > 0)
                    likes.setText("" + (d - 1));
            }
        }
    }
    //endregion

    //region Metodos Parse
    @Override
    public void done(boolean success, ParseException e) {
    }

    @Override
    public void resultRecord(boolean success, SunshineRecord record, ParseException e) {
    }

    @Override
    public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
        if (requestCode == REQUEST_DISCUSION_SCORE_LIKE) {
            processDiscussionScore(records, DiscussionScore.LIKE);
        } else if (requestCode == REQUEST_DISCUSION_SCORE_DISLIKE) {
            processDiscussionScore(records, DiscussionScore.DISLIKE);
        }
    }
    //endregion

    //region Votar Comentario
    @Override
    public void onItemClick(int position, int type) {

        if(type == CommentAdapter.BTN_LIKE){
            Log.i("BOTONES", "Se presiono el like");
        }
        if(type == CommentAdapter.BTN_DISLIKE){
            Log.i("BOTONES", "Se presiono el dislike");
        }

    }
    //endregion





}