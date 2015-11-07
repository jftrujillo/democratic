package com.kcumendigital.democratic;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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

public class ForumsActivity extends AppCompatActivity implements SunshineParse.SunshineCallback, View.OnClickListener, DialogInterface.OnClickListener, MediaPlayer.OnCompletionListener,View.OnTouchListener,CommentAdapter.OnItemClickListener {

   // static final String USER_ID = "Jgb5AcAcBp"; // BORRAR

    static final int REQUEST_PAGE = 0;
    static final int REQUEST_RECENT = 1;
    static final int REQUEST_DISCUSION_SCORE_LIKE = 2;
    static final int REQUEST_DISCUSION_SCORE_DISLIKE = 3;
    static final int REQUEST_COMMENT_SCORE = 4;
    static boolean IS_STOP = false;
    private Toolbar mToolbar;
    public static String ID_DISUCION = "";
    private boolean intialStage = true;
    private boolean playPause;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Transformation transformation;

    List<Comment> data;

    RecyclerView recyclerView;
    CommentAdapter adapter;

    int pos;
    TextView tittle, likes, dislikes, categoria;
    LinearLayout btnLike, btnDislike;
    ImageView btn_record;
    ImageView discussionUser, imgCategory;
    ImageButton imgPlay;
    User user;
    MediaRecorder recorder;
    File archivo;
    MediaPlayer player;
    SeekBar seekbar;


    SunshineParse parse;
    Discussion discussion;
    DiscussionScore score;
    String scoreD;

    EditText comentario;

    @Override
    protected void onStart() {
        super.onStart();
        imgPlay = (ImageButton) findViewById(R.id.playVoice);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        AppUtil.initStaticUser();
        user = AppUtil.getUserStatic();
        comentario = (EditText) findViewById(R.id.comentario);

        imgPlay = (ImageButton) findViewById(R.id.playVoice);
        btn_record = (ImageView) findViewById(R.id.btnRecord);
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        btn_record.setOnTouchListener(this);

        imgCategory = (ImageView) findViewById(R.id.imgCategories);



        Bundle bundle = getIntent().getExtras();
        pos = (int) bundle.get("pos");

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

        ArrayList<Discussion> dataDiscusion = ColletionsStatics.getDataDiscusion();

        discussion = dataDiscusion.get(pos);
        tittle.setText(discussion.getTitle());
        likes.setText("" + discussion.getLikes());
        dislikes.setText("" + discussion.getDislikes());
        categoria.setText(discussion.getCategory());

        if(discussion.getCategory().equals("Gobierno")){
            Picasso.with(this).load(R.drawable.ic_account_balance_white_36dp).into(imgCategory);

        }else
        if(discussion.getCategory().equals("Educación")) {
            Picasso.with(this).load(R.drawable.ic_school_white_18dp).into(imgCategory);

        }else{
            if (discussion.getCategory().equals("Salud")){
                Picasso.with(this).load(R.drawable.ic_local_hospital_white_18dp).into(imgCategory);
            }
        }

        transformation = new RoundedTransformationBuilder().oval(false).build();
        Picasso.with(this).load(Uri.parse(discussion.getUser().getImg())).transform(transformation).into(discussionUser);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewForumsComents);

        data = new ArrayList<>();
        adapter = new CommentAdapter(this,this, data,recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new commentsLayoutManager(this));

        parse = new SunshineParse();

        getComments();

