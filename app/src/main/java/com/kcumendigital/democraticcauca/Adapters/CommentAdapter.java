package com.kcumendigital.democraticcauca.Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcumendigital.democraticcauca.Models.Comment;
import com.kcumendigital.democraticcauca.R;
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
    public static final int BTN_OVERFLOW = 3;
    public static final int BTN_OVERFLOW_VOICE = 4;
    public static final int STATE_PAUSED=0;
    public static final int STATE_PLAYING=1;
    public static final int STATE_DOWNLOADING=2;
    public static final int SHARE =5;
    public static final int REPORT =6;

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

    int sizeAvatar;

    public interface OnItemClickListener {
        void onItemClick(int position, int button, View v);
    }



    public CommentAdapter(OnItemClickListener onItemClick, Context context, List<Comment> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
        this.onItemClick = onItemClick;
        pos = -1;
        bars = new HashMap<>();

        transformation = new RoundedTransformationBuilder().oval(true).build();
        sizeAvatar = context.getResources().getDimensionPixelSize(R.dimen.forum_avatar);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {

            ((TextViewHolder) holder).nombreUsuario.setText(data.get(position).getUser().getName());
            ((TextViewHolder) holder).likes.setText("" + data.get(position).getLikes());
            ((TextViewHolder) holder).dislikes.setText("" + data.get(position).getDislikes());
            ((TextViewHolder) holder).comment.setText(data.get(position).getDescription());
            ((TextViewHolder) holder).likes.setOnClickListener(this);
            ((TextViewHolder) holder).dislikes.setOnClickListener(this);
            ((TextViewHolder) holder).share.setOnClickListener(this);
            ((TextViewHolder) holder).reportPapu.setOnClickListener(this);
            ((TextViewHolder) holder).share.setTag(position);
            ((TextViewHolder) holder).reportPapu.setTag(position);

            ((TextViewHolder) holder).btnLike.setOnClickListener(this);
            ((TextViewHolder) holder).btnLike.setTag(position);
            ((TextViewHolder) holder).btnDislike.setOnClickListener(this);
            ((TextViewHolder) holder).btnDislike.setTag(position);

            Picasso.with(context).load(data.get(position).getUser().getImg())
                    .resize(sizeAvatar, sizeAvatar)
                    .centerCrop()
                    .transform(transformation).into(((TextViewHolder) holder).imgPerfil);

            //  comment,likes,dislikes,titulo;
            if (data.get(position).getReport() > 100) {
                ((TextViewHolder) holder).maskReport.setVisibility(View.VISIBLE);
               FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((TextViewHolder) holder).linearParent.getLayoutParams();
                lp.height = dpToPx(60);
                       ((TextViewHolder) holder).maskReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        ((TextViewHolder) holder).linearParent.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                });
            }
        }

        if (holder instanceof VoiceViewHolder) {

                ((VoiceViewHolder) holder).nombreUsuario.setText(data.get(position).getUser().getName());
            ((VoiceViewHolder) holder).btn_play.setOnClickListener(this);
            ((VoiceViewHolder) holder).btn_play.setOnClickListener(this);
            ((VoiceViewHolder) holder).btnLike.setOnClickListener(this);
                ((VoiceViewHolder) holder).btnLike.setTag(position);
                ((VoiceViewHolder) holder).btnDislike.setOnClickListener(this);
                ((VoiceViewHolder) holder).btnDislike.setTag(position);
                ((VoiceViewHolder) holder).share.setOnClickListener(this);
                ((VoiceViewHolder) holder).reportPapu.setOnClickListener(this);
                ((VoiceViewHolder) holder).share.setTag(position);
                ((VoiceViewHolder) holder).btn_play.setTag(position);
                ((VoiceViewHolder) holder).reportPapu.setTag(position);
                ((VoiceViewHolder) holder).btn_play.setTag(position);
                if (data.get(position).getState() == STATE_PAUSED) {
                    ((VoiceViewHolder) holder).btn_play.setVisibility(ImageView.VISIBLE);
                    ((VoiceViewHolder) holder).downloading.setVisibility(ImageView.GONE);
                    ((VoiceViewHolder) holder).btn_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else if (data.get(position).getState() == STATE_PLAYING) {
                    ((VoiceViewHolder) holder).btn_play.setVisibility(ImageView.VISIBLE);
                    ((VoiceViewHolder) holder).downloading.setVisibility(ImageView.GONE);
                    ((VoiceViewHolder) holder).btn_play.setImageResource(R.drawable.ic_pause_black_24dp);
                } else {
                    ((VoiceViewHolder) holder).btn_play.setVisibility(ImageView.GONE);
                    ((VoiceViewHolder) holder).downloading.setVisibility(ImageView.VISIBLE);
                }

                Picasso.with(context).load(data.get(position).getUser().getImg())
                        .resize(sizeAvatar, sizeAvatar)
                        .centerCrop()
                        .transform(transformation).into(((VoiceViewHolder) holder).imgPerfil);

                bars.put(position, ((VoiceViewHolder) holder).progress);

            if (data.get(position).getReport() > 100) {
                ((VoiceViewHolder) holder).maskReport.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((VoiceViewHolder) holder).cardParent.getLayoutParams();
                lp.height = dpToPx(70);
                        ((VoiceViewHolder) holder).maskReport.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((VoiceViewHolder) holder).cardParent.getLayoutParams();
                        lp.height = dpToPx(125);

                    }
                });

            }
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

        TextView comment, likes, dislikes, nombreUsuario,maskReport;
        ImageView imgPerfil, overflow;
        ImageButton share,reportPapu;
        LinearLayout btnLike, btnDislike;
        CardView cardParent;
        LinearLayout linearParent;

        public TextViewHolder(View itemView) {
            super(itemView);
            linearParent = (LinearLayout) itemView.findViewById(R.id.parent_id);
            cardParent = (CardView) itemView.findViewById(R.id.card_parent);
            maskReport = (TextView) itemView.findViewById(R.id.mask_report);
            comment = (TextView) itemView.findViewById(R.id.comment);
            likes = (TextView) itemView.findViewById(R.id.value_like);
            dislikes = (TextView) itemView.findViewById(R.id.value_dislike);
            nombreUsuario = (TextView) itemView.findViewById(R.id.nombreUsuario);
            imgPerfil = (ImageView) itemView.findViewById(R.id.imgPerfil);
            share = (ImageButton) itemView.findViewById(R.id.share);
            reportPapu = (ImageButton) itemView.findViewById(R.id.report_papu);
            btnLike = (LinearLayout) itemView.findViewById(R.id.btn_like_text);
            btnDislike = (LinearLayout) itemView.findViewById(R.id.btn_dislike_text);
        }
    }

    public class VoiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPerfil;
        ImageButton btn_play;
        ProgressBar downloading;
        ProgressBar progress;
        ImageView overflowVoice;
        ImageButton share,reportPapu;
        TextView nombreUsuario;
        TextView maskReport;
        LinearLayout btnLike;
        LinearLayout btnDislike;
        CardView cardParent;

        public VoiceViewHolder(View itemView) {
            super(itemView);
            cardParent = (CardView) itemView.findViewById(R.id.card_parent);
            imgPerfil = (ImageView) itemView.findViewById(R.id.imgPerfilvoice);
            btn_play = (ImageButton) itemView.findViewById(R.id.playVoice);
            downloading = (ProgressBar) itemView.findViewById(R.id.downloadingVoice);
            progress = (ProgressBar) itemView.findViewById(R.id.progressVoice);
            share = (ImageButton) itemView.findViewById(R.id.share);
            reportPapu = (ImageButton) itemView.findViewById(R.id.report_papu);
            nombreUsuario = (TextView) itemView.findViewById(R.id.nombre_usuario);
            maskReport = (TextView) itemView.findViewById(R.id.mask_report);
            btnLike = (LinearLayout) itemView.findViewById(R.id.btn_like);
            btnDislike = (LinearLayout) itemView.findViewById(R.id.btn_dislike);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.playVoice:
                int posC = (Integer) view.getTag();
                handlePlayer(data.get(posC).getFile(), posC);
                break;
            case R.id.btn_like_text:
                onItemClick.onItemClick((Integer) view.getTag(), BTN_LIKE, view);
                break;
            case R.id.btn_dislike_text:
                onItemClick.onItemClick((Integer) view.getTag(), BTN_DISLIKE, view);
                break;

            case R.id.btn_like:
                onItemClick.onItemClick((Integer) view.getTag(), BTN_LIKE, view);
                break;
            case R.id.btn_dislike:
                onItemClick.onItemClick((Integer) view.getTag(), BTN_DISLIKE,view);
                break;

            case R.id.overflowVoice:
                onItemClick.onItemClick(0,BTN_OVERFLOW_VOICE,view);
                break;
            case R.id.share:
                onItemClick.onItemClick((Integer) view.getTag(),SHARE,view);

                break;

            case R.id.report_papu:
                onItemClick.onItemClick((Integer) view.getTag(),REPORT,view);
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}

