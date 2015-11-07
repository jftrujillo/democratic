package com.kcumendigital.democratic.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kcumendigital.democratic.Models.Comment;
import com.kcumendigital.democratic.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by manuelfernando on 16/10/2015.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_VOICE = 1;

    public static final int BTN_LIKE = 1;
    public static final int BTN_DISLIKE = 2;

    public static final int STATE_PAUSED=0;
    public static final int STATE_PLAYING=1;
    public static final int STATE_DOWNLOADING=2;

    Context context;
    List<Comment> data;
    RecyclerView recyclerView;
    OnItemClickListener onItemClick;
    Transformation transformation;

    MediaPlayer player;
    HashMap<Integer, ProgressBar> bars;
    boolean paused;
    int pos;
    ProgressHandler progressHandler;

    public interface OnItemClickListener {
        void onItemClick(int position, int button);
    }

    ImageButton imgPlay;


    public CommentAdapter(OnItemClickListener onItemClick, Context context, List<Comment> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
        this.onItemClick = onItemClick;
        pos = -1;
        bars = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;

        if (viewType == TYPE_TEXT) {
            v = View.inflate(context, R.layout.template_text_forums, null);
            viewHolder = new TextViewHolder(v);
        } else {
            v = View.inflate(context, R.layout.template_voice_forums, null);
            viewHolder = new VoiceViewHolder(v);

        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).nombreUsuario.setText(data.get(position).getUser().getUserName());
            ((TextViewHolder) holder).likes.setText("" + data.get(position).getLikes());
            ((TextViewHolder) holder).dislikes.setText("" + data.get(position).getDislikes());
            ((TextViewHolder) holder).comment.setText(data.get(position).getDescription());

            ((TextViewHolder) holder).likes.setOnClickListener(this);
            ((TextViewHolder) holder).dislikes.setOnClickListener(this);


            //  comment,likes,dislikes,titulo;
        }

        if (holder instanceof VoiceViewHolder) {
            ((VoiceViewHolder) holder).btn_play.setOnClickListener(this);
            ((VoiceViewHolder) holder).btn_play.setTag(position);
            if(data.get(position).getState()==STATE_PAUSED){
                ((VoiceViewHolder) holder).btn_play.setVisibility(ImageView.VISIBLE);
                ((VoiceViewHolder) holder).downloading.setVisibility(ImageView.GONE);
                ((VoiceViewHolder) holder).btn_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            }else if(data.get(position).getState()==STATE_PLAYING){
                ((VoiceViewHolder) holder).btn_play.setVisibility(ImageView.VISIBLE);
                ((VoiceViewHolder) holder).downloading.setVisibility(ImageView.GONE);
                ((VoiceViewHolder) holder).btn_play.setImageResource(R.drawable.ic_pause_black_24dp);
            }else{
                ((VoiceViewHolder) holder).btn_play.setVisibility(ImageView.GONE);
                ((VoiceViewHolder) holder).downloading.setVisibility(ImageView.VISIBLE);
            }

            bars.put(position, ((VoiceViewHolder) holder).progress);

        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        // return data.get(position).getType();
        if (data.get(position).getRecord() == true) {
            return TYPE_VOICE;
        } else {
            return TYPE_TEXT;

        }

    }


    class TextViewHolder extends RecyclerView.ViewHolder {

        TextView comment, likes, dislikes, nombreUsuario;
        ImageView imgPerfil;

        public TextViewHolder(View itemView) {
            super(itemView);
            comment = (TextView) itemView.findViewById(R.id.comment);
            likes = (TextView) itemView.findViewById(R.id.value_like);
            dislikes = (TextView) itemView.findViewById(R.id.value_dislike);
            nombreUsuario = (TextView) itemView.findViewById(R.id.nombreUsuario);
            imgPerfil = (ImageView) itemView.findViewById(R.id.imgPerfil);

            transformation = new RoundedTransformationBuilder().oval(true).build();
            Picasso.with(context).load("https://fbcdn-sphotos-b-a.akamaihd.net/hphotos-ak-frc3/v/t1.0-9/1970810_10151892227450771_595738950_n.jpg?oh=882c88204af7abd222bc73a21a3edc62&oe=56C9C868&__gda__=1459270949_613753f7ed5e1b07391330a1d893d390").transform(transformation).into(imgPerfil);
        }
    }

    public class VoiceViewHolder extends RecyclerView.ViewHolder {
        ImageView perfil;
        ImageButton btn_play;
        ProgressBar downloading;
        ProgressBar progress;

        public VoiceViewHolder(View itemView) {
            super(itemView);

            perfil = (ImageView) itemView.findViewById(R.id.imgPerfilvoice);
            com.squareup.picasso.Transformation transformation = (com.squareup.picasso.Transformation) new RoundedTransformationBuilder().oval(true).build();
            Picasso.with(context).load("http://k10.kn3.net/taringa/6/6/8/7/9/1/4/takehikoinoue/FA7.jpg").transform(transformation).into(perfil);
            btn_play = (ImageButton) itemView.findViewById(R.id.playVoice);
            downloading = (ProgressBar) itemView.findViewById(R.id.downloadingVoice);
            progress = (ProgressBar) itemView.findViewById(R.id.progressVoice);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.playVoice:
                int posC = (Integer) view.getTag();
                handlePlayer(data.get(posC).getFile(), posC);
                break;
            case R.id.like:
                onItemClick.onItemClick(0, BTN_LIKE);
                break;
            case R.id.disLike:
                onItemClick.onItemClick(0, BTN_DISLIKE);
                break;
        }

    }

    public void handlePlayer(String url, int posC){
        if(pos==-1 || pos == posC)
            playPlayer(data.get(posC).getFile(), posC);
        else{
            stopPlayer();
            playPlayer(data.get(posC).getFile(), posC);
        }

    }


    public void playPlayer(String url, int posC){
        if(this.paused){
            data.get(posC).setState(STATE_PLAYING);
            notifyDataSetChanged();
            player.start();
            paused = false;
            progressHandler.setPaused(false);
        }else {
            if(player!=null && player.isPlaying()){
                pausePlayer();
            }else {
                data.get(posC).setState(STATE_DOWNLOADING);
                notifyDataSetChanged();
                player = new MediaPlayer();
                try {
                    player.setDataSource(url);
                    player.setOnPreparedListener(this);
                    player.setOnCompletionListener(this);
                    player.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        pos = posC;

    }



    public void stopPlayer(){
        if(player!=null){
            paused = false;
            data.get(pos).setState(STATE_PAUSED);
            notifyDataSetChanged();
            player.stop();
            player.release();
            player=null;
            progressHandler.stopProgress();
            pos=-1;
        }
    }

    public void pausePlayer(){
        data.get(pos).setState(STATE_PAUSED);
        notifyDataSetChanged();
        progressHandler.setPaused(true);
        this.paused = true;
        player.pause();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        progressHandler = new ProgressHandler(pos);
        progressHandler.execute();
        paused = false;
        data.get(pos).setState(STATE_PLAYING);
        notifyDataSetChanged();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopPlayer();
    }

    public class ProgressHandler extends AsyncTask<Integer,Integer, Integer>{

        boolean running;
        boolean pausedProgress;
        int posBar;

        public ProgressHandler(int posBar) {
            this.posBar = posBar;
            int duration = player.getDuration();
            bars.get(posBar).setMax(player.getDuration());
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            running = true;
            pausedProgress = false;
            while(running){
                try {
                    Thread.sleep(200);
                    if(!pausedProgress)
                        publishProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if(player==null){
                running = false;
                bars.get(posBar).setProgress(0);
            }else {
                int posssss = player.getCurrentPosition();
                bars.get(posBar).setProgress(player.getCurrentPosition());
            }
        }

        public void stopProgress(){
            running = false;
            bars.get(posBar).setProgress(0);
        }

        public void setPaused(boolean paused){
            pausedProgress = paused;
        }
    }

}