        comentario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                if (comentario.getText().toString().equals("")){
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_mic_white_18dp).transform(transformation).into(btn_record);
                }

                else {
                    Picasso.with(getApplicationContext()).load(R.drawable.ic_send_white_24dp).transform(transformation).into(btn_record);
                }
            }
        });
    }

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
            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            try {
                player.setDataSource(archivo.getAbsolutePath());
            } catch (IOException e) {
            }
            try {
                player.prepare();
            } catch (IOException e) {
            }
            createNewVoiceComent(archivo.getPath());
            btn_record.setEnabled(true);
            IS_STOP = true;
        } else{}
    }

    public void stopPlayer(){
        if(!IS_STOP){
            player.stop();
            player.release();
            player = null;
            IS_STOP = true;
        }
    }

    public void playPlayer(String url){
        IS_STOP = true;
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
    }

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
                    Toast.makeText(getApplicationContext(), "Nota de Voz Creada", Toast.LENGTH_SHORT).show();
                    getNewComments();
                    adapter.notifyDataSetChanged();
                    parseVoice.incrementField(discussion.getObjectId(), "comments", Discussion.class);
                    ColletionsStatics.getDataDiscusion().get(pos).setComments(ColletionsStatics.getDataDiscusion().get(pos).getComments() + 1);
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

    public void reproducir() {
        player.start();
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

    public void getComments() {
        SunshineQuery query = new SunshineQuery();
        query.addPointerValue("discussion", discussion.getObjectId());
        Date date = null;
        if (data.size() > 0)
            date = data.get(data.size() - 1).getCreatedAt();
        parse.getRecordsByPage(date, ColletionsStatics.LIMIT, query, this, REQUEST_PAGE, Comment.class);
    }

    public void getNewComments(){
        SunshineQuery query = new SunshineQuery();
        query.addPointerValue("discussion", discussion.getObjectId());
        Date date = null;
        if (data.size() > 0)
            date = data.get(0).getCreatedAt();
        parse.getRecentRecords(date, query, this, REQUEST_RECENT, Comment.class);
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

    public void likeComment(Comment comment) {

    }

    public void dislikeComment(Comment comment) {

    }


    @Override
    public void done(boolean success, ParseException e) {
    }

    @Override
    public void resultRecord(boolean success, SunshineRecord record, ParseException e) {
    }

    @Override
    public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
        if (requestCode == REQUEST_PAGE) {
            addPage(records);
        } else if(requestCode == REQUEST_RECENT){
            addRecent(records);
        }

        else if (requestCode == REQUEST_DISCUSION_SCORE_LIKE) {
            processDiscussionScore(records, DiscussionScore.LIKE);
        } else if (requestCode == REQUEST_DISCUSION_SCORE_DISLIKE) {
            processDiscussionScore(records, DiscussionScore.DISLIKE);
        }
    }

    private void addRecent(List<SunshineRecord> records) {
        for(int i = records.size()-1; i>-1;i--){
            data.add(0, (Comment)records.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    private void addPage(List<SunshineRecord> records) {
        for (SunshineRecord r : records) {
            data.add((Comment) r);
        }
        adapter.notifyDataSetChanged();
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
                data.get(pos).setLikes(data.get(pos).getDislikes()+1);
            }
        }
    }

    private void showAlertInternet() {
        Toast.makeText(this, R.string.con_intenert_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            score.setType(scoreD);
            parse.update(score, false,false, this);
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

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlayer();
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
            User user = new User();
            user.setObjectId("9zq30KL7Gu");
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
                        getNewComments();
                        parse.incrementField(discussion.getObjectId(), "comments", Discussion.class);
                        ColletionsStatics.getDataDiscusion().get(pos).setComments(ColletionsStatics.getDataDiscusion().get(pos).getComments() + 1);
                        adapter.notifyDataSetChanged();
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

    @Override
    public void onItemClick(int position, int type) {

        if(type == CommentAdapter.BTN_LIKE){
            Log.i("BOTONES", "Se presiono el like");
        }
        if(type == CommentAdapter.BTN_DISLIKE){
            Log.i("BOTONES", "Se presiono el dislike");
        }
        if(type == CommentAdapter.BTN_PLAY){
            Log.i("BOTONES", "Se presiono el play en pos "+position);

            if (!playPause) {

                if (intialStage)
                    new Player()
                            .execute(data.get(position).getFile());
                else {
                    if (!player.isPlaying()) {
                        player.start();
                    }
                }
                playPause = true;
            } else {
                //imgPlay.setBackgroundResource(R.drawable.ic_thumb_up_black_18dp);
                if (player.isPlaying()){
                    player.pause();
                }
                playPause = false;
            }


        }
/*
        if (!playPause) {
            //btn.setBackgroundResource(R.drawable.button_pause);
            if (intialStage)
                new Player()
                        .execute(data.get(position).getFile());
            else {
                if (!player.isPlaying())
                    player.start();
            }
            playPause = true;
        } else {
            // btn.setBackgroundResource(R.drawable.button_play);
            if (player.isPlaying())
                player.pause();
            playPause = false;
        }*/
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                player.setDataSource(params[0]);
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        intialStage = true;
                        playPause=false;
                        player.stop();
                        player.reset();
                    }
                });
                player.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            player.start();

            intialStage = false;
        }

        public Player() {
            progress = new ProgressDialog(ForumsActivity.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            this.progress.setMessage("Buffering...");
            this.progress.show();

        }
    }

}